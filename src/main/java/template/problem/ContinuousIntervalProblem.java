package template.problem;

import template.datastructure.PermutationNode;
import template.graph.DirectedEdge;
import template.graph.KthAncestorOnTreeByLongLink;
import template.graph.LcaOnTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>连续段问题，O(n)时间复杂度预处理一个0~n-1的排列。之后O(1)回答如下两类请求：</p>
 * <ul>
 *     <li>提供l,r,查询区间最小的区间[a,b],满足a<=l<=r<=b，且[a,b]区间是一个连续段</li>
 *     <li>提供l,r,统计有多少连续段[a,b]，满足l<=a<=b<=r</li>
 * </ul>
 */
public class ContinuousIntervalProblem {
    public ContinuousIntervalProblem(int[] perm) {
        int n = perm.length;
        int[] p = new int[n + 2];
        for (int i = 1; i <= n; i++) {
            p[i] = perm[i - 1] + 1;
        }
        p[n + 1] = n + 1;

        nodes = new ArrayList<>(2 * n);
        Node root = PermutationNode.build(p, () -> {
            Node node = new Node();
            node.id = nodes.size();
            nodes.add(node);
            return node;
        });
        leaf = new Node[n + 2];

        int m = nodes.size();
        List<DirectedEdge>[] g = new List[m];
        for (int i = 0; i < m; i++) {
            Node node = nodes.get(i);
            g[i] = node.adj.stream().map(x -> new DirectedEdge(((Node) x).id)).collect(Collectors.toList());
        }
        kthAncestorOnTreeByLongLink = new KthAncestorOnTreeByLongLink(g, root.id);
        lcaOnTree = new LcaOnTree(g, root.id);

        dfs(root, null, -1);
        dfs2(root);
    }

    public int[] findMinContinuousIntervalContains(int lId, int rId) {
        Node l = leaf[lId + 1];
        Node r = leaf[rId + 1];
        if (l == r) {
            return new int[]{l.ll - 1, l.rr - 1};
        }
        int lcaId = lcaOnTree.lca(l.id, r.id);
        Node ancestor = nodes.get(lcaId);
        if (!ancestor.join) {
            return new int[]{ancestor.ll - 1, ancestor.rr - 1};
        } else {
            l = nodes.get(kthAncestorOnTreeByLongLink.kthAncestor(l.id, l.depth - ancestor.depth - 1));
            r = nodes.get(kthAncestorOnTreeByLongLink.kthAncestor(r.id, r.depth - ancestor.depth - 1));
            return new int[]{l.ll - 1, r.rr - 1};
        }
    }

    public long countContinuousIntervalBetween(int l, int r) {
        if (l > r) {
            return 0;
        }
        if (l == r) {
            return 1;
        }
        r += 2;
        Node lNode = leaf[l];
        Node rNode = leaf[r];
        int lca = lcaOnTree.lca(lNode.id, rNode.id);
        Node lcaNode = nodes.get(lca);
        Node lParent = nodes.get(kthAncestorOnTreeByLongLink.kthAncestor(lNode.id, lNode.depth - lcaNode.depth - 1));
        Node rParent = nodes.get(kthAncestorOnTreeByLongLink.kthAncestor(rNode.id, rNode.depth - lcaNode.depth - 1));
        long ans = lNode.psA - lParent.psA;
        ans += rNode.psB - rParent.psB;
        int left = lParent.index + 1;
        int right = rParent.index - 1;
        ans += interval(lcaNode.ps, left, right);
        if (left <= right && lcaNode.join) {
            int cnt = right - left + 1;
            ans += (long) cnt * (cnt - 1) / 2;
        }
        return ans;
    }

    Node[] leaf;
    List<Node> nodes;
    KthAncestorOnTreeByLongLink kthAncestorOnTreeByLongLink;
    LcaOnTree lcaOnTree;

    private void dfs(Node root, Node p, int index) {
        if (root.length() == 1) {
            leaf[root.ll] = root;
        }
        root.depth = p == null ? 0 : p.depth + 1;
        root.p = p;
        root.index = index;
        root.c = 1;
        int n = root.adj.size();
        if (root.join && !root.adj.isEmpty()) {
            root.c += (long) n * (n - 1) / 2 - 1;
        }
        root.ps = new long[n];
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                root.ps[i] = root.ps[i - 1];
            }
            Node node = (Node) root.adj.get(i);
            dfs(node, root, i);
            root.c += node.c;
            root.ps[i] += node.c;
        }
    }

    private long interval(long[] ps, int l, int r) {
        r = Math.min(r, ps.length - 1);
        l = Math.max(l, 0);
        if (l > r) {
            return 0;
        }
        long ans = ps[r];
        if (l > 0) {
            ans -= ps[l - 1];
        }
        return ans;
    }

    private long choose2(long n) {
        return n * (n - 1) / 2;
    }

    private void dfs2(Node root) {
        if (root.p != null) {
            root.a = interval(root.p.ps, root.index + 1, root.p.ps.length - 1);
            root.b = interval(root.p.ps, 0, root.index - 1);
            if (root.p.join) {
                root.a += choose2(root.p.ps.length - root.index - 1);
                root.b += choose2(root.index);
            }
            root.psA = root.p.psA + root.a;
            root.psB = root.p.psB + root.b;
        }
        for (PermutationNode node : root.adj) {
            dfs2((Node) node);
        }
    }


    private static class Node extends PermutationNode {
        Node p;
        int depth;
        long[] ps;
        int index;
        long a;
        long b;
        long c;
        int id;

        long psA;
        long psB;
    }

}
