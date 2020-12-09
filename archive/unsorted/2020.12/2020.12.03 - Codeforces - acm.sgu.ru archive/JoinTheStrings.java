package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class JoinTheStrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        String[] s = new String[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.rs();
        }
        Arrays.sort(s, (a, b) -> (a + b).compareTo(b + a));
        for (String x : s) {
            out.append(x);
        }
    }
}
