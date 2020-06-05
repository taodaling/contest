package on2020_06.on2020_06_05_Codeforces___Codeforces_Round__647__Div__1____Thanks__Algo_Muse_.D__Johnny_and_James;



import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class DJohnnyAndJames {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();


        TreeMap<IntegerPoint2, List<Node>> map = new TreeMap<>(IntegerPoint2.SORT_BY_POLAR_ANGLE);

        Node center = null;
        for (int i = 0; i < n; i++) {
            Node node = new Node();
            node.pt = new IntegerPoint2(in.readInt(), in.readInt());
            node.d = node.pt.abs();
            if (node.pt.x == 0 && node.pt.y == 0) {
                center = node;
            } else {
                map.computeIfAbsent(node.pt, x -> new ArrayList<>()).add(node);
            }
        }

        debug.debug("map", map);

        PriorityQueue<Node> pq = new PriorityQueue<>(map.size(), (a, b) -> -Double.compare(a.weight, b.weight));
        for (List<Node> list : map.values()) {
            list.sort((a, b) -> Double.compare(a.d, b.d));
            Node last = null;
            for (int i = 0; i < list.size(); i++) {
                Node node = list.get(i);
                node.after = list.size() - i - 1;
                node.weight = node.d * (k - 2 * node.after - 1);
                node.p = last;
                last = node;
            }
            pq.add(list.get(list.size() - 1));
        }

        int threshold = k / 2;
        double ans1 = 0;
        int got = 0;
        while (got < k - 1 && !pq.isEmpty()) {
            got++;
            Node head = pq.remove();
            ans1 += head.weight;
            if (head.p != null && head.p.after < threshold) {
                pq.add(head.p);
            }
        }

        if (!pq.isEmpty() && pq.peek().weight > 0) {
            ans1 += pq.peek().weight;
        }

        if (got < k - 1) {
            ans1 = -1;
        }

        boolean valid = false;
        double ans2 = 0;
        for (List<Node> list : map.values()) {
            double local = 0;
            if (k - (n - list.size() + threshold) > 0) {
                //pick all
                int prev = k - (n - list.size() + threshold);
                valid = true;
                Node last = center;
                int l = prev;
                int r = list.size() - threshold - 1;
                for (int i = 0; i < list.size(); i++) {
                    if (i >= l && i <= r) {
                        continue;
                    }
                    Node node = list.get(i);
                    int t = (n - list.size()) + (i < l ? i : i - (r - l + 1));
                    local += (node.d - last.d) * t * (k - t);
                    last = node;
                }
            } else {
                //fill
                Node last = center;
                for (int i = 0; i < list.size(); i++) {
                    Node node = list.get(i);
                    int t = list.size() - i;
                    local += (node.d - last.d) * t * (k - t);
                    last = node;
                }
            }

            ans2 += local;
        }

        double ans = ans1;
        debug.debug("ans1", ans1);
        debug.debug("ans2", ans2);
        if (valid) {
            ans = Math.max(ans, ans2);
        }

        out.println(ans);
    }
}

class Node {
    double d;
    IntegerPoint2 pt;
    Node p;
    int after;
    double weight;

    @Override
    public String toString() {
        return pt.toString();
    }
}