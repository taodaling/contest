package on2020_09.on2020_09_29_Codeforces___Divide_by_Zero_2017_and_Codeforces_Round__399__Div__1___Div__2__combined_.G__The_Winds_of_Winter;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class GTheWindsOfWinter {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        Node root = null;
        for (int i = 0; i < n; i++) {
            int pId = in.readInt() - 1;
            Node b = nodes[in.readInt() - 1];
            if (pId == -1) {
                root = b;
                continue;
            }
            Node a = nodes[pId];
            a.adj.add(b);
            b.adj.add(a);
        }

        if (n == 1) {
            out.println(0);
            return;
        }


        assert root != null;
        dfsForSize(root, null);
        for (Node node : nodes) {
            add(top, node.size);
        }

        dfsForSolve(root, null, false);

        for (Node node : nodes) {
            out.println(node.ans);
        }
    }

    int n;
    TreeMap<Integer, Integer> top = new TreeMap<>();
    TreeMap<Integer, Integer> stack = new TreeMap<>();
    TreeMap<Integer, Integer> bot = new TreeMap<>();

    public void add(TreeMap<Integer, Integer> map, int x) {
        int cnt = map.getOrDefault(x, 0) + 1;
        map.put(x, cnt);
    }

    public void delete(TreeMap<Integer, Integer> map, int x) {
        int cnt = map.getOrDefault(x, 0) - 1;
        if (cnt == 0) {
            map.remove(x);
        } else {
            map.put(x, cnt);
        }
    }

    public void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
            if (root.heavy == null || root.heavy.size < node.size) {
                root.heavy = node;
            }
        }
    }

    public void dfsForUndo(Node root, Node p) {
        delete(bot, root.size);
        add(top, root.size);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForUndo(node, root);
        }
    }

    public void dfsForDo(Node root, Node p) {
        add(bot, root.size);
        delete(top, root.size);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForDo(node, root);
        }
    }


    IntegerArrayList sizeList = new IntegerArrayList((int) 1e5);

    public int optimal(TreeMap<Integer, Integer> structure, TreeMap<Integer, Integer> extra, int rootSize) {
        if (sizeList.size() == 1) {
            return sizeList.tail();
        }
        int secondMax = sizeList.get(sizeList.size() - 2);
        int max = sizeList.tail();
        int min = sizeList.first();
        //\min(min+d, max-d),

        int half = (max - min) / 2;
        Integer floor = structure.floorKey(half);
        Integer ceil = structure.ceilingKey(half);

        int ans = max;
        if (floor != null) {
            ans = Math.min(ans, Math.max(Math.max(max - floor, min + floor), secondMax));
        }
        if (ceil != null && ceil != max) {
            ans = Math.min(ans, Math.max(Math.max(max - ceil, min + ceil), secondMax));
        }

        if (extra != null) {
            floor = extra.floorKey(half + rootSize);
            ceil = extra.ceilingKey(half + rootSize);

            if (floor != null) {
                floor -= rootSize;
                ans = Math.min(ans, Math.max(Math.max(max - floor, min + floor), secondMax));
            }
            if (ceil != null && ceil != max) {
                ceil -= rootSize;
                ans = Math.min(ans, Math.max(Math.max(max - ceil, min + ceil), secondMax));
            }
        }
        return ans;
    }


    public void dfsForSolve(Node root, Node p, boolean undo) {
        delete(top, root.size);
        add(stack, root.size);

        for (Node node : root.adj) {
            if (node == p || node == root.heavy) {
                continue;
            }
            dfsForSolve(node, root, true);
            assert bot.isEmpty();
        }

        if (root.heavy != null) {
            dfsForSolve(root.heavy, root, false);
        }

        add(top, root.size);
        delete(stack, root.size);

        sizeList.clear();
        for (Node node : root.adj) {
            if (node == p) {
                sizeList.add(n - root.size);
            } else {
                sizeList.add(node.size);
            }
        }
        sizeList.sort();

        root.ans = sizeList.tail();
        if (root.heavy != null && root.heavy.size == sizeList.tail()) {
            root.ans = Math.min(root.ans, optimal(bot, null, root.size));
        }

        for (Node node : root.adj) {
            if (node == p || node == root.heavy) {
                continue;
            }
            dfsForDo(node, root);
        }
        add(bot, root.size);
        delete(top, root.size);

        if (n - root.size == sizeList.tail()) {
            root.ans = Math.min(root.ans, optimal(top, stack, root.size));
        }

        if (undo) {
            dfsForUndo(root, p);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int size;
    Node heavy;
    int ans;

    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}


