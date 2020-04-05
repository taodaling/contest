package on2020_04.on2020_04_05_Codeforces_Round__631__Div__1____Thanks__Denis_aramis_Shitov_.D__Dreamoon_Likes_Strings;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class DDreamoonLikesStrings {
    char[] s = new char[200001];

    int charset = 'z' - 'a' + 1;

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readString(s, 1);

        TreeSet<Node>[] sets = new TreeSet[charset];
        for (int i = 0; i < charset; i++) {
            sets[i] = new TreeSet<>();
        }

        Node[] nodes = new Node[n];
        for (int i = 1; i < n; i++) {
            if (s[i] != s[i + 1]) {
                continue;
            }
            nodes[i] = new Node();
            nodes[i].index = i;
            sets[s[i] - 'a'].add(nodes[i]);
        }

        Node pre = null;
        for (int i = 1; i < n; i++) {
            if (nodes[i] == null) {
                continue;
            }
            nodes[i].pre = pre;
            if (pre != null) {
                pre.next = nodes[i];
            }
            pre = nodes[i];
        }

        bit = new IntegerBIT(n);
        List<int[]> seq = new ArrayList<>(n);
        while (true) {
            int largestIndex = 0;
            TreeSet<Node> largest = sets[0];
            for (int i = 0; i < charset; i++) {
                if (sets[i].size() > largest.size()) {
                    largest = sets[i];
                    largestIndex = i;
                }
            }

            debug.debug("largestIndex", largestIndex);
            if (largest.size() == 0) {
                seq.add(new int[]{1, indexOf(n)});
                break;
            }
            Node head = largest.first();
            Node a = null;
            Node b = null;
            if (head.pre != null) {
                a = head.pre;
                b = head;
            } else {
                while (head.find().tail.next != null && s[head.index] == s[head.find().tail.next.index]) {
                    head = head.find().tail.next;
                    Node.merge(head.pre, head);
                }

                //only one color
                if (head.find().tail.next == null) {
                    head = head.find().tail;

                    int[] ans = new int[]{indexOf(head.index + 1), indexOf(n)};
                    seq.add(ans);
                    head.find().tail = head.pre;
                    if(head.pre != null) {
                        head.pre.next = null;
                    }
                    largest.remove(head);
                    bit.update(head.index + 1, length(ans));
                    continue;
                } else {
                    a = head.find().tail;
                    b = a.next;
                }
            }

            int[] ans = new int[]{indexOf(a.index + 1), indexOf(b.index)};
            seq.add(ans);
            bit.update(a.index + 1, length(ans));
            a.find().tail = a.pre;
            if (a.pre != null) {
                a.pre.next = b.next;
            }
            if (b.next != null) {
                b.next.pre = a.pre;
            }
            sets[s[a.index] - 'a'].remove(a);
            sets[s[b.index] - 'a'].remove(b);
        }

        out.println(seq.size());
        for (int[] lr : seq) {
            out.append(lr[0]).append(' ').append(lr[1]).println();
        }
    }


    public int length(int[] interval){
        return interval[1] - interval[0] + 1;
    }

    IntegerBIT bit;

    public int indexOf(int i) {
        return i - bit.query(i);
    }
}

class Node implements Comparable<Node> {
    Node pre;
    Node next;

    int index;

    Node p = this;
    int rank;
    Node tail = this;

    Node find() {
        return p.p == p ? p : (p = p.find());
    }

    static Node larger(Node a, Node b) {
        return a.index < b.index ? b : a;
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
        if (a.rank > b.rank) {
            b.p = a;
            a.tail = larger(a.tail, b.tail);
        } else {
            a.p = b;
            b.tail = larger(a.tail, b.tail);
        }
    }

    @Override
    public int compareTo(Node o) {
        return index - o.index;
    }

    @Override
    public String toString() {
        return "" + index;
    }
}
