package template.primitve.generated.graph;

import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Very fast, but the flow on each edge is wrong, the maximum flow is right.
 */
public class LongHLPP implements LongMaximumFlow {
    private long inf;
    private int maxV;
    private int s;
    private int t;
    private List<LongFlowEdge>[] adj;
    private IntegerArrayList[] lst;
    private List<Integer>[] gap;
    private long[] excess;
    private int highest;
    private int work;
    private int[] height;
    private int[] cnt;
    IntegerDequeImpl deque;

    public LongHLPP(int maxV) {
        deque = new IntegerDequeImpl(maxV);
        lst = new IntegerArrayList[maxV + 5];
        gap = new ArrayList[maxV + 5];
        cnt = new int[maxV + 5];
        height = new int[maxV + 5];
        excess = new long[maxV + 5];
        for (int i = 0; i < maxV + 5; ++i) {
            lst[i] = new IntegerArrayList();
            gap[i] = new ArrayList<>();
        }
    }

    private void init() {
        deque.clear();
        for (int i = 0; i < maxV + 5; ++i) {
            cnt[i] = 0;
            height[i] = 0;
            excess[i] = 0;
            lst[i].clear();
            gap[i].clear();
        }
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
            for (LongFlowEdge e : adj[v]) {
                if (height[e.to] == maxV && e.flow > 0) {
                    deque.addLast(e.to);
                    updHeight(e.to, height[v] + 1);
                }
            }
            highest = height[v];
        }
        deque.clear();
    }

    private void push(int v, LongFlowEdge e) {
        if (excess[e.to] == 0) {
            lst[height[e.to]].add(e.to);
        }
        long df = Math.min(excess[v], e.rev.flow);
        LongFlow.send(e, df);
        excess[v] -= df;
        excess[e.to] += df;
    }

    private void discharge(int v) {
        int nh = maxV;
        for (LongFlowEdge e : adj[v]) {
            if (e.rev.flow > 0) {
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
        excess[s] = inf;
        excess[t] = -inf;
        globalRelabel();
        for (LongFlowEdge e : adj[s]) {
            push(s, e);
        }
        for (; highest >= 0; highest--) {
            while (!lst[highest].isEmpty()) {
                int v = lst[highest].pop();
                discharge(v);
                if (work > 4 * heur_n) {
                    globalRelabel();
                }
            }
        }
        return excess[t] + inf;
    }

    public long calc() {
        return calc(maxV);
    }

    public long apply(List<LongFlowEdge>[] net, int s, int t, long send) {
        this.adj = net;
        this.s = s;
        this.t = t;
        this.maxV = net.length;
        this.inf = send;
        init();
        return calc();
    }
}