import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.concurrent.ThreadLocalRandom;


public class TimeMerge {
	public static void main(String[] args) throws IOException {
		String dir_path = args[0];
		for (int i =1;i<2;i++) {
			TimeMeasure times = run_sort();
			save_results(times,dir_path,"merge");
		}
		
		
	}
	public static TimeMeasure run_sort() {
		TimeMeasure times = new TimeMeasure();
		for (int k=1;k<31;k++){
			//create an array of size
			double powK = (int) Math.pow(2,k);
		    int[] array = new int[(int) powK];
			for (int i = 0; i < powK; i++) {
		        
		        array[i] =ThreadLocalRandom.current().nextInt(1, 1001);
		        //System.out.print(array[i] +" ");
		    }
			//run all sorts
			String merge_time = merge_sort(array);
			//System.out.println(Arrays.toString(array));
			times.add_results(merge_time);
		}
		
		
	return times;
		
		
	}
	private static String merge_sort(int[] array) {
		//start timing
		long startIncTime = System.nanoTime();
		//sort array
		sort_merge(array,0,array.length-1);
		//stop timing
		long incDuration = System.nanoTime() - startIncTime; 
		final String time_seconds = String.valueOf((double)incDuration / 1000000000);
		//System.out.println(Arrays.toString(array));
		return time_seconds;
	}
	
	private static void sort_merge(int[] array, int l, int r) {
		if (l<r) {
			int m = l + (r-l)/2;
			sort_merge(array,l,m);
			sort_merge(array,m+1,r);
			merge(array,l,m,r);		
		}
	}
	private static void merge(int[] array, int l, int m, int r) {
		int len1 = m-l+1;
		int len2 = r-m;
		
		int left[] = new int[len1];
		int right[] = new int[len2];
		
		for (int i = 0; i<len1;++i) {
			left[i] = array[l+i];
		}
		for (int i = 0; i<len2;++i) {
			right[i] = array[m+1+i];
		}
		int i = 0;
		int j = 0;
		
		int k = l;
        while (i < len1 && j < len2) {
            if (left[i] <= right[j]) {
                array[k] = left[i];
                i++;
            }
            else {
                array[k] = right[j];
                j++;
            }
            k++;
        }
        while (i < len1) {
            array[k] = left[i];
            i++;
            k++;
        }
 
        while (j < len2) {
            array[k] = right[j];
            j++;
            k++;
        }
	}
	public static void save_results(TimeMeasure times,String dir_path,String sort_type){
		save_array(times,dir_path,"merge");
	}
	public static void save_array(TimeMeasure times,String dir_path,String sort_type) {
		Path folder_dir = Path.of(dir_path);
		String filename = sort_type + ".csv";
		Path save_filepath = folder_dir.resolve(filename);
		String res_line = times.getTimes();
		
		if(!Files.exists(save_filepath)){
            //create a file		
		    try {
				Files.createFile(save_filepath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		  //write a row to it
		    try (BufferedWriter writer = Files.newBufferedWriter(save_filepath, StandardOpenOption.APPEND)) {
			    writer.write(res_line);		    
			} catch (IOException ioe) {
			    System.err.format("IOException: %s%n", ioe);
			}
        }
		else {
			//append a row to the file
			String newline_increment = System.lineSeparator() + res_line;
			try (BufferedWriter writer = Files.newBufferedWriter(save_filepath, StandardOpenOption.APPEND)) {
			    writer.write(newline_increment);
			    System.out.printf("File is located at %s%n", save_filepath);
			} catch (IOException ioe) {
			    System.err.format("IOException: %s%n", ioe);
			}
			
		}
		
	}

	

}
