import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.concurrent.ThreadLocalRandom;


public class Time3way {
	public static void main(String[] args) throws IOException {
		String dir_path = args[0];
		for (int i =1;i<11;i++) {
			System.out.println(i);
			TimeMeasure times = run_sort();
			save_results(times,dir_path,"3way");
		}

		
	}
	public static TimeMeasure run_sort() {
		TimeMeasure times = new TimeMeasure();
		for (int k=1;k<31;k++){
			//create an array of size
			double powK = (int) Math.pow(2,k);
			System.out.println(powK);
		    int[] array = new int[(int) powK];
			for (int i = 0; i < powK; i++) {
		        
		        array[i] =ThreadLocalRandom.current().nextInt(1, 1001);
		        //System.out.print(array[i] +" ");
		    }
			//run all sorts
			String thway_time = threeway_sort(array);
			//System.out.println(Arrays.toString(array));
			times.add_results(thway_time);
		}
	return times;	
	}
	private static String threeway_sort(int[] array) {
		//start timing
		long startIncTime = System.nanoTime();
		StdRandom.shuffle(array);
		//sort array
		quick_sort_threeway(array,0,array.length-1);
		//stop timing
		long incDuration = System.nanoTime() - startIncTime; 
		final String time_seconds = String.valueOf((double)incDuration / 1000000000);
		//System.out.println(Arrays.toString(array));
		return time_seconds;
	}
	private static void quick_sort_threeway(int[] array,int pos_l,int pos_h) {
		if (pos_l >= pos_h)   return;
		int m = pos_l + (pos_h - pos_l) / 2;
        int p = array[m];
        swap_elements(array,pos_l,m);
        int lt = pos_l; 
        int gt = pos_h;
        int i = pos_l + 1;
        while (i <= gt) {
            // move elements < pivot to the front
            if (array[i] < p) {
            	swap_elements(array, i++, lt++);
            }
            else if (array[i] > p) {
            	swap_elements(array, i, gt--);
            }	
            else    {
            	i++;
            }
        }
        quick_sort_threeway(array,pos_l,lt-1);
        quick_sort_threeway(array,gt+1,pos_h);
        
	}
	
	private static void swap_elements(int[] array, int pos1,int pos2) {
		int pos1_copy = array[pos1];
		array[pos1] = array[pos2];
		array[pos2] = pos1_copy;
	}
	
	public static void save_results(TimeMeasure times,String dir_path,String sort_type){
		save_array(times,dir_path,"threeway");
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
