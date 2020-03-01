package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.polynomial.GravityModLagrangeInterpolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FCowmpanyCowmpensation {
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].presum = new int[n + 1];
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[in.readInt() - 1];
            p.next.add(nodes[i]);
        }
        dfs(nodes[0]);
        GravityModLagrangeInterpolation interpolation = new GravityModLagrangeInterpolation(mod, n + 1);
        for(int i = 0; i < n + 1; i++){
            interpolation.addPoint(i + 1, nodes[0].presum[i]);
        }
        int ans = interpolation.getYByInterpolation(d);
        out.println(ans);
    }

    public void dfs(Node root) {
        Arrays.fill(root.presum, 1);
        for (Node node : root.next) {
            dfs(node);
            for(int i = 0; i < root.presum.length; i++){
                root.presum[i] = mod.mul(root.presum[i], node.presum[i]);
            }
        }
        for(int i = 1; i < root.presum.length; i++){
            root.presum[i] = mod.plus(root.presum[i], root.presum[i - 1]);
        }
    }

}

class Node {
    List<Node> next = new ArrayList<>();
    int[] presum;
}