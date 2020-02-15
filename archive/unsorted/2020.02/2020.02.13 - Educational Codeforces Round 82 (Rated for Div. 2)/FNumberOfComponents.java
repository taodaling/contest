package contest;

import template.datastructure.Range2DequeAdapter;
import template.graph.ConnectionChecker;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;

import java.util.ArrayList;
import java.util.List;

public class FNumberOfComponents {
    int n;
    int m;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        int q = in.readInt();
        int[][] mat = new int[n][m];
        Edge[][] right = new Edge[n][m];
        Edge[][] bot = new Edge[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                right[i][j] = new Edge(0, cell(i, j), cell(i, j + 1));
                bot[i][j] = new Edge(0, cell(i, j), cell(i + 1, j));
            }
        }

        List<Edge> edges = new ArrayList<>(2 * n * m + q * 4);
        int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        for (int i = 1; i <= q; i++) {
            int x = in.readInt() - 1;
            int y = in.readInt() - 1;
            int c = in.readInt();
            if (c == mat[x][y]) {
                continue;
            }
            int dieAt = i * 4;
            for (int[] dir : dirs) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                    continue;
                }
                if (mat[nx][ny] == mat[x][y]) {
                    if (ny > y && right[x][y] != null) {
                        right[x][y].die = dieAt++;
                        edges.add(right[x][y]);
                        right[x][y] = null;
                    }
                    if (ny < y && right[x][ny] != null) {
                        right[x][ny].die = dieAt++;
                        edges.add(right[x][ny]);
                        right[x][ny] = null;
                    }
                    if (nx < x && bot[nx][y] != null) {
                        bot[nx][y].die = dieAt++;
                        edges.add(bot[nx][y]);
                        bot[nx][y] = null;
                    }
                    if (nx > x && bot[x][y] != null) {
                        bot[x][y].die = dieAt++;
                        edges.add(bot[x][y]);
                        bot[x][y] = null;
                    }
                }
                if (mat[nx][ny] == c) {
                    if (ny > y) {
                        right[x][y] = new Edge(i * 4, cell(x, y), cell(nx, ny));
                    }
                    if (ny < y) {
                        right[x][ny] = new Edge(i * 4, cell(x, y), cell(nx, ny));
                    }
                    if (nx < x) {
                        bot[nx][y] = new Edge(i * 4, cell(x, y), cell(nx, ny));
                    }
                    if (nx > x) {
                        bot[x][y] = new Edge(i * 4, cell(x, y), cell(nx, ny));
                    }
                }
            }

            mat[x][y] = c;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m - 1; j++) {
                if (right[i][j] == null) {
                    continue;
                }
                right[i][j].die = 4 * (q + 1);
                edges.add(right[i][j]);
            }
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m; j++) {
                if (bot[i][j] == null) {
                    continue;
                }
                bot[i][j].die = 4 * (q + 1);
                edges.add(bot[i][j]);
            }
        }

        int cc = n * m;
        Edge[] edgeSortByBirth = edges.toArray(new Edge[0]);
        int[] edgeSortByBirthIndex = new int[edgeSortByBirth.length];
        for (int i = 0; i < edgeSortByBirthIndex.length; i++) {
            edgeSortByBirthIndex[i] = i;
        }
        int[] edgesOrderByDieIndex = edgeSortByBirthIndex.clone();
        CompareUtils.radixSort(edgeSortByBirthIndex, 0, edgeSortByBirthIndex.length - 1, i -> edgeSortByBirth[i].birth);
        CompareUtils.radixSort(edgesOrderByDieIndex, 0, edgesOrderByDieIndex.length - 1, i -> edgeSortByBirth[i].die);


        Range2DequeAdapter<Edge> d1 = new Range2DequeAdapter<>(i -> edgeSortByBirth[edgeSortByBirthIndex[i]], 0, edgeSortByBirthIndex.length - 1);
        Range2DequeAdapter<Edge> d2 = new Range2DequeAdapter<>(i -> edgeSortByBirth[edgesOrderByDieIndex[i]], 0, edgeSortByBirthIndex.length - 1);
        ConnectionChecker checker = new ConnectionChecker(n * m);
        for (int i = 1; i <= q; i++) {
            int end = (i + 1) * 4 - 1;
            while (!d1.isEmpty() && d1.peekFirst().birth <= end) {
                Edge e = d1.removeFirst();
                if (!checker.check(e.a, e.b)) {
                    cc--;
                }
                checker.addEdge(e.a, e.b, e.die);
            }
            while (!d2.isEmpty() && d2.peekFirst().die <= end) {
                Edge e = d2.removeFirst();
                checker.elapse(e.die);
                if (!checker.check(e.a, e.b)) {
                    cc++;
                }
            }
            checker.elapse(end);
            out.println(cc);
        }
    }

    public int cell(int i, int j) {
        return i * m + j;
    }
}

class Edge {
    int birth;
    int die;
    int a;
    int b;

    public Edge(int birth, int a, int b) {
        this.birth = birth;
        this.a = a;
        this.b = b;
    }
}