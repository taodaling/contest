package contest;

import template.FastInput;
import template.FastOutput;
import template.SAIS;
import template.SuffixArray;

import java.util.Arrays;

public class LUOGU3809 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[1000000];
        int len = in.readString(s, 0);
        SuffixArray sa = new SuffixArray(Arrays.copyOf(s, len), 0, 128);
        for(int i = 1; i <= len; i++){
            out.append(sa.getSuffixByRank(i).getSuffixStartIndex() + 1).append(' ');
        }
    }
}
