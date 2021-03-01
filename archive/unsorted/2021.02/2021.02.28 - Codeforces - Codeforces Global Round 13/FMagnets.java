package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

public class FMagnets {
    FastInput in;
    FastOutput out;

    private int ask(int l, int r, int L, int R) {
        out.append("? ").append(r - l + 1).append(' ').append(R - L + 1).println();
        for (int i = l; i <= r; i++) {
            out.append(i + 1).append(' ');
        }
        out.println();
        for (int i = L; i <= R; i++) {
            out.append(i + 1).append(' ');
        }
        out.println();
        out.flush();
        return in.ri();
    }

    private int ask(Node a, Node b) {
        if (a == null || b == null) {
            return 0;
        }
        return ask(a.l, a.r, b.l, b.r);
    }

    Node build(int l, int r) {
        Node ans = new Node(l, r);
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            ans.left = build(l, m);
            ans.right = build(m + 1, r);
        }
        return ans;
    }

    Node bs(Node nice, Node root, boolean skip) {
        if (root.l == root.r) {
            root.ok = true;
            if (!skip) {
                root.ok = ask(nice, root) != 0;
            }
            return root.ok ? root : null;
        }
        if (ask(nice, root.left) != 0) {
            return bs(nice, root.left, true);
        } else {
            return bs(nice, root.right, skip);
        }
    }


    void bf(Node nice, Node root) {
        if (root.l == root.r) {
            root.ok = ask(nice, root) != 0;
            return;
        }
        bf(nice, root.left);
        bf(nice, root.right);
    }

    private Node bfSet(Node root) {
        if (root.l == root.r) {
            root.ok = true;
            return root;
        }
        bfSet(root.left);
        return bfSet(root.right);
    }

    Node solve1(Node root) {
        if (root.l >= root.r) {
            return bfSet(root);
        }
        Node ans = solve(root.left);
        if (ans != null) {
            bf(ans, root.right);
            return ans;
        }
        if (ask(root.left, root.right) == 0) {
            ans = solve(root.right);
            bs(ans, root.left, false);
            return ans;
        }
        ans = bs(root.right, root.left, true);
        bf(ans, root.right);
        return ans;
    }

    Node solve(Node root, boolean skip) {
        if (root.l == root.r) {
            root.ok = skip;
            return root.ok ? root : null;
        }
        Node ans = solve(root.left, false);
        if (ans == null) {
            if (ask(root.left, root.right) == 0) {
                ans = solve(root.right, skip);
                if (ans == null) {
                    return null;
                }
                if (ask(ans, root.left) != 0) {
                    bs(ans, root.left, true);
                }
                return ans;
            }
            ans = bs(root.right, root.left, true);
            bf(ans, root.right, );
            return ans;
        } else {
            bf(ans, root.right);
            return ans;
        }
    }

    void output(Node root, IntegerArrayList seq) {
        if (root.l == root.r) {
            if (!root.ok) {
                seq.add(root.l);
            }
            return;
        }
        output(root.left, seq);
        output(root.right, seq);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.ri();
        Node root = build(0, n - 1);
        solve(root);
        IntegerArrayList ans = new IntegerArrayList(n);
        output(root, ans);
        out.append("! ").append(ans.size()).append(' ');
        for (int x : ans.toArray()) {
            out.append(x + 1).append(' ');
        }
        out.println().flush();
    }
}

class Node {
    Node left;
    Node right;
    int l;
    int r;
    boolean ok;

    public Node(int l, int r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", l, r);
    }
}