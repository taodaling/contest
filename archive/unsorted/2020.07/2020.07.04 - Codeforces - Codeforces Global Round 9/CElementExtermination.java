package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CElementExtermination {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        if(a[0] < a[n - 1]){
            out.println("YES");
        }else{
            out.println("NO");
        }
    }
}
