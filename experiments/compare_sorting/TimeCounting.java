import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.concurrent.ThreadLocalRandom;


public class TimeCounting {
	public static void main(String[] args) throws IOException {
		String dir_path = args[0];
		for (int i =1;i<11;i++) {
			TimeMeasure times = run_sort();
			save_results(times,dir_path,"counting");
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
			String count_time = counting_sort(array);
			//System.out.println(Arrays.toString(array));
			times.add_results(count_time);
		}
		
		
	return times;
		
		
	}
	
	private static String counting_sort(int[] array) {
		//start timing
		long startIncTime = System.nanoTime();
		//sort array
		sort_count(array);
		//stop timing
		long incDuration = System.nanoTime() - startIncTime; 
		final String time_seconds = String.valueOf((double)incDuration / 1000000000);
		//System.out.println(Arrays.toString(array));
		return time_seconds;
		
	}
	private static void sort_count(int[] array) {
		int[] new_array = new int[array.length+1];
		int max = array[0];
	    for (int i = 1; i < array.length; i++) {
	      if (array[i] > max)
	        max = array[i];
	    }
	    int[] counts = new int[max + 1];
	    for (int i = 0; i < max; ++i) {
	        counts[i] = 0;
	      }
	    for (int i = 0; i < array.length; i++) {
	        counts[array[i]]++;
	      }
	    for (int i = 1; i <= max; i++) {
	        counts[i] += counts[i-1];
	      }
	    for (int i = array.length - 1; i >= 0; i--) {
	        new_array[counts[array[i]] - 1] = array[i];
	        counts[array[i]]--;
	      }
	    for (int i = 0; i < array.length; i++) {
	        array[i] = new_array[i];
	      }
	}
	
	
	public static void save_results(TimeMeasure times,String dir_path,String sort_type){
		save_array(times,dir_path,"counting");
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
