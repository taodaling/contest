package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BCodeforcesSubsequences {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long k = in.readLong();
        String s = "codeforces";
        int[] cnts = new int[s.length()];
        Arrays.fill(cnts, 1);
        for (int i = 0; prod(cnts) < k; i++) {
            cnts[i % s.length()]++;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            for (int j = 0; j < cnts[i]; j++) {
                out.append(c);
            }
        }
    }

    public long prod(int[] cnts) {
        long prod = 1;
        for (int x : cnts) {
            prod *= x;
        }
        return prod;
    }
}
