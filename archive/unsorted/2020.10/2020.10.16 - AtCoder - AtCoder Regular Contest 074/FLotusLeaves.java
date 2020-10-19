package contest;

import template.io.FastInput;
import template.primitve.generated.graph.IntegerDinic;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;

import java.io.PrintWriter;
import java.util.List;

public class FLotusLeaves {
    int h;
    int w;

    public int getRow(int i) {
        return i;
    }

    public int getCol(int i) {
        return h + i;
    }

    public int getSrc() {
        return h + w + 1;
    }

    public int getDst() {
        return getSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        h = in.readInt();
        w = in.readInt();
        int inf = (int) 1e9;
        List<IntegerFlowEdge>[] g = IntegerFlow.createFlow(getDst() + 1);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                char c = in.readChar();
                if (c == 'o') {
                    IntegerFlow.addEdge(g, getRow(i), getCol(j), 1);
                    IntegerFlow.addEdge(g, getCol(j), getRow(i), 1);
                } else if (c == 'S') {
                    IntegerFlow.addEdge(g, getSrc(), getRow(i), inf);
                    IntegerFlow.addEdge(g, getSrc(), getCol(j), inf);
                } else if (c == 'T') {
                    IntegerFlow.addEdge(g, getRow(i), getDst(), inf);
                    IntegerFlow.addEdge(g, getCol(j), getDst(), inf);
                }
            }
        }

        IntegerDinic dinic = new IntegerDinic();
        int ans = dinic.apply(g, getSrc(), getDst(), inf);
        if (ans == inf) {
            out.print(-1);
            return;
        }
        out.print(ans);
    }
}
