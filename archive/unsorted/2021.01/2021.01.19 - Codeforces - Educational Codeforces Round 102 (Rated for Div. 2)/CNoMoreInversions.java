package contest;

import template.datastructure.Treap;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class CNoMoreInversions {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int low = k - (n - k);
        for(int i = 1; i < low; i++){
            out.append(i).append(' ');
        }
        for(int i = k; i >= low; i--){
            out.append(i).append(' ');
        }
        out.println();
    }

}
