package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ETravellingThroughTheSnowQueensKingdom {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        int[][] edges = new int[2][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                edges[j][i] = in.readInt() - 1;
            }
        }
        int[][] mat = new int[n][n];
        int inf = m + 1;
        SequenceUtils.deepFill(mat, inf);

        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].l = in.readInt() - 1;
            qs[i].r = in.readInt() - 1;
            qs[i].s = in.readInt() - 1;
            qs[i].t = in.readInt() - 1;
        }

        Query[] sortedQs = qs.clone();
        Arrays.sort(sortedQs, (a, b) -> Integer.compare(a.l, b.l));
        SimplifiedDeque<Query> dq = new Range2DequeAdapter<>(i -> sortedQs[i], 0, q - 1);
        for (int i = m - 1; i >= 0; i--) {
            int u = edges[0][i];
            int v = edges[1][i];
            for (int j = 0; j < n; j++) {
                mat[u][j] = mat[v][j] = Math.min(mat[u][j], mat[v][j]);
            }
            mat[u][v] = mat[v][u] = i;
            while (!dq.isEmpty() && dq.peekLast().l == i) {
                Query query = dq.removeLast();
                query.ans = mat[query.s][query.t] <= query.r;
            }
        }

        for(Query query : qs){
            out.println(query.ans ? "Yes" : "No");
        }
    }
}

class Query {
    int l;
    int r;
    int s;
    int t;
    boolean ans;
}
