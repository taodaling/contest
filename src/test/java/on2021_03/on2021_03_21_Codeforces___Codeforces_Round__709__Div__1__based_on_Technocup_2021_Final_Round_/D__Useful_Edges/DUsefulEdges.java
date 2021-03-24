package on2021_03.on2021_03_21_Codeforces___Codeforces_Round__709__Div__1__based_on_Technocup_2021_Final_Round_.D__Useful_Edges;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DUsefulEdges {
    long inf = (long) 1e18;
    long[][] dist;
    int[][] edges;
    int[] largest;
    int n;
    int to;

    public void updateLargest(int a) {
        int largestIndex = -1;
        for (int b = 0; b < n; b++) {
            if (edges[a][b] == -1) {
                continue;
            }
            if (largestIndex == -1 || edges[a][largestIndex] + dist[largestIndex][to] > edges[a][b] + dist[b][to]) {
                largestIndex = b;
            }
        }
        largest[a] = largestIndex;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int m = in.ri();
        dist = new long[n][n];
        SequenceUtils.deepFill(dist, inf);
        for(int i = 0; i < n; i++){
            dist[i][i] = 0;
        }
        edges = new int[n][n];
        SequenceUtils.deepFill(edges, -1);
        List<int[]> allEdges = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            int d = in.ri();
            edges[u][v] = d;
            edges[v][u] = d;
            dist[u][v] = d;
            dist[v][u] = d;
            allEdges.add(new int[]{v, u});
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }

        int q = in.ri();
        List<int[]> qs = new ArrayList<>(q);
        for (int i = 0; i < q; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            int l = in.ri();
            qs.add(new int[]{u, v, l});
        }
        Map<Integer, List<int[]>> groupBy = qs.stream().collect(Collectors.groupingBy(x -> x[1]));
        largest = new int[n];
        for (List<int[]> list : groupBy.values()) {
            to = list.get(0)[1];
            for (int a = 0; a < n; a++) {
                updateLargest(a);
            }

            for (int[] query : list) {
                int u = query[0];
                int l = query[2];
                for (int a = 0; a < n; a++) {
                    while (largest[a] != -1 && dist[u][a] + edges[a][largest[a]] + dist[largest[a]][to] <= l) {
                        edges[a][largest[a]] = -1;
                        updateLargest(a);
                    }
                }
            }
        }

        int ans = 0;
        for (int[] e : allEdges) {
            int u = e[0];
            int v = e[1];
            if (edges[u][v] == -1 || edges[v][u] == -1) {
                ans++;
            }
        }
        out.println(ans);
    }
}

class Edge {
    int a;
    int b;
    int w;
}