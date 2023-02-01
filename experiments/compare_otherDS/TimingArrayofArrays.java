/*
 * Code to run the increment/sum experiments on the array of arrays.
 * 
 * The class was implemented without prior source code, based on the structure description during the classes;
 * two code elements are adopted from the outside sources:
 * 1) the search method is a simple search using a loop through the arrays written by me and a build-in binarySearch method in Java (https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#binarySearch(int[],%20int))
 * 2) the merge performed when adding a new element uses a common algorithm for merging two sorted arrays, based on Merge Sort; it can be found
 * in similar form online, e.g. https://www.geeksforgeeks.org/merge-two-sorted-arrays/. I use Merge Sort method because it was suggested during classes.
 * 
 * Authorship of the remaining code is mine.
 */

 import java.io.BufferedWriter;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Path;
 import java.nio.file.StandardOpenOption;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Random;
 
 public class TimingArrayofArrays {
     
     public static void main(String[] args) throws IOException {
         int num_loops = Integer.parseInt(args[0]);
         String dir_path = args[1];
         TimeInventory times = run_loops(num_loops);		
         save_array(times,dir_path,"increment_arrofarr.csv","search_arrofarr.csv");
         
     }
     
     public static TimeInventory run_loops(int num_loops) {
         //initialize arrays to save measurements
         ArrayList<String> inc_times = new ArrayList<String>();
         ArrayList<String> search_times = new ArrayList<String>();
         //loop for each DS size (2^10 to 2^30)
         for (int k=10;k<num_loops;k++){
             System.out.println(k);
             //find the number of elements to add
             double powK = (int) Math.pow(2,k);
             //initialize an empty array
             int [][] arrOfarr_empty = new int[1][];
             //start timer
             long startIncTime = System.nanoTime();
             //add elements in loop
             int [][] arrOfarr = increment(arrOfarr_empty,powK);
             //turn off timer (for increment) and calculate difference
             long incDuration = System.nanoTime() - startIncTime;  
             //turn on timer (for search)
             long startSearchTime = System.nanoTime();
             //search 100 000 elements 
             run_search(arrOfarr);
             //turn off timer (for search) and calculate difference
             long searchDuration = System.nanoTime() - startSearchTime; 
             //add times to the arrays
             final String inc_time_seconds = String.valueOf((double)incDuration / 1000000000);
             inc_times.add(inc_time_seconds);
             final String search_time_seconds = String.valueOf((double)searchDuration / 1000000000);
             search_times.add(search_time_seconds);     
         }
         //return both lists
         TimeInventory times = new TimeInventory(inc_times,search_times);
         return times;
     }
     public static int[][] increment(int[][] arr, double powK){
         //initialize number generator
         Random rd = new Random(); 
         //increment in a loop up to 2^n
         for (int x=0;x<powK;x++) {
             //put the new element in the temporary array of size 1
             int[] temp_arr = new int[1];
             int temp_size = 1;
             temp_arr[0]= 1+rd.nextInt((int) Math.pow(2,30)-1);//random integer from 1 to 2^30
 
             //look for the first empty array to which we can copy
             for (int i=0;i<powK;i++) { //the max number of the arrays we can iterate through is the number of added elements (but it will never happen, as each array would have only one element, the loop breaks earlier)
                 //check if there exists an array of size same as the temporary
                 //if yes, check if empty, if not array of arrays need to be copied to a new one
                 //find first array in which the temporary array could fit (checking if such exists)
                 if (arr.length>=temp_size) {
                     //check if it's empty
                     if (arr[temp_size-1]==null) {
                         //copy temporary to corresponding array
                         arr[temp_size-1]=temp_arr;
                         //clear all smaller arrays 
                         for (int n=0;n<temp_size-1;n++) {
                             arr[n]=null;
                             
                         }
                         //exit loop
                         break;
                     }
                     else {
                         //merge the two arrays
                         temp_arr = merge(temp_arr,arr[i]); 
                         temp_size++;
                     }
                 }
                 else {
                     //add new array to array of arrays 
                     arr = add_level(arr);
                     
                 }
                 
             }
             
         }
         return arr;	
     }
     /*
      * Helper method for array incrementation, adds new array to array of arrays
      */	
     public static int[][] add_level(int arr[][]){
         //create larger array
         int[][] new_arr = new int[arr.length+1][];
         //copy all elements
         for (int i=0;i<arr.length;i++) {
             new_arr[i]=arr[i];
         }
         return new_arr;
         
     }
 
     
      
      //Method for searching in in the array of arrays, with i arrays, each array has j elements - j can be different for each i.
      
     public static void run_search(int[][] arr) {
         //initialize random number generator
         Random rd1 = new Random();
         //run search loop 100 000 times
         for (int k=0;k<100000;k++) {
             int x = 1+rd1.nextInt((int) Math.pow(2,30)-1);//random integer from 1 to 2^30
             int[] search_result = new int[2];//array for storing search results
             int found_index = -1;//the index within the array that contains the element (default -1: "not found")
             //for each array in the array of arrays
             for(int i=0; i < arr.length; i++) {
                 //check if the array has any elements
                 if (arr[i]!=null) {
                     //run binary search (using java standard library: https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#binarySearch(int[],%20int))
                     found_index = Arrays.binarySearch(arr[i], x);
                     }
                 if (found_index>=0) {//if the element is found in the array, add result to result array (Arrays.binarySearch returns values below 0 if not found)
                     search_result[0]=i;
                     search_result[1]=found_index;
                     break;//search only until first mention is found
                 }
             }
             if (found_index<0){
                 //if not found in any of the arrays, return [-1,-1]
                 search_result[0]=-1;
                 search_result[1]=-1;  
             }
         //the function doesn't actually return the indices (search results), because we don't need them to measure search time (but they are stored in "search_result" variable and can be accessed for testing
         //System.out.println(Arrays.toString(search_result)); //to print search results for testing
         }
     }
         
     
     //Helper method for incrementing array of arrays; merges two sorted arrays; based on a modified code from https://www.geeksforgeeks.org/merge-two-sorted-arrays/
     public static int[] merge(int[] arr1,int arr2[]){
         int len1 = arr1.length;
         int len2 = arr2.length;
         //define the new array that will contain merged elements
         int[] new_arr = new int[len1+len2];
         merge_loops(arr1,arr2,new_arr,len1,len2);
         return new_arr;
     }
     public static void merge_loops(int[] arr1,int[] arr2, int[] arr3, int len1, int len2){
         int i = 0, j = 0, k = 0;
         //loop through both arrays to merge
         while (i<len1 && j <len2) {
             //compare elements (indicated by pointers) in the two arrays
             if (arr1[i] < arr2[j]) {
                 arr3[k++] = arr1[i++];
             }
             else {
                 arr3[k++] = arr2[j++];
             }	
         }
         //copy remaining elements of the first array
         while (i < len1) {
             arr3[k++] = arr1[i++];
         }
         //copy remaining elements of the second array
         while (j < len2){
             arr3[k++] = arr2[j++];
         }
     }
     
 
     //Helper method to print a jagged array in Java (only used for testing)
     static void print_array(int[][] arr) {
         for (int index = 0; index < arr.length; index++) {
             System.out.println(Arrays.toString(arr[index]));
         }
     }
 
 public static void save_array(TimeInventory times,String dir_path,String inc_file, String sum_file) throws IOException {
     
     Path folder_dir = Path.of(dir_path);
     Path inc_filepath = folder_dir.resolve(inc_file);
     Path sum_filepath = folder_dir.resolve(sum_file);
     String increment_line = times.getIncTimes();
     String sum_line = times.getSumTimes();
     
     if(!Files.exists(inc_filepath)){
         //create a file		
         Files.createFile(inc_filepath);
       //write a row to it
         try (BufferedWriter writer = Files.newBufferedWriter(inc_filepath, StandardOpenOption.APPEND)) {
             writer.write(increment_line);		    
         } catch (IOException ioe) {
             System.err.format("IOException: %s%n", ioe);
         }
     }
     else {
         //append a row to the file
         String newline_increment = System.lineSeparator() + increment_line;
         try (BufferedWriter writer = Files.newBufferedWriter(inc_filepath, StandardOpenOption.APPEND)) {
             writer.write(newline_increment);
             System.out.printf("File is located at %s%n", inc_filepath);
         } catch (IOException ioe) {
             System.err.format("IOException: %s%n", ioe);
         }
         
     }
     if(!Files.exists(sum_filepath)){
         //create a file		
         Files.createFile(sum_filepath);
       //write a row to it
         try (BufferedWriter writer = Files.newBufferedWriter(sum_filepath, StandardOpenOption.APPEND)) {
             writer.write(sum_line);
         } catch (IOException ioe) {
             System.err.format("IOException: %s%n", ioe);
         }
     }
     else {
         //append a row to the file
         String newline_sum = System.lineSeparator() + sum_line;
         try (BufferedWriter writer = Files.newBufferedWriter(sum_filepath, StandardOpenOption.APPEND)) {
             writer.write(newline_sum);
         } catch (IOException ioe) {
             System.err.format("IOException: %s%n", ioe);
         }
         
     }
 }
 
 }