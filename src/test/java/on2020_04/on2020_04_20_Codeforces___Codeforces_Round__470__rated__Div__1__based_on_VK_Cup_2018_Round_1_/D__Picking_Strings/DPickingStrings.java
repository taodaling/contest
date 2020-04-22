package on2020_04.on2020_04_20_Codeforces___Codeforces_Round__470__rated__Div__1__based_on_VK_Cup_2018_Round_1_.D__Picking_Strings;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

import java.util.Arrays;

public class DPickingStrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int limit = (int) 1e5;
        char[] S = new char[limit];
        char[] T = new char[limit];

        int n = in.readString(S, 0);
        int m = in.readString(T, 0);
        int[] a = new int[n];
        int[] b = new int[m];
        int[] aLeft = new int[n];
        int[] bLeft = new int[m];
        for (int i = 0; i < n; i++) {
            a[i] = S[i] == 'A' ? 0 : 1;
            aLeft[i] = i == 0 ? -1 : aLeft[i - 1];
            if (a[i] == 1) {
                aLeft[i] = i;
            }
        }
        for (int i = 0; i < m; i++) {
            b[i] = T[i] == 'A' ? 0 : 1;
            bLeft[i] = i == 0 ? -1 : bLeft[i - 1];
            if (b[i] == 1) {
                bLeft[i] = i;
            }
        }

        IntegerPreSum psA = new IntegerPreSum(a);
        IntegerPreSum psB = new IntegerPreSum(b);

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int l1 = in.readInt() - 1;
            int r1 = in.readInt() - 1;
            int l2 = in.readInt() - 1;
            int r2 = in.readInt() - 1;

            int c1 = psA.intervalSum(l1, r1);
            int c2 = psB.intervalSum(l2, r2);
            if (c2 < c1 || (c2 - c1) % 2 == 1) {
                out.append(0);
                continue;
            }
            int len1 = r1 - Math.max(l1 - 1, aLeft[r1]);
            int len2 = r2 - Math.max(l2 - 1, bLeft[r2]);
            if (c2 == c1) {
                if (len2 > len1 || (len1 - len2) % 3 != 0) {
                    out.append(0);
                } else {
                    out.append(1);
                }
                continue;
            }
            if (c2 > 0 && c1 == 0) {
                len2++;
            }
            if (len2 > len1) {
                out.append(0);
            } else {
                out.append(1);
            }
        }
    }
}
