package on2020_09.on2020_09_17_Codeforces___ICM_Technex_2017_and_Codeforces_Round__400__Div__1___Div__2__combined_.F__Sherlock_s_bet_to_Moriarty;



import template.binary.Bits;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongObjectHashMap;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FSherlocksBetToMoriarty {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        if (m == 0) {
            out.println(1);
            return;
        }

        int[][] cuts = new int[m][3];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                cuts[i][j] = in.readInt() - 1;
            }
            int u = cuts[i][0];
            int v = cuts[i][1];
            if (u > v) {
                int tmp = u;
                u = v;
                v = tmp;
            }
            cuts[i][2] = Math.min(v - u + 1, n + 2 - (v - u + 1));
        }
        Arrays.sort(cuts, (a, b) -> -Integer.compare(a[2], b[2]));
        LinkedNode[] linkNodes = new LinkedNode[n];
        for (int i = 0; i < n; i++) {
            linkNodes[i] = new LinkedNode();
            linkNodes[i].val = i;
        }

        for (int i = 0; i < n; i++) {
            linkNodes[i].prev = linkNodes[(i - 1 + n) % n];
            linkNodes[i].next = linkNodes[(i + 1) % n];
        }

        List<Node> nodes = new ArrayList<>(m + 1);
        for (int i = m - 1; i >= 0; i--) {
            int a = cuts[i][0];
            int b = cuts[i][1];
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            LinkedNode u = linkNodes[a];
            LinkedNode v = linkNodes[b];
            if (i == 0) {
                nodes.add(makeNode(u, v));
                nodes.add(makeNode(v, u));
                continue;
            }

            int na = cuts[i - 1][0];
            int nb = cuts[i - 1][1];
            if (na > nb) {
                int tmp = na;
                na = nb;
                nb = tmp;
            }
            LinkedNode cutFrom = u;
            LinkedNode cutTo = v;
            if (na >= a && nb <= b) {
                //inner
                cutFrom = v;
                cutTo = u;
            }
            nodes.add(makeNode(cutFrom, cutTo));
        }

        for (Node node : nodes) {
            int max1 = 0;
            int max2 = 0;
            int max3 = 0;
            for (int x : node.pts) {
                max1 = Math.max(max1, x);
            }
            for (int x : node.pts) {
                if (x < max1) {
                    max2 = Math.max(max2, x);
                }
            }
            for (int x : node.pts) {
                if (x < max2) {
                    max3 = Math.max(max3, x);
                }
            }
            node.sign = max1 * (long) (1e12) + max2 * (long) 1e6 + max3;
        }

        nodes.sort((a, b) -> Long.compare(a.sign, b.sign));
        int curId = 0;
        for(Node node : nodes){
            node.id = curId++;
        }
        LongObjectHashMap<Node> map = new LongObjectHashMap<>((int) 1e6, false);
        for (Node node : nodes) {
            int len = node.pts.length;
            for (int i = 0; i < len; i++) {
                int a = node.pts[i];
                int b = node.pts[(i + 1) % len];
                if (a > b) {
                    int tmp = a;
                    a = b;
                    b = tmp;
                }
                long key = DigitUtils.asLong(a, b);
                Node other = map.get(key);
                if (other == null) {
                    map.put(key, node);
                } else {
                    node.adj.add(other);
                    other.adj.add(node);
                    debug.debug("u", node.id);
                    debug.debug("v", other.id);
                }
            }
        }

        Node root = nodes.get(0);
        dfs(root, null);

        for (Node node : nodes) {
            int ans = node.alloc;
            out.println(ans);
        }
    }


    public static int merge(int a, int b) {
        int ans = a | b;
        int highestOneBit = Integer.highestOneBit(a & b);
        while (highestOneBit > 0) {
            highestOneBit >>= 1;
            ans |= highestOneBit;
        }
        return ans;
    }


    public static int dfs(Node root, Node p) {
        root.size = 1;
        int sum = 0;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            sum = merge(sum, dfs(node, root));
            root.size += node.size;
        }
        if (sum + 1 > root.size) {
            throw new RuntimeException();
        }
        root.alloc = 20 - Log2.floorLog((sum + 1) ^ sum);
        return sum + 1;
    }

    public Node makeNode(LinkedNode u, LinkedNode v) {
        Node ans = new Node();
        ans.pts = new int[estimate(u, v) + 2];
        Consumer<LinkedNode> p1Consumer = new Consumer<LinkedNode>() {
            int wpos = 0;

            @Override
            public void accept(LinkedNode o) {
                ans.pts[wpos++] = o.val;
            }
        };
        p1Consumer.accept(u);
        detach(u, v, p1Consumer);
        p1Consumer.accept(v);
        return ans;
    }

    public int estimate(LinkedNode a, LinkedNode end) {
        int size = 0;
        for (LinkedNode cur = a.next; cur != end; cur = cur.next) {
            size++;
        }
        return size;
    }

    public void detach(LinkedNode begin, LinkedNode end, Consumer<LinkedNode> consumer) {
        while (begin.next != end) {
            LinkedNode next = begin.next;
            next.detach();
            consumer.accept(next);
        }
    }
}

class Node {
    int[] pts;
    long sign;
    List<Node> adj = new ArrayList<>();
    int size;
    int alloc;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}

class LinkedNode {
    LinkedNode prev;
    LinkedNode next;
    int val;

    public void detach() {
        prev.next = next;
        next.prev = prev;
        prev = null;
        next = null;
    }

    @Override
    public String toString() {
        return "" + val;
    }
}