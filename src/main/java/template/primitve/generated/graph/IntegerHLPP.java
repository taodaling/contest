package template.primitve.generated.graph;

import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Very fast, but the flow on each edge is wrong, the maximum flow is right.
 */
/**
 * It contains some bugs
 */
@Deprecated
public class IntegerHLPP implements IntegerMaximumFlow {
    private int inf;
    private int vertexNum;
    private int s;
    private int t;
    private List<IntegerFlowEdge>[] adj;
    private IntegerArrayList[] lst;
    private List<Integer>[] gap;
    private int[] excess;
    private int highest;
    private int work;
    private int[] height;
    private int[] cnt;
    IntegerDequeImpl deque;

    public IntegerHLPP() {
    }

    public void ensure(int vertexNum) {
        if (lst != null && lst.length >= vertexNum + 5) {
            return;
        }
        deque = new IntegerDequeImpl(vertexNum);
        lst = new IntegerArrayList[vertexNum + 5];
        gap = new ArrayList[vertexNum + 5];
        cnt = new int[vertexNum + 5];
        height = new int[vertexNum + 5];
        excess = new int[vertexNum + 5];
        for (int i = 0; i < vertexNum + 5; ++i) {
            lst[i] = new IntegerArrayList();
            gap[i] = new ArrayList<>();
        }
    }

    private void init() {
        deque.clear();
        for (int i = 0; i < vertexNum + 5; ++i) {
            cnt[i] = 0;
            height[i] = 0;
            excess[i] = 0;
            lst[i].clear();
            gap[i].clear();
        }
    }

    void updHeight(int v, int nh) {
        work++;
        if (height[v] != vertexNum) {
            cnt[height[v]]--;
        }
        height[v] = nh;
        if (nh == vertexNum) {
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
        Arrays.fill(height, vertexNum);
        Arrays.fill(cnt, 0);
        for (int i = 0; i < highest; ++i) {
            lst[i].clear();
            gap[i].clear();
        }
        height[t] = 0;
        deque.addLast(t);
        while (!deque.isEmpty()) {
            int v = deque.removeFirst();
            for (IntegerFlowEdge e : adj[v]) {
                if (height[e.to] == vertexNum && e.flow > 0) {
                    deque.addLast(e.to);
                    updHeight(e.to, height[v] + 1);
                }
            }
            highest = height[v];
        }
        deque.clear();
    }

    private void push(int v, IntegerFlowEdge e) {
        if (excess[e.to] == 0) {
            lst[height[e.to]].add(e.to);
        }
        int df = Math.min(excess[v], e.rev.flow);
        IntegerFlow.send(e, df);
        excess[v] -= df;
        excess[e.to] += df;
    }

    private void discharge(int v) {
        int nh = vertexNum;
        for (IntegerFlowEdge e : adj[v]) {
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
                    updHeight(curGap, vertexNum);
                }
                gap[i].clear();
            }
        }
    }

    private int calc(int heur_n) {
        Arrays.fill(excess, 0);
        excess[s] = inf;
        excess[t] = -inf;
        globalRelabel();
        for (IntegerFlowEdge e : adj[s]) {
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

    public int calc() {
        return calc(vertexNum);
    }

    public int apply(List<IntegerFlowEdge>[] net, int s, int t, int send) {
        ensure(net.length);
        this.adj = net;
        this.s = s;
        this.t = t;
        this.vertexNum = net.length;
        this.inf = send;
        init();
        return calc();
    }
}