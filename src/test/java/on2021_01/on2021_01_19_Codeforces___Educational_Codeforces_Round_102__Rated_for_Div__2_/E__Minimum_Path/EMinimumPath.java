package on2021_01.on2021_01_19_Codeforces___Educational_Codeforces_Round_102__Rated_for_Div__2_.E__Minimum_Path;



import template.binary.Bits;
import template.datastructure.PairingHeap;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class EMinimumPath {
    Node[][] nodes;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        nodes = new Node[n][4];

        long inf = (long) 1e18;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {
                nodes[i][j] = new Node();
                nodes[i][j].id = i * 10 + j;
                nodes[i][j].dist = inf;
                nodes[i][j].ph = new PairingHeap<>(nodes[i][j]);
            }
        }
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int w = in.ri();
            addEdge(a, b, w);
            addEdge(b, a, w);
        }

        Comparator<Node> cmp = Comparator.comparingLong(x -> x.dist);
        nodes[0][0].dist = 0;
        PairingHeap<Node> heap = PairingHeap.NIL;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {
                heap = PairingHeap.merge(heap, nodes[i][j].ph, cmp);
            }
        }
        while (!PairingHeap.isEmpty(heap)) {
            Node head = PairingHeap.peek(heap);
            heap = PairingHeap.pop(heap, cmp);
            for (Edge e : head.adj) {
                long cand = head.dist + e.w;
                if (cand >= e.to.dist) {
                    continue;
                }
                e.to.dist = cand;
                heap = PairingHeap.decrease(heap, e.to.ph, e.to, cmp);
            }
        }

        for (int i = 1; i < n; i++) {
            long ans = nodes[i][3].dist;
            out.println(ans);
        }
    }

    void addEdge(int a, int b, long w) {
        for (int i = 0; i < 4; i++) {
            for (int useMin = 0; useMin <= 1; useMin++) {
                if (useMin == 1 && Bits.get(i, 0) == 1) {
                    continue;
                }
                for (int useMax = 0; useMax <= 1; useMax++) {
                    if (useMax == 1 && Bits.get(i, 1) == 1) {
                        continue;
                    }
                    int state = i | (useMin) | (useMax << 1);
                    addEdge(nodes[a][i], nodes[b][state], (1 - useMax + useMin) * w);
                }
            }
        }
    }


    void addEdge(Node a, Node b, long w) {
        Edge e = new Edge();
        e.to = b;
        e.w = w;
        a.adj.add(e);
    }
}

class Event {
    Node a;
    long dist;

    public Event(Node a) {
        this.a = a;
        dist = a.dist;
    }
}

class Edge {
    Node to;
    long w;
}

class Node {
    long dist;
    int id;
    List<Edge> adj = new ArrayList<>();
    PairingHeap<Node> ph;
}
