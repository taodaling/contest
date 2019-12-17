package contest;


import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.Arrays;

public class G2PlaylistForPolycarpHardVersion {
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] musics = new int[n][2];
        int[] cnts = new int[4];
        for (int i = 0; i < n; i++) {
            musics[i][0] = in.readInt();
            musics[i][1] = in.readInt();
            cnts[musics[i][1]]++;
        }

        int c1 = cnts[1];
        int c2 = cnts[2];
        int c3 = cnts[3];

        int[][][][] comp = new int[c1 + 1][c2 + 1][c3 + 1][4];
        for (int i = 0; i <= c1; i++) {
            for (int j = 0; j <= c2; j++) {
                for (int k = 0; k <= c3; k++) {
                    for (int t = 0; t < 4; t++) {
                        if (i == 0 && j == 0 && k == 0) {
                            comp[i][j][k][t] = 1;
                            continue;
                        }
                        if (i > 0 && t != 1) {
                            comp[i][j][k][t] = mod.plus(comp[i][j][k][t], mod.mul(comp[i - 1][j][k][1], i));
                        }
                        if (j > 0 && t != 2) {
                            comp[i][j][k][t] = mod.plus(comp[i][j][k][t], mod.mul(comp[i][j - 1][k][2], j));
                        }
                        if (k > 0 && t != 3) {
                            comp[i][j][k][t] = mod.plus(comp[i][j][k][t], mod.mul(comp[i][j][k - 1][3], k));
                        }
                    }
                }
            }
        }

        int[][][][] last = new int[c1 + 1][c2 + 1][c3 + 1][m + 1];
        int[][][][] next = new int[c1 + 1][c2 + 1][c3 + 1][m + 1];

        last[0][0][0][0] = 1;
        int t1 = 0;
        int t2 = 0;
        int t3 = 0;
        for (int[] music : musics) {
            int m1 = music[1];
            int m0 = music[0];
            if (m1 == 1) {
                t1++;
            } else if (m1 == 2) {
                t2++;
            } else {
                t3++;
            }
            for (int i = 0; i <= t1; i++) {
                for (int j = 0; j <= t2; j++) {
                    for (int k = 0; k <= t3; k++) {
                        for (int t = 0; t <= m; t++) {
                            next[i][j][k][t] = last[i][j][k][t];
                            if (t < m0) {
                                continue;
                            }
                            if (m1 == 1 && i > 0) {
                                next[i][j][k][t] = mod.plus(next[i][j][k][t], last[i - 1][j][k][t - m0]);
                            } else if (m1 == 2 && j > 0) {
                                next[i][j][k][t] = mod.plus(next[i][j][k][t], last[i][j - 1][k][t - m0]);
                            } else if (m1 == 3 && k > 0) {
                                next[i][j][k][t] = mod.plus(next[i][j][k][t], last[i][j][k - 1][t - m0]);
                            }
                        }
                    }
                }
            }

            int[][][][] tmp = last;
            last = next;
            next = tmp;
        }

        int ans = 0;
        for (int i = 0; i <= c1; i++) {
            for (int j = 0; j <= c2; j++) {
                for (int k = 0; k <= c3; k++) {
                    ans = mod.plus(ans, mod.mul(last[i][j][k][m], comp[i][j][k][0]));
                }
            }
        }

        out.println(ans);
    }
}
