package on2021_08.on2021_08_22_Codeforces___Codeforces_Round__320__Div__1___Bayan_Thanks_Round_.E__Walking_;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.*;
import template.utils.CollectionUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EWalking {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        s = new char[(int) 1e5];
        n = in.rs(s);
        for (int i = 0; i < n; i++) {
            s[i] = (char) (s[i] == 'R' ? 1 : 0);
        }
        List<IntegerFlowEdge>[] g = Graph.createGraph(dst() + 1);
        int inf = (int) 1e9;
        for (int i = n - 1; i >= 0; i--) {
            if (i + 1 < n) {
                IntegerFlow.addFlowEdge(g, type0(i), type0(i + 1), inf);
                IntegerFlow.addFlowEdge(g, type1(i), type1(i + 1), inf);
                IntegerFlow.addFlowEdge(g, type(s[i + 1] ^ 1, i), dst(), 1);
            }
            IntegerFlow.addFlowEdge(g, src(), type(s[i], i), 1);
        }

        IntegerMaximumFlow mf = new IntegerISAP();
        int flow = mf.apply(g, src(), dst(), inf);
        debug.debug("flow", flow);
        next = new int[n];
        prev = new int[n];
        Arrays.fill(next, -1);
        Arrays.fill(prev, -1);
        IntegerArrayList[] cand = new IntegerArrayList[]{new IntegerArrayList(n), new IntegerArrayList(n)};
//        debug.debug("g", IntegerFlow.flowToString(g));
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 2; j++) {
                for (IntegerFlowEdge e : g[type(j, i)]) {
                    if (e.to == dst()) {
                        if (e.flow == 1) {
                            cand[j].add(i + 1);
                        }
                    }
                }
            }
            if (!cand[s[i]].isEmpty()) {
                next[i] = cand[s[i]].pop();
                prev[next[i]] = i;
            }
        }
        debug.debug("next", next);
        //01
        List<int[]> path = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (prev[i] != -1) {
                continue;
            }
            int end = i;
            while (next[end] != -1) {
                end = next[end];
            }
            path.add(new int[]{i, end});
        }
        out.println(path.size() - 1);

        List<int[]>[][] type = new List[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                type[i][j] = new ArrayList<>(n);
            }
        }
        for (int[] p : path) {
            type[s[p[0]]][s[p[1]]].add(p);
        }
        while (type[0][0].size() > 0 && type[1][1].size() > 0) {
            int[] t1 = CollectionUtils.pop(type[0][0]);
            int[] t2 = CollectionUtils.pop(type[1][1]);
            int[] merge = concat(t1, t2);
            type[0][1].add(merge);
        }
        while (type[0][1].size() >= 2) {
            int[] t1 = CollectionUtils.pop(type[0][1]);
            int[] t2 = CollectionUtils.pop(type[0][1]);
            type[0][1].add(concat(t1, t2));
        }
        while (type[1][0].size() >= 2) {
            int[] t1 = CollectionUtils.pop(type[1][0]);
            int[] t2 = CollectionUtils.pop(type[1][0]);
            type[1][0].add(concat(t1, t2));
        }
        if (type[0][1].size() > 0 && type[1][0].size() > 0) {
            int[] t1 = CollectionUtils.pop(type[0][1]);
            int[] t2 = CollectionUtils.pop(type[1][0]);
            if (t1[1] < t2[1]) {
                int[] end = detach(t2);
                type[0][1].add(concat(concat(t1, end), t2));
            } else {
                int[] end = detach(t1);
                type[1][0].add(concat(concat(t2, end), t1));
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (type[i][i ^ 1].size() > 0 && type[j][j].size() > 0) {
                    int[] t1 = CollectionUtils.pop(type[i][i ^ 1]);
                    int[] t2 = CollectionUtils.pop(type[j][j]);
                    int[] end;
                    if (i != j) {
                        end = concat(t2, t1);
                    } else {
                        end = concat(t1, t2);
                    }
                }
            }
        }

        int head = 0;
        for (int i = 0; i < n; i++) {
            if (prev[i] == -1) {
                head = i;
            }
        }
        while (head != -1) {
            out.append(head + 1).append(' ');
            head = next[head];
        }
    }

    int[] next;
    int[] prev;

    public int[] detach(int[] t1) {
        int end = t1[1];
        t1[1] = prev[end];
        next[prev[end]] = -1;
        prev[end] = -1;
        return new int[]{end, end};
    }

    public int[] concat(int[] t1, int[] t2) {
        next[t1[1]] = t2[0];
        prev[t2[0]] = t1[1];
        return new int[]{t1[0], t2[1]};
    }

    Debug debug = new Debug(false);
    char[] s;
    int n;

    public int type(int t, int i) {
        return t * n + i;
    }

    public int type0(int i) {
        return i;
    }

    public int type1(int i) {
        return i + n;
    }

    public int src() {
        return n * 2;
    }

    public int dst() {
        return src() + 1;
    }
}
