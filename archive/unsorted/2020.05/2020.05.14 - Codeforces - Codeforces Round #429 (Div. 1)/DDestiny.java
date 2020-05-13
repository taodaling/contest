package contest;

import template.algo.MoOnArray;
import template.datastructure.CircularLinkedNode;
import template.datastructure.SparseTable;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerRMQ;
import template.primitve.generated.datastructure.IntegerSparseTable;

public class DDestiny {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt() - 1;
        }

        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].l = in.readInt() - 1;
            qs[i].r = in.readInt() - 1;
            qs[i].atLeast = (int) DigitUtils.minimumIntegerGreaterThanDiv(qs[i].r - qs[i].l + 1, in.readInt());
        }

        int[] cnts = new int[n];
        for (int i = 0; i < n; i++) {
            cnts[a[i]]++;
        }
        Handler handler = new Handler(n, cnts);
        MoOnArray.handle(a, qs.clone(), handler, 600);

        for (Query query : qs) {
            if (query.ans == -1) {
                out.println(-1);
                continue;
            }
            out.println(query.ans + 1);
        }
    }
}

class Node {
    Node prev;
    Node next;

    int cnt;
}

class Handler implements MoOnArray.IntHandler<Query> {
    Node[] nodes;
    Node[][] level;
    int[] max;
    int k = 600;

    public Handler(int n, int[] cnts) {
        nodes = new Node[n];
        max = new int[DigitUtils.ceilDiv(n, k)];
        level = new Node[DigitUtils.ceilDiv(n, k)][];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }

        for (int i = 0; i < level.length; i++) {
            int l = i * k;
            int r = Math.min(l + k - 1, n - 1);
            int max = 0;
            for(int j = l; j <= r; j++){
                max = Math.max(cnts[j], max);
            }
            level[i] = new Node[max + 1];

            for (int j = l; j <= r; j++) {
                attach(i, nodes[j]);
            }
        }
    }

    public void attach(int i, Node node) {
        node.next = level[i][node.cnt];
        if (level[i][node.cnt] != null) {
            level[i][node.cnt].prev = node;
        }
        level[i][node.cnt] = node;
    }

    public void detach(int i, Node node) {
        if (node.prev == null) {
            level[i][node.cnt] = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        node.prev = node.next = null;
    }

    @Override
    public void add(int i, int x) {
        int g = x / k;
        detach(g, nodes[x]);
        nodes[x].cnt++;
        attach(g, nodes[x]);
        max[g] = Math.max(max[g], nodes[x].cnt);
    }

    @Override
    public void remove(int i, int x) {
        int g = x / k;
        detach(g, nodes[x]);
        nodes[x].cnt--;
        attach(g, nodes[x]);
        if (max[g] == nodes[x].cnt + 1 && level[g][nodes[x].cnt + 1] == null) {
            max[g] = nodes[x].cnt;
        }
    }

    @Override
    public void answer(Query query) {
        int index = -1;
        for (int i = 0; i < max.length; i++) {
            if (max[i] >= query.atLeast) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            query.ans = -1;
            return;
        }

        for (int i = index * k; ; i++) {
            if (nodes[i].cnt >= query.atLeast) {
                query.ans = i;
                return;
            }
        }
    }
}

class Query implements MoOnArray.Query {
    int l;
    int r;
    int atLeast;
    int ans;

    @Override
    public int getL() {
        return l;
    }

    @Override
    public int getR() {
        return r;
    }
}
