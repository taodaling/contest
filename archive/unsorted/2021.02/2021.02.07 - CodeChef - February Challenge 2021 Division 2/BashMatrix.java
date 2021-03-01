package contest;

import template.graph.DirectedMST;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.problem.DirectedGraphMaximumWeightedHamiltonCirclePartition;
import template.problem.GridHamiltonCirclePartition;
import template.utils.Debug;
import template.utils.GridUtils;
import template.utils.SequenceUtils;

import java.util.*;

public class BashMatrix {
    int n;

    boolean valid(int i, int j) {
        return i >= 0 && j >= 0 && i < n && j < n;
    }

    int[][][] mat;

    boolean equal(int i, int j, int a, int b) {
        return mat[i][j][0] == mat[a][b][0] && mat[i][j][1] == mat[a][b][1];
    }

    char getDir(int i, int j, int x, int y) {
        if (i < x) {
            return 'D';
        } else if (i > x) {
            return 'U';
        } else if (j < y) {
            return 'R';
        } else {
            return 'L';
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        mat = new int[n][n][2];
        int pu = in.ri();
        int pl = in.ri();
        int pd = in.ri();
        int pr = in.ri();
        int[][] dirs = new int[][]{
                {-1, 0, pu},
                {0, -1, pl},
                {1, 0, pd},
                {0, 1, pr}
        };

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < 2; k++) {
                    mat[i][j][k] = in.ri() - 1;
                }
            }
        }
        int[][] grid = new int[n][n];
        List<int[]> onCircle = new ArrayList<>(n * n);
        SequenceUtils.deepFill(grid, -1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j][0] == i && mat[i][j][1] == j) {
                    grid[i][j] = onCircle.size();
                    onCircle.add(new int[]{i, j});
                }
            }
        }
        if (onCircle.size() == 0) {
            out.println(-1);
            return;
        }
        DirectedGraphMaximumWeightedHamiltonCirclePartition partition = new DirectedGraphMaximumWeightedHamiltonCirclePartition(onCircle.size());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == -1) {
                    continue;
                }
                for (int[] d : dirs) {
                    int ni = i + d[0];
                    int nj = j + d[1];
                    if (!valid(ni, nj) || grid[ni][nj] == -1) {
                        continue;
                    }
                    partition.addEdge(grid[i][j], grid[ni][nj], -d[2]);
                }
            }
        }
        if (!partition.solve()) {
            out.println(-1);
            return;
        }
        Map<Long, List<int[]>> map = new HashMap<>(onCircle.size());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                long key = DigitUtils.asLong(mat[i][j][0], mat[i][j][1]);
                map.computeIfAbsent(key, (x) -> new ArrayList<>()).add(new int[]{i, j});
            }
        }
        int[][] ids = new int[n][n];
        int[][] revDirs = new int[][]{
                {-1, 0, pd},
                {0, -1, pr},
                {1, 0, pu},
                {0, 1, pl}
        };


        char[][] ans = new char[n][n];
        IntegerArrayList dismantleRes = new IntegerArrayList(n * n);
        for (List<int[]> part : map.values()) {
            int idIndicator = 0;
            int root = -1;
            for (int[] pos : part) {
                ids[pos[0]][pos[1]] = idIndicator++;
                if (grid[pos[0]][pos[1]] != -1) {
                    root = ids[pos[0]][pos[1]];
                }
            }

            DirectedMST mst = new DirectedMST(idIndicator);
            for (int[] pos : part) {
                for (int[] d : revDirs) {
                    int ni = pos[0] + d[0];
                    int nj = pos[1] + d[1];
                    if (!valid(ni, nj) || !equal(pos[0], pos[1], ni, nj)) {
                        continue;
                    }
                    mst.addEdge(((pos[0] * 100 + pos[1]) * 100 + ni) * 100 + nj, ids[pos[0]][pos[1]], ids[ni][nj], d[2]);
                }
            }
            mst.contract();
            dismantleRes.clear();
            mst.dismantle(root, dismantleRes);
            for (int e : dismantleRes.toArray()) {
                if (e == -1) {
                    out.println(-1);
                    return;
                }
                int nj = e % 100;
                e /= 100;
                int ni = e % 100;
                e /= 100;
                int pos1 = e % 100;
                e /= 100;
                int pos0 = e;
                ans[ni][nj] = getDir(ni, nj, pos0, pos1);
            }
        }
        Debug debug = new Debug(true);
        for(int i = 0; i < onCircle.size(); i++){
            debug.debug("i", i);
            debug.debug("part", partition.next(i));
        }
        for (int[] pos : onCircle) {
            int to = partition.next(grid[pos[0]][pos[1]]);
            ans[pos[0]][pos[1]] = getDir(pos[0], pos[1], onCircle.get(to)[0], onCircle.get(to)[1]);
        }
        long cost = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (ans[i][j] == 'U') {
                    cost += pu;
                } else if (ans[i][j] == 'D') {
                    cost += pd;
                } else if (ans[i][j] == 'L') {
                    cost += pl;
                } else if (ans[i][j] == 'R') {
                    cost += pr;
                }
            }
        }
        out.println(cost);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(ans[i][j]);
            }
            out.println();
        }
    }
}
