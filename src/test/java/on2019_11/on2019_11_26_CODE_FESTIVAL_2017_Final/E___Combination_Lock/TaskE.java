package on2019_11.on2019_11_26_CODE_FESTIVAL_2017_Final.E___Combination_Lock;




import template.datastructure.LongHashMap;
import template.datastructure.LongObjectHashMap;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.string.ModifiableHash;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[100000];
        int len = in.readString(s, 0);
        int n = in.readInt();
        int[] differ = new int[len + 2];
        for (int i = 1; i <= len; i++) {
            if (i == 1) {
                differ[i] = s[i - 1] - 'a';
            } else {
                differ[i] = s[i - 1] - s[i - 2];
            }
        }
        differ[len + 1] = 'a' - s[len - 1];
        Node[] nodes = new Node[len + 2];
        for (int i = 0; i < len + 2; i++) {
            nodes[i] = new Node();
            nodes[i].val = differ[i];
        }

        for (int i = 0; i < n; i++) {
            int l = in.readInt();
            int r = in.readInt();
            Node.merge(nodes[l], nodes[r + 1]);
        }

        for (int i = 1; i <= (len + 2 + 1) / 2; i++) {
            Node.merge(nodes[i], nodes[len + 2 - i]);
        }

        int mod = 'z' - 'a' + 1;
        boolean allZero = true;
        for (int i = 0; i < len + 2; i++) {
            allZero = allZero && nodes[i].find().val % mod == 0;
        }
        out.println(allZero ? "YES" : "NO");
    }
}

class Node {
    Node p = this;
    int rank;
    int val;

    public Node find() {
        return p.p == p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank < b.rank) {
            Node tmp = a;
            a = b;
            b = tmp;
        }
        b.p = a;
        a.val += b.val;
    }
}