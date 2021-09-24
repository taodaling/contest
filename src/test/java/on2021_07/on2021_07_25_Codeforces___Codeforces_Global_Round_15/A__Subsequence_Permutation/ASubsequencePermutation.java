package on2021_07.on2021_07_25_Codeforces___Codeforces_Global_Round_15.A__Subsequence_Permutation;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class ASubsequencePermutation {
    char[] s = new char[(int) 1e5];
    char[] clone = new char[(int) 1e5];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        in.rs(s);
        for (int i = 0; i < n; i++) {
            clone[i] = s[i];
        }
        Randomized.shuffle(clone, 0, n);
        Arrays.sort(clone, 0, n);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if(clone[i] != s[i]){
                ans++;
            }
        }
        out.println(ans);
    }
}
