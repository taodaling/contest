package on2021_08.on2021_08_20_CS_Academy___Virtual_Beta_Round__2.Matrix_Coloring;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.rand.HashData;
import template.rand.RollingHash;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrixColoring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        int[][] mat2 = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.rc() == 'R' ? 1 : 0;
                mat2[j][i] = mat[i][j];
            }
        }

        if(!solveable(mat)){
            out.println(-1);
            return;
        }
        int ans1 = solve(mat);
        int ans2 = solve(mat2);
        int ans = Math.min(ans1, ans2);
        out.println(ans);
    }

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

    int L = (int) 1e4;
    IntegerDeque rdq = new IntegerDequeImpl(L);
    IntegerDeque cdq = new IntegerDequeImpl(L);
    HashData[] hds = HashData.doubleHashData(L);
    RollingHash rh = new RollingHash(hds[0], hds[1], L);

    public int solve(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        long[] rowHash = new long[n];
        for (int i = 0; i < n; i++) {
            rh.clear();
            for (int j = 0; j < m; j++) {
                rh.addLast(mat[i][j]);
            }
            long hash = rh.hash();
            rowHash[i] = hash;
        }
        Map<Long, List<Integer>> groupBy = IntStream.range(0, n).boxed()
                .collect(Collectors.groupingBy(i -> rowHash[i]));

        int best = n + m;
        for (List<Integer> list : groupBy.values()) {
            int k = list.size();
            int[][] temp = new int[k][];
            for (int i = 0; i < k; i++) {
                temp[i] = mat[list.get(i)];
            }
            if (solveable(temp)) {
                best = Math.min(m + n - k, best);
            }
        }
        return best;
    }
}
