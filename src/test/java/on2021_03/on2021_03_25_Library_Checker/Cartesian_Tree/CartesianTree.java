package on2021_03.on2021_03_25_Library_Checker.Cartesian_Tree;



import template.graph.DescartesNode;
import template.io.FastInput;
import template.io.FastOutput;

public class CartesianTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        DescartesNode root = DescartesNode.build(0, n - 1, (i, j) -> Integer.compare(a[i], a[j]),
                DescartesNode::new);
        p = new int[n];
        dfs(root, root.index);
        for(int i = 0; i < n; i++){
            out.append(p[i]).append(' ');
        }
    }
    int[] p;
    public void dfs(DescartesNode root, int fa){
        if(root == null){
            return;
        }
        p[root.index] = fa;
        dfs(root.left, root.index);
        dfs(root.right, root.index);
    }
}
