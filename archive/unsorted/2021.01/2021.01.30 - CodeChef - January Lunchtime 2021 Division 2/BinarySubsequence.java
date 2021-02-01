package contest;

import template.algo.LIS;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;

public class BinarySubsequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }
        int[] cnts = new int[2];
        for (int i = 0; i < n; i++) {
             cnts[s[i]]++;
        }
        int ans = cnts[0];
        int delete = 0;
        int ps = cnts[1];
        for (int i = n - 1; i >= 0; i--) {
            if (s[i] != 0) {
                ps--;
                continue;
            }
            if(s[i] == 0){
                ans = Math.min(ans, delete + ps);
            }
            delete++;
        }
        out.println(ans);
    }
}
