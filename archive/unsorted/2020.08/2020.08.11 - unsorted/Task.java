package contest;

import template.datastructure.PermutationNode;
import template.io.FastInput;
import template.io.FastOutput;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] perm  = new int[n];
        in.populate(perm);
        PermutationNode node = PermutationNode.build(perm);
        dfs(node);
        out.println(ans);
    }

    long ans = 0;
    public void dfs(PermutationNode root){
        ans++;
        if(root.join && !root.adj.isEmpty()){
            int n = root.adj.size();
            ans += n * (n - 1) / 2 - 1;
        }
        for(PermutationNode node : root.adj){
            dfs(node);
        }
    }
}
