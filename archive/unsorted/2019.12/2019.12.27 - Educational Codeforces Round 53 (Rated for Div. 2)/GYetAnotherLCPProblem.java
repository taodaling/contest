package contest;

import template.datastructure.Array2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerArray2IntegerDequeAdapter;
import template.primitve.generated.IntegerDeque;
import template.primitve.generated.IntegerRMQ;
import template.string.SAIS;
import template.string.SuffixArray;

import java.util.Arrays;

public class GYetAnotherLCPProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        char[] s = new char[n + 1];
        in.readString(s, 1);
        SAIS sais = new SAIS(s, 1, n);
        int[] lcp = new int[n];
        for (int i = 0; i + 1 < n; i++) {
            lcp[i] = sais.longestCommonPrefixBetween(i + 1);
        }
        lcp[n - 1] = 0;
        IntegerRMQ rmq = new IntegerRMQ(lcp, (a, b) -> a - b);

        for (int i = 0; i < q; i++) {
            int k = in.readInt();
            int l = in.readInt();
            Node[] nodes = new Node[k + l];
            for (int j = 0; j < k; j++) {
                nodes[j] = new Node();
                nodes[j].type = 0;
                nodes[j].index = sais.queryRank(in.readInt());
            }
            for (int j = k; j < k + l; j++) {
                nodes[j] = new Node();
                nodes[j].type = 1;
                nodes[j].index = sais.queryRank(in.readInt());
            }
            Arrays.sort(nodes, (a, b) -> a.index - b.index);
            for (int j = 0; j + 1 < k + l; j++) {
                if (nodes[j].index == nodes[j + 1].index) {
                    nodes[j].lcpWithNext = n + 1 - sais.queryKth(nodes[j].index);
                } else {
                    nodes[j].lcpWithNext = lcp[rmq.query(nodes[j].index, nodes[j + 1].index - 1)];
                }
            }
            nodes[k + l - 1].lcpWithNext = 0;
            long ans = dac(nodes, 0, k + l - 1);
            out.println(ans);
        }
    }

    public long dac(Node[] nodes, int l, int r) {
        if (l >= r) {
            return 0;
        }
        int m = (l + r) >>> 1;
        long ans = dac(nodes, l, m) + dac(nodes, m + 1, r);
        long contrib = 0;

        SimplifiedDeque<Node> deque = new Array2DequeAdapter<>(nodes, m, r);
        long[] cnts = new long[2];
        int lMin = nodes[m].lcpWithNext;
        int rMin = (int) 1e8;
        for (int i = m; i >= l; i--) {
            lMin = Math.min(lMin, nodes[i].lcpWithNext);
            while (!deque.isEmpty() && Math.min(deque.peekFirst().lcpWithNext, rMin) >= lMin) {
                deque.removeFirst();
                if (!deque.isEmpty()) {
                    cnts[deque.peekFirst().type]++;
                }
            }
            contrib += lMin * cnts[1 - nodes[i].type];
        }
        deque = new Array2DequeAdapter<>(nodes, l, m);
        cnts[0] = cnts[1] = 0;
        lMin = (int) 1e8;
        rMin = nodes[m].lcpWithNext;
        for (int i = m + 1; i <= r; i++) {
            rMin = Math.min(rMin, nodes[i - 1].lcpWithNext);
            while (!deque.isEmpty() && Math.min(deque.peekLast().lcpWithNext, lMin) > rMin) {
                cnts[deque.removeLast().type]++;
            }
            contrib += rMin * cnts[1 - nodes[i].type];
        }

        return ans + contrib;
    }
}

class Node {
    int type;
    int index;
    int lcpWithNext;
}
