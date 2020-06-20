package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.string.SAIS;
import template.utils.Debug;

public class BPolycarpsPhoneBook {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n * 10];
        for (int i = 0; i < n; i++) {
            in.readString(s, 10 * i);
            s[10 * (i + 1) - 1] = '#';
        }

        debug.debug("s", new String(s));
        SAIS sais = new SAIS(i -> s[i], 0, s.length - 1);
        int[] begin = new int[n];
        int[] len = new int[n];
        for (int i = 0; i < n; i++) {
            begin[i] = i * 10;
            len[i] = 9;
        }

        for (int i = 0; i < s.length; i++) {
            int index = sais.queryKth(i);
            int id = index / 10;
            if (index % 10 == 9) {
                continue;
            }
            int leftLcp = sais.longestCommonPrefixBetween(i);
            int l = i;
            while (l > 0 && sais.queryKth(l - 1) / 10 == id) {
                l--;
                leftLcp = Math.min(leftLcp, sais.longestCommonPrefixBetween(l));
            }
            if (l == 0) {
                leftLcp = 0;
            }

            int rightLcp = 10;
            int r = i;
            while (r + 1 < s.length && sais.queryKth(r + 1) / 10 == id) {
                r++;
                rightLcp = Math.min(rightLcp, sais.longestCommonPrefixBetween(r));
            }
            if (r == s.length - 1) {
                rightLcp = 0;
            } else {
                rightLcp = Math.min(rightLcp, sais.longestCommonPrefixBetween(r + 1));
            }

            int lcp = Math.max(leftLcp, rightLcp);
            int from = index;
            int to = index + lcp;
            if (to % 10 == 9 || to / 10 != id) {
                continue;
            }
            if (len[id] > to - from + 1 || len[id] == to - from + 1 && begin[id] > from) {
                len[id] = to - from + 1;
                begin[id] = from;
            }

        }


        debug.debug("begin", begin);
        debug.debug("len", len);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < len[i]; j++) {
                out.append(s[begin[i] + j]);
            }
            out.println();
        }
    }

    public int idOf(int i) {
        return i / 10;
    }
}
