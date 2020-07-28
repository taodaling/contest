package contest;

import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;
import template.primitve.generated.graph.IntegerISAP;
import template.primitve.generated.graph.IntegerMaximumFlow;

import java.util.ArrayList;
import java.util.List;

public class ParkPlace {
    int N;

    int idOf(int i, int j) {
        return i * N + j;
    }

    int idOfSrc() {
        return N * N;
    }

    int idOfDst() {
        return idOfSrc() + 1;
    }

    int rowOf(int i) {
        return i / N;
    }

    int colOf(int i) {
        return i % N;
    }

    List<IntegerFlowEdge>[] g;

    public String[] construct(int N, String[] place) {
        this.N = N;
        char[][] mat = new char[N][];

        g = IntegerFlow.createFlow(idOfDst() + 1);
        for (int i = 0; i < N; i++) {
            mat[i] = place[i].toCharArray();
        }
        int cnt1 = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mat[i][j] == '#' || (i + j) % 2 == 1) {
                    continue;
                }
                IntegerFlow.addEdge(g, idOfSrc(), idOf(i, j), 2);
                cnt1++;
            }
        }

        int cnt2 = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mat[i][j] == '#' || (i + j) % 2 == 0) {
                    continue;
                }
                IntegerFlow.addEdge(g, idOf(i, j), idOfDst(), 2);
                cnt2++;
            }
        }

        if(cnt1 != cnt2){
            return new String[0];
        }

        int[][] dir = new int[][]{
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mat[i][j] == '#' || (i + j) % 2 != 0) {
                    continue;
                }
                for (int[] d : dir) {
                    int x = i + d[0];
                    int y = j + d[1];
                    if (x < 0 || y < 0 || x >= N || y >= N) {
                        continue;
                    }
                    if (mat[x][y] == '#') {
                        continue;
                    }
                    IntegerFlow.addEdge(g, idOf(i, j), idOf(x, y), 1);
                }
            }
        }

        IntegerMaximumFlow mf = new IntegerISAP(g.length);
        int flow = mf.apply(g, idOfSrc(), idOfDst(), (int) 1e8);
        if (flow != cnt1 * 2) {
            return new String[0];
        }
        visited = new boolean[N * N];
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mat[i][j] == '#' || (i + j) % 2 == 1) {
                    continue;
                }
                if (visited[idOf(i, j)]) {
                    continue;
                }
                List<Integer> trace = new ArrayList<>();
                trace(idOf(i, j), -1, trace, 0);
                StringBuilder builder = new StringBuilder();
                builder.append(i).append(' ').append(j).append(' ');
                for (int k = 0; k < trace.size(); k++) {
                    int cur = trace.get(k);
                    int next = trace.get((k + 1) % trace.size());
                    if (rowOf(cur) < rowOf(next)) {
                        builder.append('S');
                    } else if (rowOf(cur) > rowOf(next)) {
                        builder.append('N');
                    } else if (colOf(cur) < colOf(next)) {
                        builder.append('E');
                    } else if (colOf(cur) > colOf(next)) {
                        builder.append('W');
                    } else {
                        throw new RuntimeException();
                    }
                }
                ans.add(builder.toString());
            }
        }

        return ans.toArray(new String[0]);
    }

    boolean[] visited;


    public void trace(int root, int p, List<Integer> trace, int tag) {
        if (visited[root]) {
            return;
        }
        trace.add(root);
        visited[root] = true;
        if (tag == 0) {
            for (IntegerFlowEdge e : g[root]) {
                if (e.to == idOfSrc() || e.to == p) {
                    continue;
                }
                if (e.flow == 1) {
                    trace(e.to, root, trace, tag ^ 1);
                    return;
                }
            }
        } else {
            for (IntegerFlowEdge e : g[root]) {
                if (e.to == idOfDst() || e.to == p) {
                    continue;
                }
                if (e.rev.flow == 1) {
                    trace(e.to, root, trace, tag ^ 1);
                    return;
                }
            }
        }
        throw new RuntimeException();
    }
}
