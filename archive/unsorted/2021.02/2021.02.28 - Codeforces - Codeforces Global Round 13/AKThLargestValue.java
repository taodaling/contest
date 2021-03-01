package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class AKThLargestValue {
    int[] a;
    int sum;
    void inverse(int i){
        sum -= a[i];
        a[i] ^= 1;
        sum += a[i];
    }
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        a = new int[n];
        in.populate(a);
        sum = Arrays.stream(a).sum();
        for(int i = 0; i < q; i++){
            int t = in.ri();
            int x = in.ri();
            if(t == 1){
                inverse(x - 1);
            }else{
                if(sum >= x){
                    out.println(1);
                }else{
                    out.println(0);
                }
            }
        }
    }
}
