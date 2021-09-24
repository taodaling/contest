package on2021_08.on2021_08_13_CS_Academy___Virtual_Beta_Round__7.Array_Coloring;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayColoring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        first = new int[m];
        last = new int[m];
        Arrays.fill(first, n);
        Arrays.fill(last, -1);
        a = in.ri(n);
        for (int i = 0; i < n; i++) {
            a[i]--;
        }
        for (int i = 0; i < n; i++) {
            first[a[i]] = Math.min(first[a[i]], i);
            last[a[i]] = Math.max(last[a[i]], i);
        }
        nodes = new Node[m];
        for (int i = 0; i < m; i++) {
            nodes[i] = new Node();
            nodes[i].c = i;
        }
        List<Node> collector = new ArrayList<>(n);
        parse(-1, collector, 0, n - 1);
        int addition = 0;
        for (int i = 0; i < m; i++) {
            if (first[i] > last[i]) {
                addition++;
            }
        }

        int maxCover = compute(collector);
        int ans = maxCover + addition;
        out.println(ans);
    }

    int compute(List<Node> all) {
        int maxCover = 0;
        int n = all.size();
        for (int i = 0; i < n; i++) {
            if (all.get(i) == null) {
                continue;
            }
            int l = i;
            int r = i;
            while (r + 1 < n && all.get(r + 1) != null) {
                r++;
            }
            i = r;
            for (int j = l; j <= r; j++) {
                Node root = all.get(j);
                root.maxCover = compute(root.adj);
                root.maxCover += r - l + 1;
                maxCover = Math.max(root.maxCover, maxCover);
            }
        }
        return maxCover;
    }

    public void parse(int fc, List<Node> children, int l, int r) {
        for (int i = l; i <= r; i++) {
            if (a[i] == fc) {
                children.add(null);
                continue;
            }
            int L = i;
            int R = last[a[i]];
            i = R;
            children.add(nodes[a[i]]);
            parse(a[i], nodes[a[i]].adj, L + 1, R - 1);
        }
    }

    Node[] nodes;
    int[] a;
    int[] first;
    int[] last;
}

class Node {
    int maxCover;
    int c;
    List<Node> adj = new ArrayList<>();
}