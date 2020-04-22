package on2020_04.on2020_04_22_Codeforces___Codeforces_Round__449__Div__1_.E__Welcome_home__Chtholly;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class EWelcomeHomeChtholly {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].val = in.readInt();
        }

        int B = 250;
        Block[] blocks = new Block[DigitUtils.ceilDiv(n, B)];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new Block(nodes, i * B, Math.min(n, (i + 1) * B) - 1);
        }

        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            int x = in.readInt();
            if (t == 1) {
                for (Block b : blocks) {
                    b.update(l, r, x);
                }
            } else {
                int ans = 0;
                for (Block b : blocks) {
                    ans += b.query(l, r, x);
                }
                out.println(ans);
            }
        }
    }
}

class Block {
    Node[] level;
    int[] size;
    int high;
    Node[] nodes;
    int l;
    int r;
    int tag;

    public Block(Node[] nodes, int l, int r) {
        this.nodes = nodes;
        this.l = l;
        this.r = r;
        for (int i = l; i <= r; i++) {
            high = Math.max(nodes[i].val, high);
        }
        level = new Node[high + 1];
        size = new int[high + 1];
        install();
    }

    private void uninstall() {
        for (int i = l; i <= r; i++) {
            int val = nodes[i].val;
            if (level[val] == null) {
                continue;
            }
            level[val].prev.next = null;
            for (Node head = level[val]; head != null; head = head.next) {
                head.val = val + tag;
            }
            level[val] = null;
            size[val] = 0;
        }
        tag = 0;
    }

    private void install() {
        for (int i = l; i <= r; i++) {
            nodes[i].reset();
        }

        for (int i = l; i <= r; i++) {
            if (level[nodes[i].val] != null) {
                Node.merge(level[nodes[i].val], nodes[i]);
            }
            level[nodes[i].val] = nodes[i];
            size[nodes[i].val]++;
        }
    }

    private void move(int i, int j) {
        if (level[i] == null) {
            return;
        }
        if (level[j] != null) {
            Node.merge(level[i], level[j]);
        }
        level[j] = level[i];
        level[i] = null;
        level[j].val = j;
        size[j] += size[i];
        size[i] = 0;
    }

    public void adjust() {
        while (level[high] == null) {
            high--;
        }
    }

    public void update(int ll, int rr, int x) {
        ll = Math.max(ll, l);
        rr = Math.min(rr, r);
        if (rr < ll) {
            return;
        }
        if (ll != l || rr != r) {
            //not cover
            uninstall();
            for (int i = ll; i <= rr; i++) {
                if (nodes[i].val <= x) {
                    continue;
                }
                nodes[i].val -= x;
            }
            install();
            return;
        }

        adjust();
        //cover
        int mx = high + tag;
        if (mx <= x) {
            return;
        }

        if (x * 2 <= mx) {
            for (int i = 1 - tag; i + tag <= x; i++) {
                move(i, i + x);
            }
            tag -= x;
        } else {
            for (int i = high; i + tag > x; i--) {
                move(i, i - x);
            }
        }
    }

    public int query(int ll, int rr, int x) {
        ll = Math.max(ll, l);
        rr = Math.min(rr, r);
        if (rr < ll) {
            return 0;
        }
        if (ll != l || rr != r) {
            //not cover
            uninstall();
            int ans = 0;
            for (int i = ll; i <= rr; i++) {
                if (nodes[i].val == x) {
                    ans++;
                }
            }
            install();
            return ans;
        }

        adjust();
        //cover
        x -= tag;
        if (x >= 0 && x <= high) {
            return size[x];
        }
        return 0;
    }
}

class Node {
    Node next;
    Node prev;

    public void reset() {
        next = prev = this;
    }

    public static void merge(Node a, Node b) {
        Node aTail = a.prev;
        Node bTail = b.prev;
        a.prev = bTail;
        bTail.next = a;
        aTail.next = b;
        b.prev = aTail;
    }

    int val;
}
