package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;
import template.primitve.generated.IntegerSparseTable;

import java.util.Arrays;

public class GChattering {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] rs = new int[n];
        for (int i = 0; i < n; i++) {
            rs[i] = in.readInt();
        }

        if (n == 1) {
            out.println(0);
            return;
        }

        int[] leftTo = new int[3 * n];
        int[] rightTo = new int[3 * n];
        for (int i = 0; i < 3 * n; i++) {
            leftTo[i] = Math.max(0, i - rs[i % n]);
            rightTo[i] = Math.min(3 * n - 1, i + rs[i % n]);
        }

        int[] indexes = new int[3 * n];
        for (int i = 0; i < 3 * n; i++) {
            indexes[i] = i;
        }

        IntegerSparseTable leftST = new IntegerSparseTable(indexes, indexes.length, (a, b) -> leftTo[a] <= leftTo[b] ? a : b);
        IntegerSparseTable rightST = new IntegerSparseTable(indexes, indexes.length, (a, b) -> rightTo[a] >= rightTo[b] ? a : b);

        int[][] dpL = new int[20][3 * n];
        int[][] dpR = new int[20][3 * n];
        dpL[0] = leftTo;
        dpR[0] = rightTo;

        for (int i = 1; i < 20; i++) {
            for (int j = n; j < n * 2; j++) {
                int l = dpL[i - 1][j];
                int r = dpR[i - 1][j];
                int lIndex = leftST.query(l, r);
                int rIndex = rightST.query(l, r);
                l = Math.min(l, dpL[i - 1][lIndex]);
                l = Math.min(l, dpL[i - 1][rIndex]);
                r = Math.max(r, dpR[i - 1][lIndex]);
                r = Math.max(r, dpR[i - 1][rIndex]);
                dpL[i][j] = l;
                dpR[i][j] = r;
            }
            for (int j = 0; j < n; j++) {
                dpL[i][j] = Math.max(0, dpL[i][j + n] - n);
                dpR[i][j] = dpR[i][j + n] - n;
            }
            for (int j = 2 * n; j < 3 * n; j++) {
                dpL[i][j] = dpL[i][j - n] + n;
                dpR[i][j] = Math.min(3 * n - 1, dpR[i][j - n] + n);
            }
        }

        //System.err.println(Arrays.deepToString(dpL));
       // System.err.println(Arrays.deepToString(dpR));

        for(int i = n; i < 2 * n; i++){
            int mask = 0;
            int ll = i;
            int rr = i;
            for(int j = 20 - 1; j >= 0; j--){
                int l = ll;
                int r = rr;
                int lIndex = leftST.query(l, r);
                int rIndex = rightST.query(l, r);
                l = Math.min(l, dpL[j][lIndex]);
                l = Math.min(l, dpL[j][rIndex]);
                r = Math.max(r, dpR[j][lIndex]);
                r = Math.max(r, dpR[j][rIndex]);

                if(r - l + 1 < n){
                    mask = Bits.setBit(mask, j, true);
                    ll = l;
                    rr = r;
                }
            }

            out.println(mask + 1);
        }
    }
}
