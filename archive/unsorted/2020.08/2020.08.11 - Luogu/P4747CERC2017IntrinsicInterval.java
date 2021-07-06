package contest;

import dp.Lis;
import graphs.lca.Lca;
import template.datastructure.PermutationNode;
import template.graph.DirectedEdge;
import template.graph.KthAncestorOnTree;
import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Permutation;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class P4747CERC2017IntrinsicInterval {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] perm = new int[n];
        in.populate(perm);
        for (int i = 0; i < n; i++) {
            perm[i]--;
        }
        PermutationNode root = PermutationNode.build(perm);
        dfs(root, 0);

        List<DirectedEdge>[] tree = new List[map.size()];
        for (Map.Entry<PermutationNode, Integer> entry : map.entrySet()) {
            int id = entry.getValue();
            PermutationNode node = entry.getKey();
            tree[id] = node.adj.stream().map(x ->
                    new DirectedEdge(map.get(x))).collect(Collectors.toList());
        }

        KthAncestorOnTree kthAncestorOnTree = new KthAncestorOnTree(tree, map.get(root));
        LcaOnTree lca = new LcaOnTree(tree, map.get(root));

        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            PermutationNode l = list.get(in.readInt() - 1);
            PermutationNode r = list.get(in.readInt() - 1);
            if(l == r){
                out.append(l.ll + 1).append(' ').append(l.rr + 1).println();
                continue;
            }
            int lId = map.get(l);
            int rId = map.get(r);
            int lcaId = lca.lca(lId, rId);
            PermutationNode ancestor = idToNode.get(lcaId);
            if (!ancestor.join) {
                out.append(ancestor.ll + 1).append(' ').append(ancestor.rr + 1).println();
            } else {
                int dAncestor = depths.get(lcaId);
                int dl = depths.get(lId);
                int dr = depths.get(rId);
                l = idToNode.get(kthAncestorOnTree.kthAncestor(lId, dl - dAncestor - 1));
                r = idToNode.get(kthAncestorOnTree.kthAncestor(rId, dr - dAncestor - 1));
                out.append(l.ll + 1).append(' ').append(r.rr + 1).println();
            }
        }
    }

    Map<PermutationNode, Integer> map = new HashMap<>();
    List<PermutationNode> list = new ArrayList<>();
    List<PermutationNode> idToNode = new ArrayList<>();
    List<Integer> depths = new ArrayList<>();

    public void dfs(PermutationNode root, int d) {
        if (root.length() == 1) {
            list.add(root);
        }
        map.put(root, map.size());
        idToNode.add(root);
        depths.add(d);
        for (PermutationNode node : root.adj) {
            dfs(node, d + 1);
        }
    }
}
