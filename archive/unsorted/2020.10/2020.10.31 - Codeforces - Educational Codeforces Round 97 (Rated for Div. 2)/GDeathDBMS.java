package contest;

import template.datastructure.MultiSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.string.SAIS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GDeathDBMS {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        ACAutomaton ac = new ACAutomaton('a', 'z');

        int[] values = new int[n];
        MultiSet<Integer>[] sets = new MultiSet[n];
        Map<String, MultiSet<Integer>> map = new HashMap<>();
        int[] end = new int[n];
        for (int i = 0; i < n; i++) {
            String s = in.readString();
            ac.beginBuilding();
            for (int j = 0; j < s.length(); j++) {
                ac.build(s.charAt(j));
            }
            sets[i] = map.computeIfAbsent(s, x -> new MultiSet<>());
            sets[i].add(0);
            end[i] = ac.getBuildLast().getId();
        }

        ac.endBuilding();
        List<ACAutomaton.Node> list = ac.getAllNodes();
        int k = list.size();
        LCTNode[] nodes = new LCTNode[k];
        for (int i = 0; i < k; i++) {
            nodes[i] = new LCTNode();
        }
        for(int id : end){
            nodes[id].val = 0;
            nodes[id].pushUp();
        }
        for (ACAutomaton.Node node : list) {
            if (node.fail != null) {
                LCTNode.join(nodes[node.id], nodes[node.fail.id]);
            }
        }
        LCTNode.makeRoot(nodes[0]);
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            if (t == 1) {
                int which = in.readInt() - 1;
                int x = in.readInt();
                sets[which].remove(values[which]);
                values[which] = x;
                sets[which].add(x);
                LCTNode.splay(nodes[end[which]]);
                nodes[end[which]].val = sets[which].last();
                nodes[end[which]].pushUp();
            } else {
                ac.beginMatching();
                String q = in.readString();
                int ans = -1;
                for (char c : q.toCharArray()) {
                    ac.match(c);
                    LCTNode cur = nodes[ac.getMatchLast().id];
                    LCTNode.access(cur);
                    LCTNode.splay(cur);
                    ans = Math.max(ans, cur.max);
                }
                out.println(ans);
            }
        }
    }
}

class LCTNode {
    public static final LCTNode NIL = new LCTNode();

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.treeFather = NIL;
    }

    LCTNode left = NIL;
    LCTNode right = NIL;
    LCTNode father = NIL;
    LCTNode treeFather = NIL;
    boolean reverse;
    int id;
    int val = -1;
    int max = -1;

    public static void access(LCTNode x) {
        LCTNode last = NIL;
        while (x != NIL) {
            splay(x);
            x.right.father = NIL;
            x.right.treeFather = x;
            x.setRight(last);
            x.pushUp();

            last = x;
            x = x.treeFather;
        }
    }

    public static void makeRoot(LCTNode x) {
        access(x);
        splay(x);
        x.reverse();
    }

    public static void cut(LCTNode y, LCTNode x) {
        makeRoot(y);
        access(x);
        splay(y);
        y.right.treeFather = NIL;
        y.right.father = NIL;
        y.setRight(NIL);
        y.pushUp();
    }

    public static void join(LCTNode y, LCTNode x) {
        makeRoot(x);
        x.treeFather = y;
    }

    public static void findRoute(LCTNode x, LCTNode y) {
        makeRoot(y);
        access(x);
    }

    public static void splay(LCTNode x) {
        if (x == NIL) {
            return;
        }
        LCTNode y, z;
        while ((y = x.father) != NIL) {
            if ((z = y.father) == NIL) {
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    zig(x);
                } else {
                    zag(x);
                }
            } else {
                z.pushDown();
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    if (y == z.left) {
                        zig(y);
                        zig(x);
                    } else {
                        zig(x);
                        zag(x);
                    }
                } else {
                    if (y == z.left) {
                        zag(x);
                        zig(x);
                    } else {
                        zag(y);
                        zag(x);
                    }
                }
            }
        }

        x.pushDown();
        x.pushUp();
    }

    public static void zig(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.right;

        y.setLeft(b);
        x.setRight(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static void zag(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.left;

        y.setRight(b);
        x.setLeft(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static LCTNode findRoot(LCTNode x) {
        splay(x);
        x.pushDown();
        while (x.left != NIL) {
            x = x.left;
            x.pushDown();
        }
        splay(x);
        return x;
    }

    @Override
    public String toString() {
        return "" + id;
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        if (reverse) {
            reverse = false;

            LCTNode tmpNode = left;
            left = right;
            right = tmpNode;

            left.reverse();
            right.reverse();
        }

        left.treeFather = treeFather;
        right.treeFather = treeFather;
    }

    public void reverse() {
        reverse = !reverse;
    }

    public void setLeft(LCTNode x) {
        left = x;
        x.father = this;
    }

    public void setRight(LCTNode x) {
        right = x;
        x.father = this;
    }

    public void changeChild(LCTNode y, LCTNode x) {
        if (left == y) {
            setLeft(x);
        } else {
            setRight(x);
        }
    }

    public void pushUp() {
        if (this == NIL) {
            return;
        }
        max = Math.max(left.max, right.max);
        max = Math.max(max, val);
    }
}
