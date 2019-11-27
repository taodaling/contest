package contest;

import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Hash;

import java.util.*;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        List<Node> list = new ArrayList<>();
        dfs(nodes[1], null);
        dfsForCenter(nodes[1], null, n, list);
        if (list.size() != 1) {
            out.println(-1);
            return;
        }

        Map<List<Integer>, Integer> cntMap = new HashMap<>();
        Node center = list.get(0);
        for (Node node : center.next) {
            node.next.remove(center);
            handle(node, cntMap);
        }

        boolean exist = false;
        for (int value : cntMap.values()) {
            if (value == center.next.size()) {
                exist = true;
                break;
            }
        }

        if (exist) {
            out.println(center.next.size());
        } else {
            out.println(-1);
        }
    }

    private List<Hash> hashes = Arrays.asList(new Hash(10000, 31), new Hash(10000, 11), new Hash(10000, 41));

    public void handle(Node root, Map<List<Integer>, Integer> cntMap) {
        dfs(root, null);
        List<Node> centers = new ArrayList<>();
        dfsForCenter(root, null, root.size, centers);

        Set<List<Integer>> hashValue = new HashSet<>();
        for (Node node : centers) {
            List<Integer> hash = new ArrayList<>();
            for (Hash h : hashes) {
                hash.add(dfsForHash(node, null, h));
            }
            hashValue.add(hash);
        }

        for (List<Integer> key : hashValue) {
            cntMap.put(key, cntMap.getOrDefault(key, 0) + 1);
        }
    }

    public int compare(List<Integer> a, List<Integer> b) {
        int n = a.size();
        int m = b.size();
        if (n != m) {
            return n - m;
        }
        for (int i = 0; i < n; i++) {
            int ae = a.get(i);
            int be = b.get(i);
            if (ae != be) {
                return Integer.compare(ae, be);
            }
        }
        return 0;
    }

    public int dfsForHash(Node root, Node p, Hash h) {
        List<Integer> children = new ArrayList<>();
        for (Node node : root.next) {
            if(node == p){
                continue;
            }
            children.add(dfsForHash(node, root, h));
        }
        children.sort(Comparator.naturalOrder());
        int[] data = new int[children.size() + 1];
        data[0] = 1;
        for (int i = 1; i < data.length; i++) {
            data[i] = children.get(i - 1);
        }
        h.populate(data, data.length);
        return h.hashVerbose(0, data.length - 1);
    }

    public void dfsForCenter(Node root, Node p, int total, List<Node> ans) {
        int max = total - root.size;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            max = Math.max(max, node.size);
            dfsForCenter(node, root, total, ans);
        }
        if (max <= total / 2) {
            ans.add(root);
        }
    }

    public void dfs(Node root, Node p) {
        root.size = 1;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.size += node.size;
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int size;
}
