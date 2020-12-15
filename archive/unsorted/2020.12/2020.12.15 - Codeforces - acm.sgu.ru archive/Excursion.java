package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class Excursion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        int n = in.ri();
        int now = b;
        List<int[]> ans = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            int boy = 0;
            int girl = 0;
            if (x > now) {
                a -= x - now;
                boy += x - now;
            } else {
                b -= now - x;
                girl += now - x;
            }
            now = x;
            if (a < 0 || b < 0) {
                out.append("ERROR");
                return;
            }
            ans.add(new int[]{boy, girl});
        }
        for(int[] pair : ans){
            out.append(pair[0]).append(' ').append(pair[1]).println();
        }
    }
}
