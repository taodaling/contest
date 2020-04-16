package on2020_04.on2020_04_16_TopCoder_SRM__763.RootItRight;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RootItRight {
    public long findMinimumTotalCost(int N, int[] edge, int[] val, int D, int seed, int MX) {
        Node[] nodes = new Node[N];
        for (int i = 0; i < N; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        long[] A = new long[N * 2];
        A[0] = seed;
        for (int i = 1; i <= 2 * N - 1; i++) {
            A[i] = (A[i - 1] * 1103515245 + 12345) % 2147483648L;
        }

        int[] E = Arrays.copyOf(edge, N);
        for (int i = edge.length; i <= N - 1; i++) {
            E[i] = (int) ((A[i] % Math.min(i, D)) + i - Math.min(i, D));
        }
        for (int i = 1; i <= N - 1; i++) {
            Node a = nodes[i];
            Node b = nodes[E[i]];
            a.next.add(b);
            b.next.add(a);
        }

        int[] V = Arrays.copyOf(val, N);
        for (int i = val.length; i <= N - 1; i++) {
            V[i] = (int) (A[N + i] % MX);
        }

        for (int i = 0; i < N; i++) {
            nodes[i].A = V[i];
        }

        dfsForSize(nodes[0], null);
        Node root = dfsForRoot(nodes[0], null, 0, 0);
        dfsForSize(root, null);
        long ans = dfsForAns(root, null, 0);
        return ans;
    }

    public void dfsForSize(Node root, Node p) {
        root.cnt = 1;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.sum += node.sum;
            root.cnt += node.cnt;
        }
        root.sum += root.cnt * root.A;
    }

    public Node dfsForRoot(Node root, Node p, long pSum, long pCnt) {
        long cnt = pCnt + root.cnt;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            long rCnt = cnt - node.cnt;
            long cand1 = 0;
            cand1 += (cnt - node.cnt) * node.A;
            cand1 += pSum + rCnt * root.A - (node.sum - node.A * node.cnt + cnt * node.A);

            if (cand1 < 0) {
                return dfsForRoot(node, root, pSum + rCnt * root.A, cnt - node.cnt);
            }
        }
        return root;
    }

    public long dfsForAns(Node root, Node p, long d) {
        long sum = d * root.A * root.cnt;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            sum += dfsForAns(node, root, d + 1);
        }
        return sum;
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    long A;
    long sum;
    int cnt;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}