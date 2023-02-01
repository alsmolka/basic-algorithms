import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ThreadLocalRandom;


public class TimeInsert {
	public static void main(String[] args) throws IOException {
		String dir_path = args[0];
		for (int i =1;i<11;i++) {
			TimeMeasure times = run_sort();
			save_results(times,dir_path,"insert");
		}
		
	}
	public static TimeMeasure run_sort() {
		TimeMeasure times = new TimeMeasure();
		for (int k=1;k<23;k++){
			//create an array of size
			double powK = (int) Math.pow(2,k);
		    int[] array = new int[(int) powK];
			for (int i = 0; i < powK; i++) {
		        array[i] =ThreadLocalRandom.current().nextInt(1, 1001);
		        //System.out.print(array[i] +" ");
		    }
			String insert_time = insert_sort(array);
			//System.out.println(Arrays.toString(array));
			times.add_results(insert_time);
		}
		
	return times;
		
		
	}
	private static String insert_sort(int[] array) {
		//start timing
		long startIncTime = System.nanoTime();
		//sort array
		for (int i=1;i<array.length;++i) {
			int val = array[i];
			int j = i-1;
			while (j>=0 && array[j]>val) {
				array[j+1] = array[j];
				j--;			
			}
			array[j+1] = val;
		}
		//System.out.println(Arrays.toString(array));
		//stop timing
		long incDuration = System.nanoTime() - startIncTime; 
		final String time_seconds = String.valueOf((double)incDuration / 1000000000);
		return time_seconds;
	}
	
	public static void save_results(TimeMeasure times,String dir_path,String sort_type){
		save_array(times,dir_path,"insert");
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
