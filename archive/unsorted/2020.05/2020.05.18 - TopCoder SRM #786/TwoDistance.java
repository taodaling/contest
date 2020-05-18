package contest;

import template.utils.Debug;

import java.util.*;

public class TwoDistance {
    //Debug debug = new Debug(true);

    public long findMinValue(int N, int[] edge, int[] val, int D, int seed) {
        int[] A = new int[2 * N];
        A[0] = seed;
        for (int i = 1; i <= 2 * N - 1; i++) {
            A[i] = (int) (((long) A[i - 1] * 1103515245 + 12345) % 2147483648L);
        }
        int[] V = Arrays.copyOf(val, N);
        for (int i = val.length; i <= N - 1; i++) {
            V[i] = A[i];
        }


    //    debug.debug("V", V);

        int[] E = Arrays.copyOf(edge, N);
        for (int i = edge.length; i <= N - 1; i++) {
            E[i] = A[N + i] % Math.min(i, D);
        }

      //  debug.debug("E", E);

        Node[] nodes = new Node[N];
        for (int i = 0; i < N; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].val = V[i];
            if (i > 0) {
                Node p = nodes[E[i]];
                p.adj.add(nodes[i]);
                nodes[i].adj.add(p);
            }
        }

//        long[] bf = new long[N];
//        Arrays.fill(bf, (long) 1e18);
//        for (Node node : nodes) {
//            list.clear();
//            collectDepth2(node, null, 0);
//            list.sort((a, b) -> a.val.compareTo(b.val));
//            for (int i = 1; i < list.size(); i++) {
//                bf[node.id] = Math.min(bf[node.id], list.get(i).val - list.get(i - 1).val);
//            }
//        }

        dac(nodes[0]);

//        for (int i = 0; i < N; i++) {
//            if (nodes[i].cost != bf[i]) {
//                throw new RuntimeException();
//            }
//        }

        long sum = 0;
        for (Node node : nodes) {
            if (node.cost > Integer.MAX_VALUE) {
                continue;
            }
            sum += node.cost;
        }

        return sum;
    }

    public void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    public Node dfsForCentroid(Node root, Node p, int total) {
        int max = total - root.size;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            Node ans = dfsForCentroid(node, root, total);
            if (ans != null) {
                return ans;
            }
            max = Math.max(max, node.size);
        }
        if (max * 2 <= total) {
            return root;
        }
        return null;
    }

    List<Node> list = new ArrayList<>(200000);
    TreeMap<Integer, Integer> cntMap = new TreeMap<>();
    TreeMap<Integer, Integer> pieceMap = new TreeMap<>();

    public void addPiece(Integer x, int fix) {
        pieceMap.put(x, pieceMap.getOrDefault(x, 0) + fix);
    }

    public Integer minOfPiece() {
        while (!pieceMap.isEmpty()) {
            Map.Entry<Integer, Integer> first = pieceMap.firstEntry();
            if (first.getValue() != 0) {
                return first.getKey();
            }
            pieceMap.remove(first.getKey());
        }
        return null;
    }

    public void add(Integer x) {
        int cnt = cntMap.getOrDefault(x, 0);
        cnt++;

        if (cnt == 1) {
            Integer floor = cntMap.floorKey(x);
            Integer ceil = cntMap.ceilingKey(x);
            if (ceil != null && floor != null) {
                addPiece(ceil - floor, -1);
            }
            if (floor != null) {
                addPiece(x - floor, 1);
            }
            if (ceil != null) {
                addPiece(ceil - x, 1);
            }
        }

        cntMap.put(x, cnt);
    }

    public void remove(Integer x) {
        int cnt = cntMap.getOrDefault(x, x);
        cnt--;

        if (cnt == 0) {
            cntMap.remove(x);
            Integer floor = cntMap.floorKey(x);
            Integer ceil = cntMap.ceilingKey(x);
            if (ceil != null && floor != null) {
                addPiece(ceil - floor, 1);
            }
            if (floor != null) {
                addPiece(x - floor, -1);
            }
            if (ceil != null) {
                addPiece(ceil - x, -1);
            }
        } else {
            cntMap.put(x, cnt);
        }
    }

    public void collectDepth2(Node root, Node p, int d) {
        if (d == 2) {
            list.add(root);
            return;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            collectDepth2(node, root, d + 1);
        }
    }

    public void apply(List<Node> list) {
        for (Node node : list) {
            add(node.val);
        }
    }

    public void revoke(List<Node> list) {
        for (Node node : list) {
            remove(node.val);
        }
    }

    public void update(Node root) {
        Integer head = minOfPiece();
        if (head != null) {
            root.cost = Math.min(root.cost, head);
        }
    }

    public void dac(Node root) {
        dfsForSize(root, null);
        root = dfsForCentroid(root, null, root.size);
        list.clear();
        collectDepth2(root, null, 0);
        cntMap.clear();
        pieceMap.clear();
        apply(list);
        apply(root.cand);
        update(root);
        for (Node node : list) {
            node.cand.add(root);
        }

        cntMap.clear();
        pieceMap.clear();
        for (Node node : root.adj) {
            add(node.val);
        }

        for (Node node : root.adj) {
            remove(node.val);

            list.clear();
            collectDepth2(node, root, 0);

            apply(list);
            apply(node.cand);
            update(node);
            revoke(list);
            revoke(node.cand);

            add(node.val);
        }

        for (Node node : root.adj) {
            node.adj.remove(root);
            dac(node);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    Integer val;
    long cost = (long) 1e18;
    int size;
    int id;
    List<Node> cand = new ArrayList<>();

    @Override
    public String toString() {
        return "" + id;
    }
}
