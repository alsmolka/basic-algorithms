import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class MeasureTime {
	public static void main(String[] args) throws IOException {
		String arr_type = args[0];
		int num_loops = Integer.parseInt(args[1]);
		String dir_path = args[2];
		System.out.println("Array type:  "+arr_type);
		if (Objects.equals(arr_type,"dynamic_array")){
			//TimingDynamicArray dynTimer = new TimingDynamicArray();
			
			TimeInventory dyn_times = TimingDynamicArray.time_loops(num_loops);
			save_array(dyn_times,dir_path,"dyn_inc_times.csv","dyn_sum_times.csv");	
		}
		else if (Objects.equals(arr_type,"linked_list")){
			//TimingLinkedList llTimer = new TimingLinkedList();
			TimeInventory ll_times = TimingLinkedList.time_loops(num_loops);
			save_array(ll_times,dir_path,"ll_inc_times.csv","ll_sum_times.csv");	
		}
	}
	
	public static void save_array(TimeInventory times,String dir_path,String inc_file, String sum_file) throws IOException {
		
		Path folder_dir = Path.of(dir_path);
		Path inc_filepath = folder_dir.resolve(inc_file);
		System.out.println(inc_filepath);
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
