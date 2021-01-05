package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongPreSum;
import template.string.AhoCorasick;

import java.util.Arrays;
import java.util.List;

public class FDuffIsMad {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        AhoCorasick ac = new AhoCorasick('a', 'z', (int) 1e5);
        char[] buf = new char[(int) 1e5];
        int[] end = new int[n];
        char[][] s = new char[n][];
        for (int i = 0; i < n; i++) {
            int m = in.rs(buf, 0);
            s[i] = Arrays.copyOf(buf, m);
            ac.prepareBuild();
            for (char c : s[i]) {
                ac.build(c);
            }
            end[i] = ac.buildLast;
        }
        ac.endBuild();
        int m = ac.size();
        g = Graph.createGraph(m);
        occur = new long[m];
        enter = new int[m];
        leave = new int[m];
        for (int i = 1; i < m; i++) {
            int p = ac.fails[i];
            g[p].add(i);
        }
        dfs(0);

        Query[] pos = new Query[q];
        Query[] neg = new Query[q];
        List<Query>[] reg = Graph.createGraph(n);
        for (int i = 0; i < q; i++) {
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            int k = in.ri() - 1;
            pos[i] = new Query(r, k);
            neg[i] = new Query(l - 1, k);
            reg[pos[i].first].add(pos[i]);
            if (neg[i].first >= 0) {
                reg[neg[i].first].add(neg[i]);
            }
        }

        int BIT = 9;
        int B = 1 << BIT;
        int mask = B - 1;
        int N = (m - 1) / B + 1;
        blockTag = new int[N];
        elementTag = new int[N * B];
        LongPreSum ps = new LongPreSum(n);
        for (int i = 0; i < n; i++) {
            if (s[i].length < B) {
                continue;
            }
            Arrays.fill(occur, 0);
            ac.prepareMatch();
            for (char c : s[i]) {
                ac.match(c);
                occur[ac.matchLast]++;
            }
            collect(0);
            ps.populate(j -> occur[end[j]], n);
            for (Query query : pos) {
                if (query.k == i) {
                    query.ans = ps.prefix(query.first);
                }
            }
            for (Query query : neg) {
                if (query.k == i) {
                    query.ans = ps.prefix(query.first);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            int l = enter[end[i]];
            int r = leave[end[i]];
            for (int j = 0; j < N; j++) {
                int ll = j * B;
                int rr = ll + B - 1;
                if (r < ll || l > rr) {
                    continue;
                }
                if (l <= ll && r >= rr) {
                    blockTag[j]++;
                    continue;
                }
                ll = Math.max(l, ll);
                rr = Math.min(r, rr);
                for (int k = ll; k <= rr; k++) {
                    elementTag[k]++;
                }
            }
            for (Query query : reg[i]) {
                if(s[query.k].length >= B){
                    continue;
                }
                ac.prepareMatch();
                for(char c : s[query.k]){
                    ac.match(c);
                    int id = enter[ac.matchLast];
                    query.ans += elementTag[id] + blockTag[id >> BIT];
                }
            }
        }

        for(int i = 0; i < q; i++){
            long ans = pos[i].ans - neg[i].ans;
            out.println(ans);
        }
    }

    int[] blockTag;
    int[] elementTag;

    int[] enter;
    int[] leave;
    int indicator;
    List<Integer>[] g;
    long[] occur;

    public void dfs(int root) {
        enter[root] = indicator++;
        for (int node : g[root]) {
            dfs(node);
        }
        leave[root] = indicator - 1;
    }

    public void collect(int root) {
        for (int node : g[root]) {
            collect(node);
            occur[root] += occur[node];
        }
    }
}

class Query {
    int first;
    int k;

    long ans;

    public Query(int first, int k) {
        this.first = first;
        this.k = k;
    }
}


