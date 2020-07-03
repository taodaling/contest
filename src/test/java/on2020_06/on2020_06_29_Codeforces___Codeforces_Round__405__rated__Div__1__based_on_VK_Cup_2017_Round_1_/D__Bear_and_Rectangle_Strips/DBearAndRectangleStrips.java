package on2020_06.on2020_06_29_Codeforces___Codeforces_Round__405__rated__Div__1__based_on_VK_Cup_2017_Round_1_.D__Bear_and_Rectangle_Strips;



import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;

import java.util.ArrayList;
import java.util.List;

public class DBearAndRectangleStrips {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] rows = new int[2][n + 1];
        int[] rowSum = new int[n + 1];
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= n; j++) {
                rows[i][j] = in.readInt();
                rowSum[i] += rows[i][j];
            }
        }

        int[] row1Last = findLast(rows[0]);
        int[] row2Last = findLast(rows[1]);
        sumLast = findLast(rowSum);
        int[] row1Next = findNext(rows[0]);
        int[] row2Next = findNext(rows[1]);

        handler1 = new Handler(buildTree(row1Next, 1, n + 1, n + 2), buildTree(row1Last, 0, n, -1));
        handler2 = new Handler(buildTree(row2Next, 1, n + 1, n + 2), buildTree(row2Last, 0, n, -1));

        dp = new int[n + 1];

    }

    int[] sumLast;
    Handler handler1;
    Handler handler2;
    int order = 0;
    int[] dp;

    public int get(int i) {
        int prev = sumLast[i - 1];
        if (prev < 0) {
            return 0;
        }
        return dp[prev] + 1;
    }

    public void dac(int ql, int qr, int sl, int sr) {
        if (ql > qr) {
            return;
        }
        if (sl == sr) {
            for (int i = ql; i <= ql; i++) {
                handler1.move(sl, i);
                handler2.move(sl, i);
                dp[i] = handler1.cnt + handler2.cnt + get(sl);
            }
            return;
        }

        int m = DigitUtils.floorAverage(ql, qr);
        dp[m] = 0;
        int choice = sl;
        for (int i = sl; i <= sr && i <= m + 1; i++) {
            handler1.move(i, m);
            handler2.move(i, m);

        }
    }

    Node[] buildTree(int[] p, int l, int r, int noP) {
        Node[] nodes = new Node[p.length];
        for (int i = 0; i < p.length; i++) {
            nodes[i] = new Node();
        }
        for (int i = l; i <= r; i++) {
            nodes[i] = new Node();
            if (p[i] == noP) {
                continue;
            }
            nodes[p[i]].adj.add(nodes[i]);
        }
        for (int i = l; i <= r; i++) {
            if (p[i] == noP) {
                dfs(nodes[i]);
            }
        }
        return nodes;
    }

    public void dfs(Node root) {
        root.l = root.r = order++;
        for (Node node : root.adj) {
            dfs(node);
            root.r = node.r;
        }
    }

    LongHashMap hm = new LongHashMap((int) 4e5, false);

    int[] findLast(int[] a) {
        hm.clear();
        hm.put(0, 0);
        long ps = 0;
        int[] last = new int[a.length];
        last[0] = -1;
        for (int i = 1; i < a.length; i++) {
            ps += a[i];
            if (hm.containKey(-ps)) {
                last[i] = (int) hm.get(-ps);
            } else {
                last[i] = -1;
            }
            hm.put(ps, i);
        }
        for (int i = 1; i < a.length; i++) {
            last[i] = Math.max(last[i - 1], last[i]);
        }

        return last;
    }

    int[] findNext(int[] a) {
        hm.clear();
        hm.put(0, a.length);
        int[] last = new int[a.length + 1];
        last[a.length] = a.length + 1;

        long ps = 0;
        for (int i = a.length - 1; i >= 1; i++) {
            ps += a[i];
            if (hm.containKey(-ps)) {
                last[i] = (int) hm.get(-ps);
            } else {
                last[i] = -1;
            }
            hm.put(ps, i);
        }
        for (int i = 1; i < a.length; i++) {
            last[i] = Math.max(last[i - 1], last[i]);
        }

        return last;
    }

}


class Node {
    List<Node> adj = new ArrayList<>();
    int l;
    int r;

    public boolean under(Node x) {
        return l >= x.l && r <= x.r;
    }
}

class Handler {
    Node[] l2r;
    Node[] r2l;
    int cnt;
    int l;
    int r;

    public Handler(Node[] l2r, Node[] r2l) {
        this.l2r = l2r;
        this.r2l = r2l;
        l = 1;
        r = 0;
    }

    public void move(int ll, int rr) {
        while (ll < l) {
            l--;
            if (r2l[r].under(r2l[l - 1])) {
                cnt++;
            }
        }
        while (rr > r) {
            r++;
            if (l2r[l].under(l2r[r + 1])) {
                cnt++;
            }
        }
        while (ll > l) {
            if (r2l[r].under(r2l[l - 1])) {
                cnt--;
            }
            l++;
        }
        while (rr < r) {
            if (l2r[l].under(l2r[r + 1])) {
                cnt--;
            }
            r--;
        }
    }

}