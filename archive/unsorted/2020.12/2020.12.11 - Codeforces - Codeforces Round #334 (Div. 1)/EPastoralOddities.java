package contest;

import template.graph.DynamicConnectiveChecker;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.*;

public class EPastoralOddities {
    public long edgeId(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int inf = (int) 2e9;
        int best = inf;
        int n = in.ri();
        int m = in.ri();
        DynamicConnectiveChecker dcc = new DynamicConnectiveChecker(n, m);
        int oddCnt = n;
        TreeSet<Edge> pq = new TreeSet<>((a, b) -> {
            int ans = Integer.compare(a.l, b.l);
            if (ans == 0) {
                ans = Long.compare(a.sig, b.sig);
            }
            return ans;
        });
        Map<Long, Edge> edgeMap = new HashMap<>(m);
        for (int i = 0; i < m; i++) {
            assert edgeMap.size() == pq.size();
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int l = in.ri();
            long sig = edgeId(a, b);
            if (edgeMap.containsKey(sig)) {
                Edge e = edgeMap.get(sig);
                pq.remove(e);
                e.l = Math.min(e.l, l);
                pq.add(e);
                out.println(best == inf ? -1 : best);
                continue;
            }
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.l = l;
            e.sig = sig;
            if (l >= best) {
                out.println(best == inf ? -1 : best);
                continue;
            }
            pq.add(e);
            edgeMap.put(sig, e);
            int sizeA, sizeB;
            boolean connected = dcc.check(a, b);
            if (!connected) {
                sizeA = dcc.getConnectedComponentSize(a) % 2;
                sizeB = dcc.getConnectedComponentSize(b) % 2;
                oddCnt -= sizeA % 2;
                oddCnt -= sizeB % 2;
                oddCnt += (sizeA + sizeB) % 2;
            }
            dcc.addEdge(a, b);

            while (oddCnt == 0) {
                Edge last = pq.pollLast();
                edgeMap.remove(last.sig);
                best = Math.min(best, last.l);
                dcc.deleteEdge(last.a, last.b);
                if(!dcc.check(last.a, last.b)) {
                    sizeA = dcc.getConnectedComponentSize(last.a) % 2;
                    sizeB = dcc.getConnectedComponentSize(last.b) % 2;
                    oddCnt += sizeA % 2;
                    oddCnt += sizeB % 2;
                    oddCnt -= (sizeA + sizeB) % 2;
                }
            }
            out.println(best == inf ? -1 : best);
        }
    }
}

class Edge {
    int a;
    int b;
    int l;
    long sig;
}