package contest;

import template.binary.Bits;
import template.datastructure.LinearBasis;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.graph.LongWeightGraph;
import template.primitve.generated.graph.LongWeightUndirectedEdge;
import template.string.PalindromeAutomaton;
import template.utils.SequenceUtils;

import java.util.List;

public class GXorMaticNumberOfTheGraph {
    List<LongWeightUndirectedEdge>[] g;
    LinearBasis lb = new LinearBasis();
    boolean[] visited;
    long[][][] bitCnt;
    long[][] two;
    long[] xorPath;
    int mod = (int) 1e9 + 7;
    int[] depth;
    List<LongWeightUndirectedEdge>[] tree;

    public void dfsForLB(int root, int d, long xor) {
        xorPath[root] = xor;
        depth[root] = d;
        visited[root] = true;
        for (LongWeightUndirectedEdge e : g[root]) {
            if (visited[e.to]) {
                if (depth[e.to] < depth[root]) {
                    //to ancestor
                    lb.add(xorPath[root] ^ xorPath[e.to] ^ e.weight);
                }
            } else {
                dfsForLB(e.to, d + 1, xor ^ e.weight);
                tree[root].add(e);
            }
        }
    }

    public void dfsForBit(int root) {
        for(int i = 0; i < 60; i++){
            bitCnt[i][0][root] = 1;
        }
        for (LongWeightUndirectedEdge e : tree[root]) {
            dfsForBit(e.to);
            for(int bit = 0; bit < 60; bit++) {
                int b = Bits.get(e.weight, bit);
                //tree edge

                two[bit][0 ^ 0 ^ b] += bitCnt[bit][0][root] * bitCnt[bit][0][e.to];
                two[bit][0 ^ 1 ^ b] += bitCnt[bit][0][root] * bitCnt[bit][1][e.to];
                two[bit][1 ^ 0 ^ b] += bitCnt[bit][1][root] * bitCnt[bit][0][e.to];
                two[bit][1 ^ 1 ^ b] += bitCnt[bit][1][root] * bitCnt[bit][1][e.to];
                two[bit][0] %= mod;
                two[bit][1] %= mod;
                for (int i = 0; i < 2; i++) {
                    bitCnt[bit][i][root] += bitCnt[bit][i ^ b][e.to];
                    if (bitCnt[bit][i][root] >= mod) {
                        bitCnt[bit][i][root] -= mod;
                    }
                }
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        g = Graph.createGraph(n);
        tree = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            LongWeightGraph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1, in.rl());
        }
        visited = new boolean[n];
        bitCnt = new long[60][2][n];
        two = new long[60][2];
        depth = new int[n];
        xorPath = new long[n];

        long sum = 0;
        for (int i = 0; i < n; i++) {
            if (visited[i]) {
                continue;
            }
            lb.clear();
            dfsForLB(i, 0, 0);
            long or = 0;
            for (long e : lb.toArray()) {
                or |= e;
            }
            long total = lb.xorNumberCount();
            long halfTotal = total / 2 % mod;
            total %= mod;
            long bitVal = 1;
            SequenceUtils.deepFill(two, 0L);
            dfsForBit(i);
            for (int j = 0; j < 60; j++) {
                long way = 0;
                if (Bits.get(or, j) == 1) {
                    way = (two[j][0] + two[j][1]) * halfTotal % mod;
                } else {
                    way = two[j][1] * total % mod;
                }
                sum += way * bitVal % mod;
                bitVal += bitVal;
                if (bitVal >= mod) {
                    bitVal -= mod;
                }
            }
        }

        sum %= mod;
        out.println(sum);
    }
}

