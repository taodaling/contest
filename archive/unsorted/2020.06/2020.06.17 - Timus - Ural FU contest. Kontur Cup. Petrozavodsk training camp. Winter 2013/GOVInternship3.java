package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class GOVInternship3 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int m = in.readInt();
        int[] b = new int[m];
        in.populate(b);

        Handler text = new Handler(a);
        Handler pattern = new Handler(b);

        //replace text
        for (int i = 0; i < n; i++) {
            if (a[i] != 0) {
                continue;
            }

            //n - 1 <=> m - 1
            int l = Math.max(0, (m - 1) - (n - 1 - i));
            //0 <=> 0
            int r = Math.min(i, m - 1);
            pattern.move(l, r);
            a[i] = pattern.findMax();
        }

        //replace pattern
        for (int i = 0; i < m; i++) {
            if (b[i] != 0) {
                continue;
            }
            int l = i;
            //n - 1 <=> m - 1
            //n - 1 - t <=> m - 1 - i
            int r = Math.min(n - 1, (n - 1) - (m - 1 - i));
            text.move(l, r);
            b[i] = text.findMax();
        }

        for(int x : a){
            out.append(x).append(' ');
        }
        out.println();
        for(int x : b){
            out.append(x).append(' ');
        }
    }
}

class Node {
    Node next;
    Node prev;
    int cnt;
    int id;
}

class Handler {
    int[] data;
    int l;
    int r;
    Node[] nodes;
    Node[] levels;
    static int limit = (int) 1e5;
    int top;

    private void detach(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            levels[node.cnt] = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        node.prev = node.next = null;
    }

    private void attach(Node node) {
        if (levels[node.cnt] != null) {
            levels[node.cnt].prev = node;
            node.next = levels[node.cnt];
        }
        levels[node.cnt] = node;
        top = Math.max(top, node.cnt);
    }

    private void modify(int i, int x) {
        detach(nodes[i]);
        nodes[i].cnt += x;
        attach(nodes[i]);
    }

    public int findMax() {
        while (levels[top] == null) {
            top--;
        }
        return levels[top].id;
    }

    public void move(int l, int r) {
        while (this.l > l) {
            this.l--;
            modify(data[this.l], 1);
        }
        while (this.r < r) {
            this.r++;
            modify(data[this.r], 1);
        }
        while (this.l < l) {
            modify(data[this.l], -1);
            this.l++;
        }
        while (this.r > r) {
            modify(data[this.r], -1);
            this.r--;
        }
    }

    public Handler(int[] data) {
        this.data = data;
        l = 0;
        r = -1;
        nodes = new Node[limit + 1];
        levels = new Node[limit + 1];
        for (int i = 1; i <= limit; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            if (i > 1) {
                nodes[i - 1].next = nodes[i];
                nodes[i].prev = nodes[i - 1];
            }
        }
        top = 0;
        levels[0] = nodes[1];
    }
}