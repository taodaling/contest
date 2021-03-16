package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastSubsetTransform;

public class P6097 {
    int mod = (int)1e9 + 9;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int log = in.ri();
        int[] a = new int[1 << log];
        int[] b = new int[1 << log];
        for(int i = 0; i < 1 << log; i++){
            a[i] = in.ri();
        }
        for(int i = 0; i < 1 << log; i++){
            b[i] = in.ri();
        }
        int[] c = FastSubsetTransform.mul(a, b, mod);
        for(int x : c){
            out.append(x).append(' ');
        }
    }
}
