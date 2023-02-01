import java.util.ArrayList;
import java.util.stream.Collectors;

public class TimeMeasure {
	private ArrayList<String> measured_times;
	public TimeMeasure(){
       this.measured_times = new ArrayList<String>(); 
    }
	public void add_results(String measured_time) {
		System.out.print("adding time");
		measured_times.add(measured_time);
	}
	public String getTimes(){
		ArrayList<String> time = new ArrayList<String>();
		time = measured_times;
		String collect = time.stream().collect(Collectors.joining(","));
		return collect;
	}
}
