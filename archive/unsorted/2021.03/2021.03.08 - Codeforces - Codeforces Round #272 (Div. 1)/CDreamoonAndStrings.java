package contest;

import template.datastructure.MonotoneOrderBeta;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;

public class CDreamoonAndStrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        char[] p = in.rs().toCharArray();
        int n = s.length;
        int m = p.length;
        int[] prev = new int[n + 1];
        int[] next = new int[n + 1];
        int inf = (int) 1e9;
        Arrays.fill(prev, inf);
        prev[0] = 0;
        for (char c : s) {
            Arrays.fill(next, inf);
            for (int i = 0, iModM = 0; i <= n; i++, iModM++) {
                if(iModM == m){
                    iModM = 0;
                }
                //give up
                next[i] = Math.min(next[i], prev[i] + Integer.signum(iModM));

                if (p[iModM] == c && i + 1 <= n) {
                    next[i + 1] = Math.min(next[i + 1], prev[i]);
                }
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }
        MonotoneOrderBeta<Integer, Integer> mo = new MonotoneOrderBeta<Integer, Integer>(Comparator.naturalOrder(), Comparator.naturalOrder(),
                true, true);
        mo.add(0, 0);
        for (int i = 0; i <= n; i++) {
            mo.add(prev[i], i / m);
        }
        for (int i = 0; i <= n; i++) {
            int floor = mo.floor(i);
            floor = Math.min(floor, (n - i) / m);
            out.println(floor);
        }
    }
}
