package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class FairPlay {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long sum = 0;
        for(int i = 0; i < m; i++){
            sum += in.ri();
        }
        long remainder = sum % n;
        out.println(remainder);
    }
}
