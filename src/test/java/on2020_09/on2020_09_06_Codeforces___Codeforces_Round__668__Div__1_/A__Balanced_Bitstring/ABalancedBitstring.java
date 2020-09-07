package on2020_09.on2020_09_06_Codeforces___Codeforces_Round__668__Div__1_.A__Balanced_Bitstring;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ABalancedBitstring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        char[] s = new char[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.readChar();
        }

        String yes = "YES";
        String no = "NO";
        int[] val = new int[n];
        Arrays.fill(val, -1);
        int[] first = new int[n];
        for (int i = 0; i < n; i++) {
            if (i < k) {
                first[i] = i;
            } else {
                first[i] = first[i - k];
            }
            if (s[i] == '0') {
                if (val[first[i]] == 1) {
                    out.println(no);
                    return;
                }
                val[first[i]] = 0;
            } else if (s[i] == '1') {
                if (val[first[i]] == 0) {
                    out.println(no);
                    return;
                }
                val[first[i]] = 1;
            } else {

            }
        }

        int cnts = 0;
        for (int i = 0; i < k; i++) {
            if (val[i] != -1) {
                cnts += val[i];
            }
        }

        for (int i = 0; i < k; i++) {
            if (val[i] == -1) {
                if (cnts < k / 2) {
                    cnts++;
                    val[i] = 1;
                } else {
                    val[i] = 0;
                }
            }
        }

        if (cnts != k / 2) {
            out.println(no);
        } else {
            out.println(yes);
        }
    }
}
