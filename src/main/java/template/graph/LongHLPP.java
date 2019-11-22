package template.graph;

import template.datastructure.IntDequeBeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LongHLPP {
    private static final long INF = Long.MAX_VALUE;
    private int maxV;
    private int s;
    private int t;
    private List<Edge>[] adj;
    private List<Integer>[] lst;
    private List<Integer>[] gap;
    private long[] excess;
    private int highest;
    private int work;
    private int[] height;
    private int[] cnt;

    public LongHLPP(int maxV, int s, int t) {
        this.maxV = maxV;
        this.s = s;
        this.t = t;
        this.init();
    }

    private void init() {
        deque = new IntDequeBeta(maxV);
        adj = new ArrayList[maxV + 5];
        lst = new ArrayList[maxV + 5];
        gap = new ArrayList[maxV + 5];
        cnt = new int[maxV + 5];
        height = new int[maxV + 5];
        excess = new long[maxV + 5];
        for (int i = 0; i < maxV + 5; ++i) {
            adj[i] = new ArrayList<>();
            lst[i] = new ArrayList<>();
            gap[i] = new ArrayList<>();
        }
    }

    public void addEdge(int from, int to, long f, boolean isDirected) {
        adj[from].add(new Edge(to, adj[to].size(), f));
        adj[to].add(new Edge(from, adj[from].size() - 1, isDirected ? 0 : f));
    }

    void updHeight(int v, int nh) {
        work++;
        if (height[v] != maxV) {
            cnt[height[v]]--;
        }
        height[v] = nh;
        if (nh == maxV) {
            return;
        }
        cnt[nh]++;
        highest = nh;
        gap[nh].add(v);
        if (excess[v] > 0) {
            lst[nh].add(v);
        }
    }

    IntDequeBeta deque;

    private void globalRelabel() {
        work = 0;
        Arrays.fill(height, maxV);
        Arrays.fill(cnt, 0);
        for (int i = 0; i < highest; ++i) {
            lst[i].clear();
            gap[i].clear();
        }
        height[t] = 0;
        deque.addLast(t);
        while (!deque.isEmpty()) {
            int v = deque.removeFirst();
            for (Edge e : adj[v]) {
                if (height[e.to] == maxV && adj[e.to].get(e.rev).f > 0) {
                    deque.addLast(e.to);
                    updHeight(e.to, height[v] + 1);
                }
            }
            highest = height[v];
        }
        deque.clear();
    }

    private void push(int v, Edge e) {
        if (excess[e.to] == 0) {
            lst[height[e.to]].add(e.to);
        }
        long df = Math.min(excess[v], e.f);
        e.f -= df;
        adj[e.to].get(e.rev).f += df;
        excess[v] -= df;
        excess[e.to] += df;
    }

    private void discharge(int v) {
        int nh = maxV;
        for (Edge e : adj[v]) {
            if (e.f > 0) {
                if (height[v] == height[e.to] + 1) {
                    push(v, e);
                    if (excess[v] <= 0) {
                        return;
                    }
                } else {
                    nh = Math.min(nh, height[e.to] + 1);
                }
            }
        }
        if (cnt[height[v]] > 1) {
            updHeight(v, nh);
        } else {
            for (int i = height[v]; i <= highest; ++i) {
                for (int curGap : gap[i]) {
                    updHeight(curGap, maxV);
                }
                gap[i].clear();
            }
        }
    }

    private long calc(int heur_n) {
        Arrays.fill(excess, 0);
        excess[s] = INF;
        excess[t] = -INF;
        globalRelabel();
        for (Edge e : adj[s]) {
            push(s, e);
        }
        for (; highest >= 0; highest--) {
            while (!lst[highest].isEmpty()) {
                int idx = lst[highest].size() - 1;
                int v = lst[highest].get(idx);
                lst[highest].remove(idx);
                discharge(v);
                if (work > 4 * heur_n) {
                    globalRelabel();
                }
            }
        }
        return excess[t] + INF;
    }

    public long calc() {
        return calc(maxV);
    }

    static class Edge {
        int to;
        int rev;
        long f;

        Edge(int to, int rev, long f) {
            this.to = to;
            this.rev = rev;
            this.f = f;
        }
    }
}