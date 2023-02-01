
/*
 * Code to run the increment/search experiments on a btree
 * 
 * I use a open-source class BTree.java
 */

 import java.io.BufferedWriter;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Path;
 import java.nio.file.StandardOpenOption;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Random;
 
 
 public class TimingBTree {
     
     public static void main(String[] args) throws IOException {
         int num_loops = Integer.parseInt(args[0]);
         String dir_path = args[1];
         
         for (int i=0;i<3;i++) {
             TimeInventory times = run_loops(num_loops);
             save_array(times,dir_path,"increment_btree.csv","search_btree.csv");
         }
         
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
             //initialize an empty btree
             BTree<Integer, Integer> btree_empty =new BTree<Integer, Integer>();
             //start timer
             long startIncTime = System.nanoTime();
             //add elements in loop
             BTree<Integer, Integer> btree = increment(btree_empty,powK);
             //turn off timer (for increment) and calculate difference
             long incDuration = System.nanoTime() - startIncTime;  
             //turn on timer (for search)
             long startSearchTime = System.nanoTime();
             //search 100 000 elements 
             run_search(btree);
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
     public static BTree<Integer, Integer> increment(BTree<Integer, Integer> btree, double powK){
         //initialize number generator
         Random rd = new Random(); 
         //increment in a loop up to 2^n
         for (int x=0;x<powK;x++) {
             int y = 1+rd.nextInt((int) Math.pow(2,30)-1);
             btree.put(y, y);//insert a random integer between 1 and 2^30; key and value the same since we have no extra data
         }
         return btree;	
     }
 
     public static void run_search(BTree<Integer, Integer> btree) {
         //initialize random number generator
         Random rd1 = new Random();
         //run search loop 100 000 times
         for (int k=0;k<100000;k++) {	
             int x = 1+rd1.nextInt((int) Math.pow(2,30)-1);//random integer from 1 to 2^30
             Integer search_result = btree.get(x);
         }
         //the method doesn't actually return the results, because we don't need them to measure search time (but they are stored in "search_result" variable and can be accessed for testing
         //System.out.println(search_result); //to print search results for testing
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