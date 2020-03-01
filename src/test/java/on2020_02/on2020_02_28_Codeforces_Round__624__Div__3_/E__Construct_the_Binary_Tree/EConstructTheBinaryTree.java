package on2020_02.on2020_02_28_Codeforces_Round__624__Div__3_.E__Construct_the_Binary_Tree;



import template.io.FastInput;
import template.io.FastOutput;

public class EConstructTheBinaryTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d = in.readInt();
        if (minH(n) > d || maxH(n) < d) {
            out.println("NO");
            return;
        }
        out.println("YES");
        nodes = new Node[n + 1];
        build(1, n, d);
        for(int i = 2; i <= n; i++){
            out.append(nodes[i].p.id).append(' ');
        }
        out.println();
    }

    Node[] nodes;
    public Node build(int l, int r, int d) {
        if(l > r){
            return null;
        }
        Node root = new Node();
        root.id = l;
        nodes[root.id] = root;
        d -= (r - l + 1) - 1;
        for (int i = l; i <= r; i++) {
            int left = (i - (l + 1) + 1);
            int right = r - i;
            if (minH(left) + minH(right) <= d &&
                    maxH(left) + maxH(right) >= d) {
                int leftAlloc = Math.max(minH(left), d - maxH(right));
                Node leftChild = build(l + 1, i, leftAlloc);
                Node rightChild = build(i + 1, r, d - leftAlloc);
                if(leftChild != null){
                    leftChild.p = root;
                }
                if(rightChild != null){
                    rightChild.p = root;
                }
                break;
            }
        }
        return root;
    }

    public int minH(int n) {
        int min = 0;
        int remain = n;
        for (int level = 0; remain > 0; level++) {
            int cnt = Math.min(1 << level, remain);
            remain -= cnt;
            min += cnt * level;
        }
        return min;
    }

    public int maxH(int n) {
        int max = (n - 1) * n / 2;
        return max;
    }
}

class Node {
    Node p;
    int id;
}