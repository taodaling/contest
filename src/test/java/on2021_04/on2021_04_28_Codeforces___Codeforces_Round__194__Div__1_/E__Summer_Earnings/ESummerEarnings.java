package on2021_04.on2021_04_28_Codeforces___Codeforces_Round__194__Div__1_.E__Summer_Earnings;



import template.datastructure.BitSet;
import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;

public class ESummerEarnings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Pt[] pts = new Pt[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Pt(in.ri(), in.ri());
            pts[i].id = i;
        }
        BitSet[][] state = new BitSet[n][n];
        int[][] invIndex = new int[n][n];
        int[][] sortIndex = new int[n][n];
        Pt[] sortedPts = pts.clone();
        int step = 30;
        for (int i = 0; i < n; i++) {
            Pt pt = pts[i];
            for(Pt p : pts){
                p.d2 = IntegerPoint2.dist2(pt, p);
            }
            Comparator<Pt> cmp = Comparator.comparingLong(x -> x.d2);
            Arrays.sort(sortedPts, cmp);
            for (int j = 0; j < n; j++) {
                sortIndex[i][j] = sortedPts[j].id;
                invIndex[i][sortIndex[i][j]] = j;
            }
            for (int j = 0; j < n; j += step) {
                state[i][j] = new BitSet(n);
                if (j > 0) {
                    state[i][j].copy(state[i][j - step]);
                    for (int k = j - step; k < j; k++) {
                        state[i][j].set(sortIndex[i][k]);
                    }
                }
            }
        }

        long max2 = 0;
        BitSet tmp = new BitSet(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                tmp.fill(false);
                int ij = invIndex[i][j];
                int ijF = ij / step * step;
                int ji = invIndex[j][i];
                int jiF = ji / step * step;
                tmp.or(state[i][ijF]);
                tmp.or(state[j][jiF]);
                for (int k = ijF; k <= ij; k++) {
                    tmp.set(sortIndex[i][k]);
                }
                for (int k = jiF; k <= ji; k++) {
                    tmp.set(sortIndex[j][k]);
                }
                if (tmp.nextClearBit(0) < tmp.capacity()) {
                    max2 = Math.max(max2, IntegerPoint2.dist2(pts[i], pts[j]));
                }
            }
        }

        double ans = Math.sqrt(max2);
        out.println(ans / 2);
    }
}

class Pt extends IntegerPoint2 {
    public Pt(long x, long y) {
        super(x, y);
    }

    int id;
    long d2;
}