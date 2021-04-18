package on2021_03.on2021_03_26_Google_Coding_Competitions___Qualification_Round_2021___Code_Jam_2021.Reversort_Engineering;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;

public class ReversortEngineering {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        int n = in.ri();
        int c = in.ri();
        int originC = c;
        c -= (n - 1);
        String impossible = "IMPOSSIBLE";
        if (c < 0) {
            out.println(impossible);
            return;
        }
        int[] choice = new int[n];
        for (int i = 0; i < n - 1; i++) {
            int max = n - 1 - i;
            int actual = Math.min(max, c);
            c -= actual;
            choice[i] = actual + 1;
        }
        if(c > 0){
            out.println(impossible);
            return;
        }
        int[] perm = IntStream.range(0, n).toArray();
        for (int i = n - 2; i >= 0; i--) {
            int j = i + choice[i] - 1;
            SequenceUtils.reverse(perm, i, j);
        }
        for (int x : perm) {
            out.append(x + 1).append(' ');
        }
        out.println();
        assert check(perm) == originC;
    }

    long check(int[] perm){
        int n = perm.length;
        int[] a = perm.clone();
        long ans = 0;
        for (int i = 0; i < n - 1; i++) {
            int j = i;
            for (int k = i; k < n; k++) {
                if (a[k] < a[j]) {
                    j = k;
                }
            }
            ans += j - i + 1;
            SequenceUtils.reverse(a, i, j);
        }
        return ans;
    }
}
