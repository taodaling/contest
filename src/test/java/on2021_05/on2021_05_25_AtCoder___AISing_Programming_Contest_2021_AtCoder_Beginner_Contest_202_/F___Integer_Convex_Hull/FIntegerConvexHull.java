package on2021_05.on2021_05_25_AtCoder___AISing_Programming_Contest_2021_AtCoder_Beginner_Contest_202_.F___Integer_Convex_Hull;



import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class FIntegerConvexHull {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }

        long ans = 0;
        long[][][] dp = new long[2][n][n];
        for (int lb = 0; lb < n; lb++) {
            IntegerPoint2 lbPt = pts[lb];
            IntegerPoint2[] sorted = Arrays.stream(pts).filter(x -> IntegerPoint2.SORT_BY_XY.compare(lbPt, x) <= 0).toArray(m -> new IntegerPoint2[m]);
            int m = sorted.length;
            if(m <= 2){
                continue;
            }
            for (int i = 0; i < m; i++) {
                if (sorted[i] == lbPt) {
                    SequenceUtils.swap(sorted, 0, i);
                    break;
                }
            }
            Arrays.sort(sorted, 1, m, IntegerPoint2.sortByPolarAngleAround(sorted[0]));
            SequenceUtils.deepFill(dp, 0L);
            for (int i = 1; i < m; i++) {
                int sign = (int) (IntegerPoint2.cross(lbPt, sorted[i]) & 1);
                dp[sign][0][i] = 1;
            }

            debug.debug("lbPt", lbPt);
            debug.debug("sorted", sorted);
            for (int i = 1; i < m; i++) {
                for (int j = i + 1; j < m; j++) {
                    int sign = (int) (IntegerPoint2.cross(sorted[i], sorted[j]) & 1);
                    long choice = 1;
                    for (int k = i + 1; k < j; k++) {
                        if (IntegerPoint2.orient(sorted[i], sorted[j], sorted[k]) == 1) {
                            choice = choice * 2;
                            if (choice >= mod) {
                                choice -= mod;
                            }
                        }
                    }

                    for (int k = 0; k < i; k++) {
                        if (IntegerPoint2.orient(sorted[k], sorted[i], sorted[j]) == 1) {
                            for (int oe = 0; oe < 2; oe++) {
                                dp[oe ^ sign][i][j] += choice * dp[oe][k][i] % mod;
                            }
                        }
                    }

                    for (int k = 0; k < 2; k++) {
                        dp[k][i][j] %= mod;
                    }
                }
            }

            long contrib = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j < m; j++) {
                    for (int k = j + 1; k < m; k++) {
                        if (dp[i][j][k] == 0) {
                            continue;
                        }
                        int sign = (int) (IntegerPoint2.cross(lbPt, sorted[k]) & 1);
                        if ((i ^ sign) == 0) {
                            contrib += dp[i][j][k];
                        }
                    }
                }
            }

            contrib %= mod;
            debug.debug("dp", dp);
            debug.debug("contrib", contrib);
            ans += contrib;
        }

        ans %= mod;
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
