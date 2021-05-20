package template.graph;

import template.datastructure.LCTNode;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UndirectedGraphHamiltonPath {
    long ms;
    long eachRoundFail;

    public UndirectedGraphHamiltonPath(long ms, long eachRoundFail) {
        this.ms = ms;
        this.eachRoundFail = eachRoundFail;
    }

    public Optional<HamiltonPath> solve(int n, List<? extends UndirectedEdge> edgeList, int s, int t) {
        List<UndirectedEdge> list = new ArrayList<>(edgeList.size() + 2);
        list.addAll(edgeList);
        list.add(Graph.makeUndirectedEdge(n, s));
        list.add(Graph.makeUndirectedEdge(t, n + 1));
        Optional<HamiltonPath> sol = solve(n + 2, list);
        if (sol.isPresent()) {
            sol.get().edges = Arrays.copyOfRange(sol.get().edges, 1, n);
            sol.get().startWith(s);
        }
        return sol;
    }


    Debug debug = new Debug(false);

    public Optional<HamiltonPath> solve(int n, List<? extends UndirectedEdge> edgeList) {
        UndirectedEdge[] E = edgeList.stream().filter(x -> x.to != x.rev.to).toArray(UndirectedEdge[]::new);
        UndirectedEdge[] origin = E.clone();
        LCTNode[] nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
        }

        int[] degs = new int[n];
        List<UndirectedEdge>[] cand = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            cand[i] = new ArrayList(2);
        }

        int now = 0;
        int mask = (1 << 6) - 1;
        long end = System.currentTimeMillis() + ms;
        while (System.currentTimeMillis() < end) {
            System.arraycopy(origin, 0, E, 0, E.length);
            Arrays.fill(degs, 0);
            for (int i = 0; i < n; i++) {
                cand[i].clear();
            }
            int size = E.length;
            long chance = eachRoundFail;
            int carry = 0;
            while (System.currentTimeMillis() < end && chance >= 0 && size > 0 && carry < n - 1) {
                Randomized.shuffle(E, 0, size);
                int wpos = 0;
                for (int i = 0; i < size && chance >= 0; i++) {
                    now++;
                    if ((now & mask) == 0 && System.currentTimeMillis() >= end) {
                        break;
                    }
                    chance--;
                    UndirectedEdge e = E[i];
                    int a = e.to;
                    int b = e.rev.to;
                    if (LCTNode.connected(nodes[a], nodes[b]) || cand[a].size() >= 2 && cand[b].size() >= 2) {
                        E[wpos++] = E[i];
                        continue;
                    }
                    if (carry == n - 1) {
                        continue;
                    }
                    if (cand[a].size() == 2 || cand[b].size() == 2) {
                        if (cand[a].size() < cand[b].size()) {
                            int tmp = a;
                            a = b;
                            b = tmp;
                        }
                        int rep = RandomWrapper.INSTANCE.nextInt(4);
                        if (rep > 1) {
                            E[wpos++] = E[i];
                            continue;
                        } else {
                            UndirectedEdge kickout = cand[a].remove(rep);
                            int otherSide = kickout.to ^ kickout.rev.to ^ a;
                            boolean del = cand[otherSide].remove(kickout);
                            assert del;

                            debug.log("cut");
                            debug.debug("a", a);
                            debug.debug("b", otherSide);
                            assert LCTNode.connected(nodes[a], nodes[otherSide]);
                            LCTNode.cut(nodes[a], nodes[otherSide]);
                            E[wpos++] = kickout;
                            carry--;
                        }
                    }
                    assert cand[a].size() < 2 && cand[b].size() < 2;
                    cand[a].add(e);
                    cand[b].add(e);
                    assert !LCTNode.connected(nodes[a], nodes[b]);
                    LCTNode.join(nodes[a], nodes[b]);
                    debug.log("merge");
                    debug.debug("a", a);
                    debug.debug("b", b);
                    carry++;
                }
                size = wpos;
            }

            //success
            if (carry == n - 1) {
                UndirectedEdge[] res = new UndirectedEdge[n - 1];
                int cur = -1;
                for (int i = 0; i < n; i++) {
                    if (cand[i].size() == 1) {
                        cur = i;
                        break;
                    }
                }
                assert cur != -1;
                UndirectedEdge last = null;
                for (int i = 0; i < n - 1; i++) {
                    cand[cur].remove(last);
                    if (last != null) {
                        cand[cur].remove(last.rev);
                    }
                    assert !cand[cur].isEmpty();
                    last = cand[cur].get(0);
                    if (last.to == cur) {
                        last = last.rev;
                    }
                    res[i] = last;
                    cur = last.to;
                }

                return Optional.of(new HamiltonPath(res));
            }
        }

        return Optional.empty();
    }

    public static class HamiltonPath {
        public UndirectedEdge[] edges;

        private HamiltonPath(UndirectedEdge[] edges) {
            this.edges = edges;
        }

        public void reverse() {
            for (int i = 0; i < edges.length; i++) {
                edges[i] = edges[i].rev;
            }
            SequenceUtils.reverse(edges);
        }

        public int endPoint(int i) {
            if (i == 0) {
                return edges[0].rev.to;
            }
            return edges[i - 1].to;
        }

        public void startWith(int x) {
            if (endPoint(0) != x) {
                reverse();
            }
            if (endPoint(0) != x) {
                throw new IllegalStateException();
            }
        }
    }
}
