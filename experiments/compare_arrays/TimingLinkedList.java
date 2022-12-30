import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;

public class TimingLinkedList {
 
    public static TimeInventory time_loops(int num_loop) {
    	//initialize arrays to save the time measures
        ArrayList<String> inc_times = new ArrayList<String>();
        ArrayList<String> sum_times = new ArrayList<String>();
        for (int k=0;k<num_loop;k++){
            double powK = (int) Math.pow(2,k);
            System.out.println("Iteration: "+k);
            System.out.println("Number of random numbers to add: "+powK);
            //initialize an empty linked list
            LinkedList<Integer> ll = new LinkedList<Integer>();
            //turn on timer (for increment)
            Random rd = new Random(); 
            long startIncTime = System.nanoTime();
            //increment list one element at a time
            for (int x=0;x<powK;x++){
            	//add random number to the array  
                ll.add(rd.nextInt());
            }
            //turn of timer (for increment)
            long incDuration = System.nanoTime() - startIncTime;  
            //turn on timer (for sum)
            long startSumTime = System.nanoTime();
            //sum elements one at a time
            int sum = 0;
            for (int x: ll){
            	//add one element to sum 
            	sum+=x;
            }
            
            //turn off timer (for sum)
            long sumDuration = System.nanoTime() - startSumTime; 
            System.gc();
            //add times to the arrays
            final String inc_time_seconds = String.valueOf((double)incDuration / 1000000000);
            inc_times.add(inc_time_seconds);
            final String sum_time_seconds = String.valueOf((double)sumDuration / 1000000000);
            sum_times.add(sum_time_seconds);            
        }
        //return both lists
        TimeInventory times = new TimeInventory(inc_times,sum_times);
		return times;
    }

}
