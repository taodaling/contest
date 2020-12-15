package on2020_12.on2020_12_12_Library_Checker.Suffix_Array;



import strings.SuffixArrayDC3;
import template.io.FastInput;
import template.io.FastOutput;
import template.string.SAIS;

import java.util.Arrays;

public class SuffixArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int)5e5];
        int n = in.rs(s);
        int[] sa = SuffixArrayDC3.suffixArray(new CharSequence() {
            @Override
            public int length() {
                return n;
            }

            @Override
            public char charAt(int index) {
                return s[index];
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                throw new UnsupportedOperationException();
            }
        });
        for(int i = 0; i < n; i++){
            out.append(sa[i]).append(' ');
        }
    }
}
