package on2019_11.on2019_11_30_Educational_Codeforces_Round_72__Rated_for_Div__2_.LUOGU3809;



import strings.SuffixArrayDC3;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class LUOGU3809 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        int[] sa = SuffixArrayDC3.suffixArray(s);
        for(int i = 0; i < sa.length; i++){
            out.append(sa[i] + 1).append(' ');
        }
        int[] lcp = SuffixArrayDC3.lcp(sa, s);
        System.out.append(Arrays.toString(lcp));
    }
}
