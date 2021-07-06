package template.graph;

import template.math.DigitUtils;
import template.math.ModMatrix;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * https://codeforces.com/blog/entry/92400
 */
public class RandomGeneralGraphMatching {
    static int mod = (int) 1e9 + 7;
    static Power pow = new Power(mod);
    boolean[][] adj;

    public RandomGeneralGraphMatching(int n) {
        this.n = n;
        adj = new boolean[n][n];
        mate = new int[n];
    }

    public void add(int u, int v) {
        if (u == v) {
            return;
        }
        adj[u][v] = adj[v][u] = true;
    }

    int n;
    long[][] tutte;
    int rank;

    private void randomize(long[][] tutte, int[] jump) {
        int n = tutte.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[jump[i]][jump[j]]) {
                    tutte[i][j] = RandomWrapper.INSTANCE.nextInt(1, mod - 1);
                    tutte[j][i] = DigitUtils.negate((int) tutte[i][j], mod);
                }
            }
        }
    }

    int[] mate;

    public int maxMatch() {
        tutte = new long[n][n];
        randomize(tutte, IntStream.range(0, n).toArray());

        for (int i = 0; i < n; i++) {
            tutte[i] = tutte[i].clone();
        }
        //find linear unrelated rows
        boolean[] added = new boolean[n];
        for (int i = 0; i < n; i++) {
            int find = -1;
            long inv = 0;
            for (int j = 0; j < n; j++) {
                if (tutte[j][i] != 0 && !added[j]) {
                    if (find == -1) {
                        find = j;
                        inv = pow.inverse((int) tutte[j][i]);
                        added[find] = true;
                    } else {
                        long mul = DigitUtils.mod(-inv * tutte[j][i], mod);
                        for (int k = 0; k < n; k++) {
                            tutte[j][k] = (tutte[j][k] + tutte[find][k] * mul) % mod;
                        }
                    }
                }
            }
        }
        IntegerArrayList rows = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            if (added[i]) {
                rows.add(i);
            }
        }
        rank = rows.size();
        int[] consider = rows.toArray();
        long[][] submat = new long[rank][rank];
        randomize(submat, consider);
        ModMatrix matrix = new ModMatrix((i, j) -> (int) submat[i][j], rank, rank);
        ModMatrix invMat = ModMatrix.inverse(matrix, pow);
        long[][] inverse = new long[rank][rank];
        for (int i = 0; i < rank; i++) {
            for (int j = 0; j < rank; j++) {
                inverse[i][j] = invMat.get(i, j);
            }
        }

        Arrays.fill(mate, -1);
        for (int i = 0; i < rank; i += 2) {
            int findJ = -1;
            int findK = -1;
            for (int j = i; j < n && findJ == -1; j++) {
                for (int k = i + 1; k < n && findK == -1; k++) {
                    if (adj[consider[j]][consider[k]] && inverse[j][k] != 0) {
                        //find
                        findJ = j;
                        findK = k;
                    }
                }
            }
//            swapRow(submat, i, i, findJ);
//            swapCol(submat, i, i, findJ);
            swapRow(inverse, i, i, findJ);
            swapCol(inverse, i, i, findJ);
            SequenceUtils.swap(consider, i, findJ);
//            swapRow(submat, i, i + 1, findK);
//            swapCol(submat, i, i + 1, findK);
            swapRow(inverse, i, i + 1, findK);
            swapCol(inverse, i, i + 1, findK);
            SequenceUtils.swap(consider, i + 1, findK);
            swapRow(inverse, i, i, i + 1);

            delete(inverse, i);
            delete(inverse, i + 1);
            mate[consider[i]] = consider[i + 1];
            mate[consider[i + 1]] = consider[i];
        }

        return rank / 2;
    }

    public int mate(int i) {
        return mate[i];
    }

    private void swapRow(long[][] A, int first, int a, int b) {
        if (a == b) {
            return;
        }
        long[] tmp = A[a];
        A[a] = A[b];
        A[b] = tmp;
    }


    private void swapCol(long[][] A, int first, int a, int b) {
        if (a == b) {
            return;
        }
        int n = A.length;
        for (int i = first; i < n; i++) {
            long tmp = A[i][a];
            A[i][a] = A[i][b];
            A[i][b] = tmp;
        }
    }

    private void delete(long[][] invA, int first) {
        long invA11 = pow.inverse((int) invA[first][first]);
        invA11 = DigitUtils.negate((int) invA11, mod);
        int n = invA.length;
        for (int i = first + 1; i < n; i++) {
            for (int j = first + 1; j < n; j++) {
                invA[i][j] = (invA[i][j] + invA[i][first] * invA[first][j] % mod * invA11) % mod;
            }
        }
    }
}
