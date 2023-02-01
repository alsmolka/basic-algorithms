import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.concurrent.ThreadLocalRandom;


public class TimeLomuto {
	public static void main(String[] args) throws IOException {
		String dir_path = args[0];
		for (int i =1;i<11;i++) {
			TimeMeasure times = run_sort();
			save_results(times,dir_path,"lomuto");
		}

		
	}
	public static TimeMeasure run_sort() {
		TimeMeasure times = new TimeMeasure();
		for (int k=1;k<26;k++){
			//create an array of size
			double powK = (int) Math.pow(2,k);
		    int[] array = new int[(int) powK];
			for (int i = 0; i < powK; i++) {
		        
		        array[i] =ThreadLocalRandom.current().nextInt(1, 1001);
		        //System.out.print(array[i] +" ");
		    }
			String lomuto_time = lomuto_sort(array);
			//System.out.println(Arrays.toString(array));
			times.add_results(lomuto_time);
		}
		
		
	return times;
		
		
	}
	private static String lomuto_sort(int[] array) {
		//start timing
		long startIncTime = System.nanoTime();
		StdRandom.shuffle(array);
		//sort array
		quick_sort_lomuto(array,0,array.length-1);
		//stop timing
		long incDuration = System.nanoTime() - startIncTime; 
		final String time_seconds = String.valueOf((double)incDuration / 1000000000);
		//System.out.println(Arrays.toString(array));
		return time_seconds;
	}
	private static void quick_sort_lomuto(int[] array,int pos_l,int pos_h) {
		int l = pos_l;
		int h = pos_h;
		if (l<h) {
			int s = split_lomuto(array,l,h);
			quick_sort_lomuto(array,pos_l,s-1);
			quick_sort_lomuto(array,s+1,pos_h);
			}
		}
	private static int split_lomuto(int[] array,int pos_l, int pos_h) {
			int p = array[pos_h];
			int i = pos_l-1;
			for (int j=pos_l;j<=pos_h-1;j++) {
				if (array[j]<=p) {
					i++;
					swap_elements(array,i,j);
				}
			}
		swap_elements(array,i+1,pos_h);
		return (i+1);
		}
	private static void swap_elements(int[] array, int pos1,int pos2) {
		int pos1_copy = array[pos1];
		array[pos1] = array[pos2];
		array[pos2] = pos1_copy;
	}
	
	public static void save_results(TimeMeasure times,String dir_path,String sort_type){
		save_array(times,dir_path,"lomuto");
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
