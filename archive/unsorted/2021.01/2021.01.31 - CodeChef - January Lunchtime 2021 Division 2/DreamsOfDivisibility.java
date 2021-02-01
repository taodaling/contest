package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DreamsOfDivisibility {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        while(k % 2 == 0){
            k /= 2;
        }
        int[] a = new int[n];
        in.populate(a);
        for(int i = 0; i < n; i++){
            if(a[i] % k != 0){
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}
