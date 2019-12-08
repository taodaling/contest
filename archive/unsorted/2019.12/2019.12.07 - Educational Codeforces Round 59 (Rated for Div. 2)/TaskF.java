package contest;

import graphs.flows.MinCostFlowDijkstra;
import template.graph.LongMinCostMaxFlow;
import template.graph.MinCostMaxFlow;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.*;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        Offer[] offers = new Offer[n];
        for (int i = 0; i < n; i++) {
            offers[i] = new Offer();
            offers[i].a = in.readInt();
            offers[i].b = in.readInt();
            offers[i].k = in.readInt();
        }
        MinCostFlowDijkstra mcmf = new MinCostFlowDijkstra(idOfDst() + 1);
        for (int i = 0; i < n; i++) {
            mcmf.addEdge(idOfSrc(), idOfOffer(i), 1, 0);
        }
        for (int i = 0; i < n; i++) {
            mcmf.addEdge(idOfDay(i), idOfDst(), 1, 0);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mcmf.addEdge(idOfOffer(i), idOfDay(j), 1, Math.min(j, offers[i].k) * offers[i].b -
                        offers[i].a);
            }
        }

        long ans = 0;
        long sum = 0;
        for(int i = 0; i < n; i++){
            sum -= mcmf.minCostFlow(idOfSrc(), idOfDst(), 1)[1];
            ans = Math.max(ans, sum);
        }
        out.println(ans);
    }

    int n;

    public int idOfOffer(int i) {
        return i;
    }

    public int idOfDay(int d) {
        return n + d;
    }

    public int idOfSrc() {
        return 2 * n;
    }

    public int idOfDst() {
        return idOfSrc() + 1;
    }
}

class Offer {
    long a;
    long b;
    long k;
}