package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.F1__Falling_Sand__Easy_Version_;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.DirectedEdge;
import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class F1FallingSandEasyVersionTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 3);
        int m = random.nextInt(1, 3);
        char[][] mat = new char[n][m];
        int[] cnt = new int[m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = random.range('#', '.');
                if (mat[i][j] == '#') {
                    cnt[j]++;
                }
            }
        }
        int[] a = new int[m];
        for (int i = 0; i < m; i++) {
            a[i] = random.nextInt(0, cnt[i]);
        }
        int ans = solve(n, m, mat, a);

        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (char[] row : mat) {
            for (char c : row) {
                in.append(c);
            }
            printLine(in);
        }
        printLine(in, a);

        return new Test(in.toString(), "" + ans);
    }

    public int solve(int n, int m, char[][] mat, int[] a) {
        int[][] id = new int[n][m];
        SequenceUtils.deepFill(id, -1);
        int idAlloc = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == '#') {
                    id[i][j] = idAlloc++;
                }
            }
        }
        int[][] next = new int[n + 1][m];
        for (int i = 0; i < m; i++) {
            next[n][i] = n;
            for (int j = n - 1; j >= 0; j--) {
                next[j][i] = next[j + 1][i];
                if (id[j][i] != -1) {
                    next[j][i] = j;
                }
            }
        }
        List<DirectedEdge>[] g = Graph.createGraph(idAlloc);
        int[][] dirs = new int[][]{
                {0, 1},
                {0, -1},
                {1, 0},
                {-1, 0}
        };
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (id[i][j] == -1) {
                    continue;
                }
                for (int[] d : dirs) {
                    int ni = i + d[0];
                    int nj = j + d[1];
                    if (ni < 0 || ni >= n || nj < 0 || nj >= m) {
                        continue;
                    }
                    if (id[ni][nj] == -1) {
                        continue;
                    }
                    Graph.addEdge(g, id[i][j], id[ni][nj]);
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (id[j][i] == -1) {
                    continue;
                }
                for (int t = i - 1; t <= i + 1; t++) {
                    if (t < 0 || t >= m) {
                        continue;
                    }
                    if (next[j + 1][t] < n) {
                        Graph.addEdge(g, id[j][i], id[next[j + 1][t]][t]);
                    }
                }
            }
        }

        DirectedTarjanSCC scc = new DirectedTarjanSCC(idAlloc);
        scc.init(g);

        boolean[] consider = new boolean[idAlloc];
        for (int i = 0; i < m; i++) {
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                if (id[j][i] == -1) {
                    continue;
                }
                cnt++;
                if (cnt <= a[i]) {
                    consider[scc.set[id[j][i]]] = true;
                }
            }
        }
        boolean[] indeg = new boolean[idAlloc];
        for (int i = 0; i < idAlloc; i++) {
            if (!consider[scc.set[i]]) {
                continue;
            }
            for (DirectedEdge e : g[i]) {
                if (scc.set[i] == scc.set[e.to]) {
                    continue;
                }
                indeg[scc.set[e.to]] = true;
            }
        }
        int ans = 0;
        for (int i = 0; i < idAlloc; i++) {
            if (scc.set[i] != i) {
                continue;
            }
            if (!consider[i]) {
                continue;
            }
            if (!indeg[i]) {
                ans++;
            }
        }
        return ans;
    }
}
