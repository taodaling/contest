package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class EvenSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long sum = 0;
        for(int i = 0; i < n; i++){
            int x = in.ri();
            sum += x;
        }
        out.println(sum % 2 == 0 ? 1 : 2);
    }
}
