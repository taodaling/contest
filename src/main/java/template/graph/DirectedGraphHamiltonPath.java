package template.graph;

import template.datastructure.LCTNode;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * https://codeforces.com/blog/entry/90513
 */
public class DirectedGraphHamiltonPath {
    long ms;
    long eachRoundFail;

    public DirectedGraphHamiltonPath(long ms, long eachRoundFail) {
        this.ms = ms;
        this.eachRoundFail = eachRoundFail;
    }

    public Optional<HamiltonPath> solve(int n, List<int[]> edgeList, int s, int t) {
        List<int[]> list = new ArrayList<>(edgeList.size() + 2);
        list.addAll(edgeList);
        list.add(new int[]{n, s});
        list.add(new int[]{t, n + 1});
        Optional<HamiltonPath> sol = solve(n + 2, list);
        if (sol.isPresent()) {
            sol.get().edges = Arrays.copyOfRange(sol.get().edges, 1, n);
        }
        return sol;
    }

    Debug debug = new Debug(true);

    public Optional<HamiltonPath> solve(int n, List<int[]> edgeList) {
        int[][] E = edgeList.stream().toArray(int[][]::new);
        int[][] origin = E.clone();
        LCTNode[] nodes = new LCTNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
        }

        int[][] out = new int[n][];
        int[][] in = new int[n][];

        int now = 0;
        int mask = (1 << 6) - 1;
        long end = System.currentTimeMillis() + ms;
        while (System.currentTimeMillis() < end) {
            System.arraycopy(origin, 0, E, 0, E.length);
            Arrays.fill(out, null);
            Arrays.fill(in, null);
            for (int i = 0; i < n; i++) {
                nodes[i].init();
            }
            int size = E.length;
            long chance = eachRoundFail;
            int carry = 0;
            while (System.currentTimeMillis() < end && chance >= 0 && size > 0 && carry < n - 1) {
                Randomized.shuffle(E, 0, size);
                int wpos = 0;
                int rep = 0;
                for (int i = 0; i < size && chance >= 0; i++) {
                    now++;
                    if ((now & mask) == 0 && System.currentTimeMillis() >= end) {
                        break;
                    }
                    chance--;
                    int[] e = E[i];
                    int a = e[0];
                    int b = e[1];
                    if (LCTNode.connected(nodes[a], nodes[b]) || in[b] != null && out[a] != null) {
                        E[wpos++] = E[i];
                        continue;
                    }
                    if (carry == n - 1) {
                        continue;
                    }
                    int[] conflict = in[b] != null ? in[b] : out[a];
                    if (conflict != null) {
                        if (RandomWrapper.INSTANCE.nextInt(0, 1) == 1) {
                            E[wpos++] = E[i];
                            continue;
                        } else {
                            int[] kickout = conflict;
                            out[kickout[0]] = null;
                            in[kickout[1]] = null;
                            assert LCTNode.connected(nodes[kickout[0]], nodes[kickout[1]]);
                            LCTNode.cut(nodes[kickout[0]], nodes[kickout[1]]);
                            E[wpos++] = kickout;
                            carry--;
                            rep++;
                        }
                    }
                    assert out[a] == null && in[b] == null;
                    out[a] = in[b] = e;
                    assert !LCTNode.connected(nodes[a], nodes[b]);
                    LCTNode.join(nodes[a], nodes[b]);
                    carry++;

                    if (conflict == null) {
                        debug.debug("carry", carry);
                    }
                }
                size = wpos;
                debug.debug("rep", rep);
            }

            //success
            if (carry == n - 1) {
                int[][] res = new int[n - 1][];
                int cur = -1;
                for (int i = 0; i < n; i++) {
                    if (out[i] != null && in[i] == null) {
                        cur = i;
                        break;
                    }
                }
                assert cur != -1;
                for (int i = 0; i < n - 1; i++) {
                    res[i] = out[cur];
                    cur = out[cur][1];
                }

                return Optional.of(new HamiltonPath(res));
            }
        }

        return Optional.empty();
    }

    public static class HamiltonPath {
        public int[][] edges;

        private HamiltonPath(int[][] edges) {
            this.edges = edges;
        }

        public int endPoint(int i) {
            if (i == 0) {
                return edges[0][0];
            }
            return edges[i - 1][1];
        }
    }
}
