package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

public class Domino {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntegerList[][] edges = new IntegerList[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                edges[i][j] = new IntegerList();
            }
        }
        UndirectedEulerTrace trace = new UndirectedEulerTrace(7, n);
        for (int i = 0; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            edges[a][b].add(i + 1);
            trace.addEdge(a, b);
        }

        if (!trace.findTrace()) {
            out.println("No solution");
            return;
        }
        IntegerList et = trace.getEulerTrace();
        for (int i = 1; i < et.size(); i++) {
            int a = et.get(i - 1);
            int b = et.get(i);
            if (!edges[a][b].isEmpty()) {
                out.append(edges[a][b].pop()).append(' ').append('+');
            } else {
                out.append(edges[b][a].pop()).append(' ').append('-');
            }
            out.println();
        }
    }
}
