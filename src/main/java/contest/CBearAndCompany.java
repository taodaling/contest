package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.SequenceUtils;

public class CBearAndCompany {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = in.readString().toCharArray();
        int V = 0;
        int K = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == 'V') {
                V++;
            }
            if (s[i] == 'K') {
                K++;
            }
        }

        int[][][][] prev = new int[V + 1][K + 1][K + 1][V + 1];
        int[][][][] next = new int[V + 1][K + 1][K + 1][V + 1];

        int[] lastNoWord = new int[n];
        int[] nextNoWord = new int[n];
        for (int i = 0; i < n; i++) {
            lastNoWord[i] = -1;
            if (s[i] != 'V' && s[i] != 'K') {
                lastNoWord[i] = i;
            } else if (i > 0) {
                lastNoWord[i] = lastNoWord[i - 1];
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            nextNoWord[i] = -1;
            if (s[i] != 'V' && s[i] != 'K') {
                nextNoWord[i] = i;
            } else if (i + 1 < n) {
                nextNoWord[i] = nextNoWord[i + 1];
            }
        }

        int inf = (int) 1e9;
        SequenceUtils.deepFill(prev, inf);
        prev[0][0][0][0] = 0;
        for (int i = 0; i < n; i++) {
            SequenceUtils.deepFill(next, inf);
            for (int j = 0; j <= V; j++) {
                for (int k = 0; k <= K; k++) {
                    for (int t = 0; t <= K; t++) {
                        for (int a = 0; a <= V; a++) {
                            if (s[i] == 'V') {
                                //do nothing
                                if (j + 1 <= V) {
                                    next[j + 1][k][t][a] = Math.min(next[j + 1][k][t][a], prev[j][k][t][a] + t + a);
                                }
                                //move first
                                if (lastNoWord[i] != -1) {
                                    next[j][k][t][a] = Math.min(next[j][k][t][a], prev[j][k][t][a] + j + k + t + a + 1);
                                }
                                //add to
                                if (a + 1 <= V) {
                                    next[j][k][t][a + 1] = Math.min(next[j][k][t][a + 1], prev[j][k][t][a]);
                                }
                            } else if (s[i] == 'K') {
                                //move before all V
                                if (k + 1 <= K) {
                                    next[j][k + 1][t][a] = Math.min(next[j][k + 1][t][a], prev[j][k][t][a] + j + a + t);
                                }
                                //move after
                                if (t + 1 <= K) {
                                    next[j][k][t + 1][a] = Math.min(next[j][k][t + 1][a], prev[j][k][t][a] + a);
                                }
                                //move before
                                if(t + 1 <= k){

                                }
                            } else {
                                next[a][t][0][0] = Math.min(next[a][t][0][0], prev[j][k][t][a] + t + a);
                            }
                        }
                    }
                }
            }

            int[][][][] tmp = next;
            next = prev;
            prev = tmp;
        }

        int ans = inf;
        for (int i = 0; i <= V; i++) {
            for (int j = 0; j <= K; j++) {
                ans = Math.min(ans, prev[i][j][0][0]);
            }
        }

        out.println(ans);
    }
}
