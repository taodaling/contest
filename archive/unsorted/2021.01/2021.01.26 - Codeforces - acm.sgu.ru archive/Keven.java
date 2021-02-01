package contest;

import strings.SuffixArrayDC3;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class Keven {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        String s = in.rs();
        s += s;
        char[] ch = s.toCharArray();
        int n = ch.length / 2;
        boolean[] satisfied = new boolean[s.length()];
        for (int len = n / 2; len >= 1; len--) {
            int differ = 0;
            for (int i = 0; i < len; i++) {
                if (ch[i] != ch[i + len]) {
                    differ++;
                }
            }
            if (differ <= k) {
                satisfied[0] = true;
            }
            if (ch[0] != ch[len]) {
                differ--;
            }
            for (int i = 1; i + len + len <= ch.length; i++) {
                if (ch[i + len - 1] != ch[i + len + len - 1]) {
                    differ++;
                }
                if (differ <= k) {
                    satisfied[i] = true;
                }
                if (ch[i] != ch[i + len]) {
                    differ--;
                }
            }
            int cnt = 0;
            for (boolean b : satisfied) {
                if (b) {
                    cnt++;
                }
            }
            if (cnt == 0) {
                continue;
            }
            int[] sa = SuffixArrayDC3.suffixArray(s);
            for (int i = 0; i < sa.length; i++) {
                if (!satisfied[sa[i]]) {
                    continue;
                }
                out.println(s.substring(sa[i], sa[i] + len + len));
                return;
            }
        }
        out.println();
    }
}
