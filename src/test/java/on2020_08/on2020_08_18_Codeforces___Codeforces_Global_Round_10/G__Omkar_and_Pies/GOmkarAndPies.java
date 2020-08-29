package on2020_08.on2020_08_18_Codeforces___Codeforces_Global_Round_10.G__Omkar_and_Pies;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.stream.IntStream;

public class GOmkarAndPies {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        k = in.readInt();

        int src = 0;
        int target = 0;
        for (int i = 0; i < k; i++) {
            src |= (in.readChar() - '0') << i;
        }
        for (int i = 0; i < k; i++) {
            target |= (in.readChar() - '0') << i;
        }
        int[][] swap = new int[2][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                swap[j][i] = in.readInt() - 1;
            }
        }

        int[] invPerm = IntStream.range(0, k).toArray();
        int[] perm = invPerm.clone();

        int[] A = new int[1 << k];
        SequenceUtils.deepFill(A, -1);
        for (int i = 0; i < n; i++) {
            applyLast(perm, swap[0][i], swap[1][i]);
            //add(perm(perm, src), A, B, i + 1);

            //i - (t + 1) + 1 == m
            //t = i - m
            int t = i - m;
//            if (t == -1) {
//                add(perm(invPerm, target), B, A, t + 1);
//            } else
            if (t >= 0) {
                applyFirst(invPerm, swap[0][t], swap[1][t]);
                add(perm(invPerm, target), A, perm(perm, src), t + 1, i + 1);
            }
        }

        int ans = k - (Integer.bitCount(src) - max) - (Integer.bitCount(target) - max);
        out.println(ans);
        out.append(l + 1).append(' ').append(r).println();
    }

    int k;
    int max = -1;
    int l;
    int r;

    public void add(int x, int[] A, int B, int l, int r) {
        if (A[x] != -1) {
            return;
        }
        A[x] = l;
        int cand = Math.max(max, Integer.bitCount(x & B));
        if (cand > max) {
            max = cand;
            this.l = l;
            this.r = r;
        }
    }

    public int perm(int[] perm, int x) {
        int ans = 0;
        for (int i = 0; i < perm.length; i++) {
            ans |= Bits.get(x, perm[i]) << i;
        }
        return ans;
    }

    public void applyLast(int[] perm, int x, int y) {
        SequenceUtils.swap(perm, x, y);
    }

    public void applyFirst(int[] perm, int x, int y) {
        for (int i = 0; i < perm.length; i++) {
            if (perm[i] == x) {
                perm[i] = y;
            } else if (perm[i] == y) {
                perm[i] = x;
            }
        }
    }
}
