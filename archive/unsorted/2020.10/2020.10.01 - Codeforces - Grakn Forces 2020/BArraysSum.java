package contest;

import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;

public class BArraysSum {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int delta = 1;
        for(int i = 1; i < n; i++){
            if(a[i] != a[i - 1]){
                delta++;
            }
        }

        if(k == 1 && delta > 1){
            out.println(-1);
            return;
        }

        int ans = 1;
        delta -= k;
        while(delta > 0){
            delta -= k - 1;
            ans++;
        }

        out.println(ans);
    }
}
