/*
 * Class used for storing measured increment and sum times for different sizes of given data structure.
 * Code for this class is written by me and was also used in homework 1. 
 */

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TimeInventory {
	private ArrayList<String> inc_times; 
	private ArrayList<String> sum_times;
	
	public TimeInventory(ArrayList<String> inc_times, ArrayList<String> sum_times)
    {
       this.inc_times = inc_times;
       this.sum_times = sum_times;
    }
	public String getIncTimes(){
		String collect = inc_times.stream().collect(Collectors.joining(","));
		return collect;
	}
	public String getSumTimes(){
		String collect = sum_times.stream().collect(Collectors.joining(","));
		return collect;
	}
}
