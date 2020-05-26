package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBinaryFunction;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerPriorityQueue;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FChristmasTree {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        for (int i = 0; i < n; i++) {
            nodes[i].children = new IntegerList(nodes[i].adj.size());
        }

        int A = blockNeed(nodes[0], null);

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                valid = true;
                dfs(nodes[0], null, mid);
                return valid;
            }
        };

        int B = ibs.binarySearch(0, n);

        out.append(A).append(' ').append(B);
    }

    boolean valid;

    public int blockNeed(Node root, Node p) {
        int child = 0;
        int ans = 0;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            child++;
            ans += blockNeed(node, root);
        }
        ans += child / 2;
        if (child % 2 == 1 && p == null) {
            ans++;
        }
        root.odd = p == null && child % 2 == 1 || p != null && child % 2 == 0;
        return ans;
    }

    private void add(TreeMap<Integer, Integer> map, Integer key) {
        map.put(key, map.getOrDefault(key, 0) + 1);
    }

    private void remove(TreeMap<Integer, Integer> map, Integer key) {
        int cnt = map.get(key);
        cnt--;
        if (cnt > 0) {
            map.put(key, cnt);
        } else {
            map.remove(key);
        }
    }

    public int dfs(Node root, Node p, int limit) {
        if (!valid) {
            return 0;
        }

        root.children.clear();
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            root.children.add(dfs(node, root, limit) + 1);
        }

        if (!valid) {
            return 0;
        }
        root.children.sort();
        int[] data = root.children.getData();
        int n = root.children.size();

        debug.debug("root", root);
        //special
        if (root.odd && p != null) {
            int l = 0;
            int r = n - 1;
            boolean skip = true;
            while (l < r && skip) {
                if (data[l] + data[r] > limit) {
                    skip = false;
                }
                l++;
                r--;
            }

            if (skip) {
                return 0;
            }
        }

        if (root.odd && n > 0) {
            if (data[n - 1] > limit) {
                valid = false;
                return 0;
            }
            n--;
        }

        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            add(map, data[i]);
        }

        Integer ret = null;
        while (!map.isEmpty()) {
            Integer max = map.lastKey();
            remove(map, max);
            Integer floor = map.floorKey(limit - max);
            if (floor == null) {
                if (ret == null) {
                    ret = max;
                    continue;
                }
                valid = false;
                return 0;
            }
            remove(map, floor);
        }
        return ret == null ? 0 : ret;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    IntegerList children;
    int id;
    boolean odd;
    IntegerPriorityQueue ipq;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
