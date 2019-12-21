package contest;

import template.FastInput;
import template.FastOutput;
import template.IntegerList;

public class TaskE {
    int n;
    long l;

    int[] cnts = new int[200000];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        l = in.readLong();

        char[] seq = new char[100000];
        IntegerList list = new IntegerList(100000);

        Node root = new Node();
        for (int i = 0; i < n; i++) {
            int k = in.readString(seq, 0);
            list.clear();
            for (int j = 0; j < k; j++) {
                list.add(seq[j] - '0');
            }
            addNode(root, list, 0);
        }

        dfs(root);

        long xor = 0;
        for (int i = 1; i < 200000; i++) {
            if(cnts[i] % 2 != cnts[i - 1] % 2){
                //this should be one
                xor ^= l + 1 - i;
            }
        }

        out.println(xor == 0 ? "Bob" : "Alice");
    }

    public void dfs(Node root) {
        if (root == null) {
            return;
        }
        if (root.tag) {
            return;
        }
        if (root.depth < l) {
            if (root.next[0] == null) {
                cnts[root.depth + 1]++;
            }
            if (root.next[1] == null) {
                cnts[root.depth + 1]++;
            }
        }
        dfs(root.next[0]);
        dfs(root.next[1]);
    }

    public void addNode(Node root, IntegerList seq, int i) {
        if (seq.size() == i) {
            root.tag = true;
            return;
        }
        addNode(root.get(seq.get(i)), seq, i + 1);
    }
}


class Node {
    Node[] next = new Node[2];
    boolean tag;
    int depth;

    public Node get(int i) {
        if (next[i] == null) {
            next[i] = new Node();
            next[i].depth = depth + 1;
        }
        return next[i];
    }
}
