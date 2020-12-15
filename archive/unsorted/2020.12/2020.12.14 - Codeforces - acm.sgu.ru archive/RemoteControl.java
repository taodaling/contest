package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.IntegerWeightDirectedEdge;
import template.primitve.generated.graph.IntegerWeightGraph;

import java.util.List;

public class RemoteControl {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        List<IntegerWeightDirectedEdge>[] g = Graph.createGraph(100);
        boolean[] numberState = new boolean[10];
        boolean up;
        boolean down;
        boolean special;
        for (int i = 1; i <= 3; i++) {
            numberState[i] = in.ri() == 1;
        }
        up = in.ri() == 1;

        for (int i = 4; i <= 6; i++) {
            numberState[i] = in.ri() == 1;
        }
        down = in.ri() == 1;

        for (int i = 7; i <= 9; i++) {
            numberState[i] = in.ri() == 1;
        }
        special = in.ri() == 1;
        numberState[0] = in.ri() == 1;

        if (up) {
            for (int i = 0; i < 100; i++) {
                IntegerWeightGraph.addEdge(g, i, (i + 1) % 100, 1);
            }
        }
        if (down) {
            for (int i = 0; i < 100; i++) {
                IntegerWeightGraph.addEdge(g, i, (i - 1 + 100) % 100, 1);
            }
        }
        for (int i = 0; i < 10; i++) {
            if (numberState[i]) {
                for (int j = 0; j < 100; j++) {
                    IntegerWeightGraph.addEdge(g, j, i, 1);
                }
            }
        }
        if (special) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (numberState[i] && numberState[j]) {
                        for (int t = 0; t < 100; t++) {
                            IntegerWeightGraph.addEdge(g, t, i * 10 + j, 3);
                        }
                    }
                }
            }
        }

        int s = in.ri();
        int t = in.ri();
        int[] dist = new int[100];
        int inf = (int) 1e8;
        IntegerWeightGraph.dijkstraV2(g, new IntegerArrayList(new int[]{s}), dist, inf);
        if(dist[t] == inf){
            out.println(-1);
        }else{
            out.println(dist[t]);
        }
    }
}
