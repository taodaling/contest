package contest;

import template.FastInput;
import template.FastOutput;
import template.Hash;

public class StringsofEternity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        StringBuilder builder = new StringBuilder(1000000);
        in.readString(builder);
        int tLen = builder.length();

        char[] s = new char[500000];
        int sLen = in.readString(s, 0);

        builder.append(builder);
        while (s.length + tLen > builder.length()) {
            builder.append(builder);
        }
        char[] t = new char[builder.length()];
        builder.getChars(0, builder.length(), t, 0);


        Hash s31 = new Hash(sLen, 31);
        Hash s61 = new Hash(sLen, 61);
        Hash t31 = new Hash(t.length, 31);
        Hash t61 = new Hash(t.length, 61);

        s31.populate(s, sLen);
        s61.populate(s, sLen);
        t31.populate(t, t.length);
        t61.populate(t, t.length);

        Node[] nodes = new Node[tLen];
        for (int i = 0; i < tLen; i++) {
            if ((t31.partial(i, i + sLen - 1) == s31.partial(0, sLen - 1)
                            && t61.partial(i, i + sLen - 1) == s61.partial(0, sLen - 1))) {
                nodes[i] = new Node();
            }

        }

        for (int i = 0; i < tLen; i++) {
            if (nodes[i] == null) {
                continue;
            }
            nodes[i].next = nodes[(i + sLen) % tLen];
        }

        int ans = 0;
        for (int i = 0; i < tLen; i++) {
            if (nodes[i] != null) {
                dfs(nodes[i]);
                ans = Math.max(ans, nodes[i].dp);
            }
        }

        if (circle) {
            out.println(-1);
        } else {
            out.println(ans);
        }
    }

    boolean circle;

    public void dfs(Node root) {
        if (root.visited) {
            if (root.instk) {
                circle = true;
            }
            return;
        }
        root.visited = true;
        root.instk = true;
        root.dp = 1;
        if (root.next != null) {
            dfs(root.next);
            root.dp += root.next.dp;
        }
        root.instk = false;
    }
}


class Node {
    Node next;
    boolean visited;
    int dp;
    boolean instk;
}
