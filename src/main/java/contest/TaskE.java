package contest;

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
        Node[] nodes = new Node[len];
        int mod = 'z' - 'a' + 1;
        for (int i = 0; i < len; i++) {
            nodes[i] = new Node();
            nodes[i].val = s[i] - 'a';
        }

        int n = in.readInt();
        Interval[] intervals = new Interval[n];
        for (int i = 0; i < n; i++) {
            intervals[i] = new Interval();
            intervals[i].id = i;
            intervals[i].l = in.readInt() - 1;
            intervals[i].r = in.readInt() - 1;
        }
        ModifiableHash h31 = new ModifiableHash(31, n);
        ModifiableHash h11 = new ModifiableHash(11, n);
        LongHashMap map = new LongHashMap(n * 2, true);

        Interval[] intervalsSortByL = intervals.clone();
        Interval[] intervalsSortByR = intervals.clone();
        Arrays.sort(intervalsSortByL, (a, b) -> a.l - b.l);
        Arrays.sort(intervalsSortByR, (a, b) -> a.r - b.r);
        int lHead = 0;
        int rHead = 0;
        for (int i = 0; i < len; i++) {
            while (lHead < n && intervalsSortByL[lHead].l <= i) {
                h31.modify(intervalsSortByL[lHead].id, 1);
                h11.modify(intervalsSortByL[lHead].id, 1);
                lHead++;
            }
            while (rHead < n && intervalsSortByR[rHead].r < i) {
                h31.modify(intervalsSortByR[rHead].id, -1);
                h11.modify(intervalsSortByR[rHead].id, -1);
                rHead++;
            }
            long state = DigitUtils.asLong(h11.hash(0, n - 1), h31.hash(0, n - 1));
            if (!map.containKey(state)) {
                map.put(state, i);
                continue;
            }
            int index = (int) map.get(state);
            Node.merge(nodes[index], nodes[i], nodes[i].val - nodes[index].val);
        }
        int l = 0;
        int r = len - 1;
        while (l < r) {
            Node a = nodes[l++];
            Node b = nodes[r--];
            if (a.find() == b.find()) {
                int differ = a.dist - b.dist;
                if (differ % mod != 0) {
                    out.println("NO");
                    return;
                }
                continue;
            }
            Node.merge(a.find(), b.find(), 0);
        }

        out.println("YES");
    }
}

class Interval {
    int l;
    int r;
    int id;
}

class Node {
    Node p = this;
    int rank = 0;
    int dist;
    int val;

    Node find() {
        if (p == p.p) {
            return p;
        }
        p.find();
        dist += p.dist;
        p = p.find();
        return p;
    }

    static void merge(Node a, Node b, int bSubA) {
        a.find();
        bSubA += a.dist;
        a = a.find();
        b.find();
        bSubA -= b.dist;
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
            bSubA = -bSubA;
        }
        b.p = a;
        b.dist = bSubA;
    }
}
