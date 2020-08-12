package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FBeautyOfAPermutation {
    boolean[][] dp = new boolean[101][10000];

    public int choose2(int n) {
        return n * (n - 1) / 2;
    }


    {
        dp[0][0] = true;
        dp[1][0] = true;
        for (int i = 0; i <= 100; i++) {
            for (int j = 0; j < 10000; j++) {
                //merge join node
                for (int k = 2; k <= i; k++) {
                    int next = j - choose2(k);
                    if (next >= 0 && next < 10000) {
                        dp[i][j] = dp[i][j] || dp[i - k + 1][next];
                    }
                }
                //merge cut node
                for (int k = 4; k <= i; k++) {
                    if (j > 0) {
                        dp[i][j] = dp[i][j] || dp[i - k + 1][j - 1];
                    }
                }

            }
        }
    }

    Node[] nodes;

    public Node build(int j, int l, int r) {
        int i = r - l + 1;
        if (l == r) {
            return nodes[l];
        }

        //merge join node
        for (int k = 2; k <= i; k++) {
            int next = j - choose2(k);
            if (next >= 0 && next < 10000 && dp[i - k + 1][next]) {
                Node root = new Node();
                root.join = true;
                for (int t = 0; t < k; t++) {
                    root.adj.add(nodes[l + t]);
                }
                l += k - 1;
                nodes[l] = root;
                return build(next, l, r);
            }
        }
        //merge cut node
        for (int k = 4; k <= i; k++) {
            if (j > 0 && dp[i - k + 1][j - 1]) {
                Node root = new Node();
                root.join = false;
                for (int t = 0; t < k; t++) {
                    root.adj.add(nodes[l + t]);
                }
                l += k - 1;
                nodes[l] = root;
                return build(j - 1, l, r);
            }
        }

        throw new IllegalStateException();
    }

    int order = 0;
    Deque<Node> dq = new ArrayDeque<>(100);
    Deque<Node> dq2 = new ArrayDeque<>(100);

    public void shuffle(List<Node> list) {
        dq.clear();
        dq2.clear();
        if (list.size() % 2 == 1) {
            dq2.addFirst(list.remove(list.size() / 2));
        }
        dq.addAll(list);
        list.clear();
        int cmd = 3;
        while (!dq.isEmpty()) {
            Node node = Bits.get(cmd, 0) == 1 ? dq.removeLast() : dq.removeFirst();
            if (Bits.get(cmd, 1) == 1) {
                dq2.addLast(node);
            } else {
                dq2.addFirst(node);
            }
            cmd++;
        }


        list.addAll(dq2);
    }

    public void dfs(Node root, boolean inc) {
        if (root.adj.isEmpty()) {
            root.val = order++;
            return;
        }
        boolean cur = false;
        for (Node node : root.adj) {
            dfs(node, cur);
            cur = !cur;
        }
        if (!root.join) {
            //interleave
            shuffle(root.adj);
        }
        if (!inc) {
            SequenceUtils.reverse(root.adj);
        }
    }

    FastOutput out;

    public void output(Node root) {
        if (root.adj.isEmpty()) {
            out.append(root.val + 1).append(' ');
            return;
        }
        for (Node node : root.adj) {
            output(node);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.out = out;
        order = 0;
        int n = in.readInt();
        int k = in.readInt();
        k -= n;
        if (k < 0 || !dp[n][k]) {
            out.println("NO");
            return;
        }
        out.println("YES");

        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].join = true;
        }
        Node root = build(k, 0, n - 1);
        dfs(root, true);
        output(root);
        out.println();
    }
}

class Node {
    boolean join;
    int val;
    List<Node> adj = new ArrayList<>();
}