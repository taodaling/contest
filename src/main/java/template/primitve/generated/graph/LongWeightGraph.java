package template.primitve.generated.graph;

import template.datastructure.PairingHeap;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LongWeightGraph {
    public static LongWeightDirectedEdge addEdge(List<LongWeightDirectedEdge>[] g, int s, int t, long w) {
        LongWeightDirectedEdge ans = new LongWeightDirectedEdge(t, w);
        g[s].add(ans);
        return ans;
    }

    public static LongWeightUndirectedEdge addUndirectedEdge(List<LongWeightUndirectedEdge>[] g, int s, int t, long w) {
        LongWeightUndirectedEdge toT = new LongWeightUndirectedEdge(t, w);
        LongWeightUndirectedEdge toS = new LongWeightUndirectedEdge(s, w);
        toS.rev = toT;
        toT.rev = toS;
        g[s].add(toT);
        g[t].add(toS);
        return toT;
    }

    public static List<LongWeightDirectedEdge>[] createDirectedGraph(int n) {
        List<LongWeightDirectedEdge>[] ans = new List[n];
        for (int i = 0; i < n; i++) {
            ans[i] = new ArrayList<>();
        }
        return ans;
    }

    public static List<LongWeightUndirectedEdge>[] createUndirectedGraph(int n) {
        List<LongWeightUndirectedEdge>[] ans = new List[n];
        for (int i = 0; i < n; i++) {
            ans[i] = new ArrayList<>();
        }
        return ans;
    }

    public static <T extends LongWeightDirectedEdge> void dijkstraElogV(List<T>[] g, IntegerArrayList s, long[] dists, int[] prev, long inf) {
        int n = g.length;
        LongPriorityQueueBasedOnSegment pq = new LongPriorityQueueBasedOnSegment(0, n);
        pq.reset(0, n);
        for (int i = 0; i < n; i++) {
            dists[i] = inf;
            if (prev != null) {
                prev[i] = -1;
            }
        }
        for (int i = s.size() - 1; i >= 0; i--) {
            int node = s.get(i);
            dists[node] = 0;
            pq.update(node, 0, n, 0);
        }
        for (int i = 0; i < n; i++) {
            int head = pq.pop(0, n);
            if (dists[head] >= inf) {
                break;
            }
            for (LongWeightDirectedEdge e : g[head]) {
                if (dists[e.to] > dists[head] + e.weight) {
                    dists[e.to] = dists[head] + e.weight;
                    pq.update(e.to, 0, n, dists[e.to]);
                    if (prev != null) {
                        prev[e.to] = head;
                    }
                }
            }
        }
    }


    public static <T extends LongWeightDirectedEdge> void dijkstraPairingHeap(List<T>[] g, IntegerArrayList s, long[] dists, int[] prev, long inf) {
        int n = g.length;
        for (int i = 0; i < n; i++) {
            dists[i] = inf;
            if (prev != null) {
                prev[i] = -1;
            }
        }
        for (int i = s.size() - 1; i >= 0; i--) {
            int node = s.get(i);
            dists[node] = 0;
        }
        Comparator<Integer> cmpByDist = (a, b) -> Long.compare(dists[a], dists[b]);
        PairingHeap<Integer>[] ph = new PairingHeap[n];
        PairingHeap<Integer> heap = PairingHeap.NIL;
        for (int i = 0; i < n; i++) {
            ph[i] = new PairingHeap<>(i);
            heap = PairingHeap.merge(heap, ph[i], cmpByDist);
        }
        for (int i = 0; i < n; i++) {
            int head = PairingHeap.peek(heap);
            heap = PairingHeap.pop(heap, cmpByDist);
            if (dists[head] >= inf) {
                break;
            }
            for (LongWeightDirectedEdge e : g[head]) {
                if (dists[e.to] > dists[head] + e.weight) {
                    dists[e.to] = dists[head] + e.weight;
                    heap = PairingHeap.decrease(heap, ph[e.to], e.to, cmpByDist);
                    if (prev != null) {
                        prev[e.to] = head;
                    }
                }
            }
        }
    }

    public static <T extends LongWeightDirectedEdge> void dijkstraV2(List<T>[] g, IntegerArrayList s, long[] dists, long inf) {
        int n = g.length;
        for (int i = 0; i < g.length; i++) {
            dists[i] = inf;
        }
        boolean[] handled = new boolean[n];
        for (int i = s.size() - 1; i >= 0; i--) {
            int node = s.get(i);
            dists[node] = 0;
        }
        for (int i = 0; i < n; i++) {
            int head = -1;
            for (int j = 0; j < n; j++) {
                if (!handled[j] && (head == -1 || dists[head] > dists[j])) {
                    head = j;
                }
            }
            handled[head] = true;
            for (LongWeightDirectedEdge e : g[head]) {
                dists[e.to] = Math.min(dists[e.to], dists[head] + e.weight);
            }
        }
    }

    public static <T extends LongWeightDirectedEdge> void spfa(List<T>[] g, IntegerArrayList s, long[] dists, long inf) {
        int n = g.length;
        for (int i = 0; i < g.length; i++) {
            dists[i] = inf;
        }
        IntegerDeque deque = new IntegerDequeImpl(n);
        boolean[] inque = new boolean[n];
        for (int i = s.size() - 1; i >= 0; i--) {
            int node = s.get(i);
            dists[node] = 0;
            deque.addLast(node);
        }
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            inque[head] = false;
            for (LongWeightDirectedEdge e : g[head]) {
                if (dists[e.to] > dists[head] + e.weight) {
                    dists[e.to] = dists[head] + e.weight;
                    if (!inque[e.to]) {
                        inque[e.to] = true;
                        deque.addLast(e.to);
                    }
                }
            }
        }
    }

    /**
     * @return whether exists a negative circle
     */
    public static <T extends LongWeightDirectedEdge> boolean spfaWithPossibleNegativeCircle(List<T>[] g, IntegerArrayList s, long[] dists, long inf) {
        int n = g.length;
        for (int i = 0; i < g.length; i++) {
            dists[i] = inf;
        }

        IntegerDeque deque = new IntegerDequeImpl(n);
        boolean[] inque = new boolean[n];
        int[] added = new int[n];
        for (int i = s.size() - 1; i >= 0; i--) {
            int node = s.get(i);
            added[node] = -1;
            dists[node] = 0;
            deque.addLast(node);
        }
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            inque[head] = false;
            added[head]++;
            if (added[head] >= n) {
                return true;
            }

            for (LongWeightDirectedEdge e : g[head]) {
                if (dists[e.to] > dists[head] + e.weight) {
                    dists[e.to] = dists[head] + e.weight;
                    if (!inque[e.to]) {
                        inque[e.to] = true;
                        deque.addLast(e.to);
                    }
                }
            }
        }
        return false;
    }
}
