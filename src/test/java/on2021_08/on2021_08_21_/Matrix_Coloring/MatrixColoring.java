package on2021_08.on2021_08_21_.Matrix_Coloring;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.rand.HashSeed;
import template.string.FastHash;
import template.utils.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrixColoring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][][] mat = new int[2][][];
        mat[0] = new int[n][m];
        mat[1] = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[0][i][j] = in.rc() == 'R' ? 1 : 0;
                mat[1][j][i] = mat[0][i][j];
            }
        }

        if (!solveable(mat[0])) {
            out.println(-1);
            return;
        }
        int ans1 = consider(mat[0]);
        int ans2 = consider(mat[1]);
        int ans = Math.min(ans1, ans2);
        out.println(ans);
    }

    int L = (int) 1e4;
    IntegerDeque rdq = new IntegerDequeImpl(L);
    IntegerDeque cdq = new IntegerDequeImpl(L);


    public boolean solveable(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;

        int aliveN = n;
        int aliveM = m;
        boolean[] rowDel = new boolean[n];
        boolean[] colDel = new boolean[m];
        int[] rowSum = new int[n];
        int[] colSum = new int[m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rowSum[i] += mat[i][j];
                colSum[j] += mat[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            if (rowSum[i] == 0 || rowSum[i] == aliveM) {
                rowDel[i] = true;
                rdq.addLast(i);
            }
        }
        for (int i = 0; i < m; i++) {
            if (colSum[i] == 0 || colSum[i] == aliveN) {
                colDel[i] = true;
                cdq.addLast(i);
            }
        }
        while (!rdq.isEmpty() || !cdq.isEmpty()) {
            if (!rdq.isEmpty()) {
                int head = rdq.removeFirst();
                aliveN--;
                for (int i = 0; i < m; i++) {
                    colSum[i] -= mat[head][i];
                    if (!colDel[i] && (colSum[i] == 0 || colSum[i] == aliveN)) {
                        cdq.addLast(i);
                        colDel[i] = true;
                    }
                }
            } else {
                int head = cdq.removeFirst();
                aliveM--;
                for (int i = 0; i < n; i++) {
                    rowSum[i] -= mat[i][head];
                    if (!rowDel[i] && (rowSum[i] == 0 || rowSum[i] == aliveM)) {
                        rdq.addLast(i);
                        rowDel[i] = true;
                    }
                }
            }
        }

        return aliveN == 0 && aliveM == 0;
    }

    int[][] mat;
    int[] rset;
    int[] cset;
    boolean[] rvisited;
    boolean[] rinstk;
    boolean[] cvisited;
    boolean[] cinstk;
    boolean containCircle;
    int n;
    int m;

    public void dfsR(int root) {
        if (rvisited[root]) {
            containCircle = containCircle || rinstk[root];
            return;
        }
        rvisited[root] = true;
        rinstk[root] = true;
        for (int i = 0; i < m; i++) {
            if (mat[root][i] != rset[root]) {
                dfsC(i);
            }
        }
        rinstk[root] = false;
    }

    public void dfsC(int root) {
        if (cvisited[root]) {
            containCircle = containCircle || cinstk[root];
            return;
        }
        cvisited[root] = true;
        cinstk[root] = true;
        for (int i = 0; i < n; i++) {
            if (mat[i][root] != cset[root]) {
                dfsR(i);
            }
        }
        cinstk[root] = false;
    }

    public boolean check(int[] delRows) {
        Arrays.sort(delRows);
        int r = delRows[0];
        for (int j = 0; j < m; j++) {
            cset[j] = mat[r][j];
        }
        Arrays.fill(rset, -1);
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                if (mat[i][j] != cset[j]) {
                    if (rset[i] == -1) {
                        rset[i] = mat[i][j];
                    }
                    if (rset[i] != mat[i][j]) {
                        return false;
                    }
                }
            }
        }
        Arrays.fill(rinstk, false);
        Arrays.fill(cinstk, false);
        Arrays.fill(rvisited, false);
        Arrays.fill(cvisited, false);
        containCircle = false;
        for (int i = 0; i < m; i++) {
            dfsC(i);
        }
        return !containCircle;
    }

    public int consider(int[][] mat) {
        this.mat = mat;
        n = mat.length;
        m = mat[0].length;
        this.rset = new int[n];
        this.cset = new int[m];
        rinstk = new boolean[n];
        cinstk = new boolean[m];
        rvisited = new boolean[n];
        cvisited = new boolean[m];
        int mod = (int) 1e9 + 7;
        Pair<Integer, Integer> seeds = HashSeed.getSeed2();
        FastHash fh = new FastHash(mod, seeds.a, seeds.b);
        long[] hash = new long[n];
        for (int i = 0; i < n; i++) {
            hash[i] = fh.hash(mat[i]);
        }
        Map<Long, List<Integer>> groupBy = IntStream.range(0, n).boxed()
                .collect(Collectors.groupingBy(x -> hash[x]));
        int best = n + m;
        for (List<Integer> cand : groupBy.values()) {
            int[] data = cand.stream().mapToInt(Integer::intValue).toArray();
            if (check(data)) {
                best = Math.min(best, m + n - data.length);
            }
        }
        return best;
    }
}
