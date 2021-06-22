package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.F1__Falling_Sand__Easy_Version_;



import template.graph.DirectedEdge;
import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class F1FallingSandEasyVersion {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[][] mat = new char[n][m];
        int[][] id = new int[n][m];
        SequenceUtils.deepFill(id, -1);
        int idAlloc = 0;
        int[] col = new int[m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.rc();
                if (mat[i][j] == '#') {
                    id[i][j] = idAlloc++;
                    col[j]++;
                }
            }
        }

        int[] a = in.ri(m);
        int sum = Arrays.stream(a).sum();
        if (sum == 0) {
            out.println(0);
            return;
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

        scc = new DirectedTarjanSCC(idAlloc);
        scc.init(g);
        int[][] left = new int[n][m];
        int[][] right = new int[n][m];
        IntegerArrayList cand = new IntegerArrayList(n);
        for (int i = 0; i < m; i++) {
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                if (id[j][i] == -1) {
                    continue;
                }
                cand.clear();
                int l = j;
                int r = j;
                cand.add(l);
                while (r < n && next[r + 1][i] < n && scc.set[id[next[r + 1][i]][i]] == scc.set[id[j][i]]) {
                    r = next[r + 1][i];
                    cand.add(r);
                }

                j = r;
                int L = col[i] - cnt >= a[i] ? i : i + 1;
                if (L == i && i - 1 >= 0 && next[l][i - 1] < n) {
                    L = left[next[l][i - 1]][i - 1];
                } else {
                    while (L - 1 >= 0 && a[L - 1] == 0) {
                        L--;
                    }
                }
                for (int k = 0; k < cand.size(); k++) {
                    left[cand.get(k)][i] = L;
                }

                cnt += cand.size();
            }
        }

        for (int i = m - 1; i >= 0; i--) {
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                if (id[j][i] == -1) {
                    continue;
                }
                cand.clear();
                int l = j;
                int r = j;
                cand.add(l);
                while (r < n && next[r + 1][i] < n && scc.set[id[next[r + 1][i]][i]] == scc.set[id[j][i]]) {
                    r = next[r + 1][i];
                    cand.add(r);
                }
                j = r;
                int R = col[i] - cnt >= a[i] ? i : i - 1;
                if (R == i && i + 1 < m && next[l][i + 1] < n) {
                    R = right[next[l][i + 1]][i + 1];
                } else {
                    while (R + 1 < m && a[R + 1] == 0) {
                        R++;
                    }
                }
                for (int k = 0; k < cand.size(); k++) {
                    right[cand.get(k)][i] = R;
                }


                cnt += cand.size();
            }
        }

        List<int[]> intervals = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            int top = next[0][i];
            if (top >= n) {
                continue;
            }
            intervals.add(new int[]{left[top][i], right[top][i]});
        }

        intervals.sort(Comparator.comparingInt(x -> x[0]));
        int curMax = 0;
        int curNext = 0;
        int head = 0;
        while (head < intervals.size() && intervals.get(head)[0] <= curNext) {
            curMax = Math.max(curMax, intervals.get(head)[1]);
            head++;
        }
        int ans = 0;
        while (curNext < m) {
            ans++;
            curNext = curMax + 1;
            while (head < intervals.size() && intervals.get(head)[0] <= curNext) {
                curMax = Math.max(curMax, intervals.get(head)[1]);
                head++;
            }
        }

        out.println(ans);
    }

    DirectedTarjanSCC scc;
}


