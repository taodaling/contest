package on2021_04.on2021_04_24_.On_The_Clock;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OnTheClock {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        //for line y=kx, k = n / m
        List<int[]> all = new ArrayList<>((int) 2e6);
        for (int i = 0; i < m; i++) {
            int left = (int) ((long) n * i / m);
            int right = (int) ((long) n * (i + 1) / m);
            if ((long) n * (i + 1) % m == 0) {
                right--;
            }
            for (int j = left; j <= right; j++) {
                all.add(new int[]{j, i});
            }
        }
        all.sort(Comparator.<int[]>comparingInt(x -> x[0]).thenComparingInt(x -> x[1]));
        out.println(all.size());
        for(int[] xy : all){
            out.append(xy[0] + 1).append(' ').append(xy[1] + 1).println();
        }
    }


}
