package contest;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class DYouAreGivenATree {
    Node[] nodes;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[1], null);

        int sqrt = (int) Math.ceil(Math.sqrt(n));
        int[] ans = new int[n + 1];
        for (int i = 1; i < sqrt; i++) {
            out.println(solve(i));
        }

        int r = sqrt - 1;
        int l;
        while (r < n) {
            int since = r + 1;
            l = since;
            int val = solve(l);
            r = n;
            while(l < r){
                int mid = (l + r + 1) >> 1;
                if(solve(mid) == val){
                    l = mid;
                }else{
                    r = mid - 1;
                }
            }
            for(int i = since; i <= r; i++){
                out.println(val);
            }
        }
    }

    public int solve(int k) {
        prepare();
        dp(nodes[1], k);
        return count;
    }

    public void dfs(Node root, Node p) {
        root.next.remove(p);
        root.size = 1;
        for (Node node : root.next) {
            dfs(node, root);
            root.size += node.size;
        }
    }

    int count = 0;

    private void prepare() {
        count = 0;
    }

    public int dp(Node root, int k) {
        int max1 = 0;
        int max2 = 0;

        for (Node node : root.next) {
            int ans = dp(node, k);
            if (ans > max2) {
                max2 = ans;
            }
            if (max2 > max1) {
                int tmp = max1;
                max1 = max2;
                max2 = tmp;
            }
        }

        if (max1 + max2 + 1 >= k) {
            count++;
            return 0;
        }
        return max1 + 1;
    }

}

class Node {
    List<Node> next = new ArrayList<>();
    int size;
}