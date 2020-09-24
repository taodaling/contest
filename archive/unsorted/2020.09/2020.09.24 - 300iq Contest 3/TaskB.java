package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] d = new int[n];
        in.populate(d);
        if(n == 2){
            out.println(1);
            return;
        }
        int cnt = (int) Arrays.stream(d).filter(x -> x == 1).count();
        out.println(Math.min(n - cnt, n / 2));
    }
}
