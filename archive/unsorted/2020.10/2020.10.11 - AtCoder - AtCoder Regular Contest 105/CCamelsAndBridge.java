package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.Arrays;

public class CCamelsAndBridge {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] w = new int[n];
        in.populate(w);
        int[][] bridges = new int[2][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                bridges[j][i] = in.readInt();
            }
            for (int x : w) {
                if (x > bridges[1][i]) {
                    out.print(-1);
                    return;
                }
            }
        }


        int[] d = new int[n];
        for (int i = 1; i < n; i++) {
            int finalI = i;
            IntBinarySearch ibs = new IntBinarySearch() {
                @Override
                public boolean check(int mid) {
                    for (int j = 0; j < m; j++) {
                        int len = bridges[0][j];
                        int cap = bridges[1][j];
                        d[finalI] = mid;
                        int sum = 0;
                        int r = -1;
                        int l = 0;
                        int lenSum = 0;
                        for (int k = 0; k <= finalI; k++) {
                            while (l < k) {
                                sum -= w[l];
                                lenSum -= w[l + 1];
                                l++;
                            }
                            while (r < l || r + 1 <= finalI && lenSum + d[r + 1] < len) {
                                r++;
                                lenSum += d[r];
                                sum += w[r];
                            }
                            if (sum > cap) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            };

            d[i] = ibs.binarySearch(0, (int) 1e8);
        }

        debug.debug("d", d);

        int ans = Arrays.stream(d).sum();
        out.println(ans);
    }
}
