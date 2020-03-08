package on2020_03.on2020_03_09_Social_Infrastructure_Information_Systems_Division__Hitachi_Programming_Contest_2020.C___ThREE;



import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.graph.UndirectedEulerTrace;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

import java.util.List;

public class CThREE {
    List<UndirectedEdge>[] g;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        collector = new IntegerList[2];
        for (int i = 0; i < 2; i++) {
            collector[i] = new IntegerList(n);
        }

        g = Graph.createUndirectedGraph(n);
        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }

        dfs(0, -1, 0);
        if (collector[0].size() > collector[1].size()) {
            SequenceUtils.swap(collector, 0, 1);
        }

        IntegerDeque[] dqs = new IntegerDeque[3];
        for (int i = 0; i < 3; i++) {
            dqs[i] = new IntegerDequeImpl(n);
        }
        for (int i = 1; i <= n; i++) {
            dqs[i % 3].addLast(i);
        }
        int[] ans = new int[n];
        if (collector[0].size() <= n / 3) {
            for (int i = 0; i < collector[0].size(); i++) {
                ans[collector[0].get(i)] = dqs[0].removeFirst();
            }
        } else {
            for (int i = 0; !dqs[1].isEmpty(); i++) {
                ans[collector[0].get(i)] = dqs[1].removeFirst();
            }
            for (int i = 0; !dqs[2].isEmpty(); i++) {
                ans[collector[1].get(i)] = dqs[2].removeFirst();
            }
        }

        for (int i = 0; i < n; i++) {
            if (ans[i] != 0) {
                continue;
            }
            for (int j = 0; j < 3; j++) {
                if (dqs[j].isEmpty()) {
                    continue;
                }
                ans[i] = dqs[j].removeFirst();
                break;
            }
        }

        for(int a : ans){
            out.append(a).append(' ');
        }
    }

    IntegerList[] collector;

    public void dfs(int root, int p, int depth) {
        collector[depth & 1].add(root);
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, depth + 1);
        }
    }
}
