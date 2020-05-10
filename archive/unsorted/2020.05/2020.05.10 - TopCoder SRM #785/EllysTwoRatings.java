package contest;

import template.primitve.generated.datastructure.DoublePreSum;
import template.utils.ArrayIndex;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class EllysTwoRatings {
    int limit = 1000;

    ArrayIndex ai = new ArrayIndex(1001, 1001);
    double[] lastProb = new double[ai.totalSize()];

    public double getChance(int N, int A, int B) {
        lastProb[ai.indexOf(A, B)] = 1;
        int[][] lr = new int[limit + 1][2];
        for (int i = 1; i <= limit; i++) {
            int l = Math.max(1, i - 100);
            int r = Math.min(1000, i + 100);
            lr[i][0] = l;
            lr[i][1] = r;
        }

        double[] dps = new double[limit + 1];
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= limit; j++) {
                int finalJ = j;
                for (int k = 1; k <= limit; k++) {
                    int l = lr[k][0];
                    int r = lr[k][1];
                    lastProb[ai.indexOf(k, j)] /= (r - l + 1);
                }
                for (int index = 1; index <= limit; index++) {
                    dps[index] = dps[index - 1] + lastProb[ai.indexOf(index, finalJ)];
                }
                for (int k = 1; k <= limit; k++) {
                    int l = lr[k][0];
                    int r = lr[k][1];
                    lastProb[ai.indexOf(k, j)] = dps[r] - dps[l - 1];
                }
            }

            for (int j = 1; j <= limit; j++) {
                lastProb[ai.indexOf(j, j)] = 0;
            }


            for (int j = 1; j <= limit; j++) {
                int finalJ = j;
                for (int k = 1; k <= limit; k++) {
                    int l = lr[k][0];
                    int r = lr[k][1];
                    lastProb[ai.indexOf(j, k)] /= (r - l + 1);
                }

                for (int index = 1; index <= limit; index++) {
                    dps[index] = dps[index - 1] + lastProb[ai.indexOf(finalJ, index)];
                }
                for (int k = 1; k <= limit; k++) {
                    int l = lr[k][0];
                    int r = lr[k][1];
                    lastProb[ai.indexOf(j, k)] = dps[r] - dps[l - 1];
                }
            }

            for (int j = 1; j <= limit; j++) {
                lastProb[ai.indexOf(j, j)] = 0;
            }

        }

        double sum = 1;
        for (int j = 1; j <= limit; j++) {
            for (int k = 1; k <= limit; k++) {
                sum -= lastProb[ai.indexOf(j, k)];
            }
        }

        return sum;
    }
}
