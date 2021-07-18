package on2021_07.on2021_07_16_DMOJ___Wesley_s_Anger_Contest_5.Problem_4___Prison_Escape;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class Problem4PrisonEscape {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
//        LCTNode[] nodes = new LCTNode[n];
//        for (int i = 0; i < n; i++) {
//            nodes[i] = new LCTNode();
//            nodes[i].id = i;
//            nodes[i].pushUp();
//        }
        for (int i = 0; i < n - 1; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            Graph.addUndirectedEdge(g, a, b);
//            LCTNode.join(nodes[a], nodes[b]);
        }
        ParentOnTree pot = new ParentOnTreeByDfs(g, i -> i == 0);
        DepthOnTree dot = new DepthOnTreeByParent(n, pot);
        CompressedBinaryLift lca = new CompressedBinaryLift(n, dot, pot);
        TreePathImpl ab = new TreePathImpl(dot, lca, lca);
        TreePathImpl cd = new TreePathImpl(dot, lca, lca);
        TreePathImpl ac = new TreePathImpl(dot, lca, lca);
        DistanceOnTree distOnTree = new DistanceOnTreeByLcaAndDepth(lca, dot);
        for (int i = 1; i <= q; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.ri() - 1;
            int d = in.ri() - 1;

//            LCTNode.findRoute(nodes[c], nodes[d]);
//            LCTNode.splay(nodes[c]);
//            nodes[c].modify(i);
//            LCTNode.findRoute(nodes[a], nodes[b]);
//            LCTNode.splay(nodes[a]);
//            LCTNode first = search(nodes[a], i);
//            if (first == null) {
//                out.println(-1);
//                continue;
//            }
            ac.init(a, c);
            if (ac.length() % 2 != 0) {
                out.println(-1);
                continue;
            }
            int meet = ac.kthNodeOnPath(ac.length() / 2);
            ab.init(a, b);
            cd.init(c, d);
            if (ab.onPath(meet) && cd.onPath(meet) && meet != b && meet != d) {
                out.println(ac.length() / 2);
            }else{
                out.println(-1);
            }
        }
    }

//    public LCTNode search(LCTNode root, int time) {
//        if (root.subtreeTime != time) {
//            return null;
//        }
//        while (true) {
//            root.pushDown();
//            if (root.left.subtreeTime == time) {
//                root = root.left;
//            } else if (root.localTime == time) {
//                break;
//            } else {
//                root = root.right;
//            }
//        }
//        LCTNode.splay(root);
//        return root;
//    }
}
