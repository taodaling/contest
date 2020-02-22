package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ACowAndHaybales {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d = in.readInt();
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }
        int remain = d;
        int ans = a[0];
        for(int i = 1; i < n; i++){
            int unit = i;
            if(remain < unit){
                break;
            }
            int move = Math.min(a[i], remain / unit);
            remain -= move * unit;
            ans += move;
        }

        out.println(ans);
    }
}
