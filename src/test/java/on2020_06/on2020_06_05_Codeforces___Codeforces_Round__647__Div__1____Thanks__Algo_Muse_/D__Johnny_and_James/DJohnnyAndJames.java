package on2020_06.on2020_06_05_Codeforces___Codeforces_Round__647__Div__1____Thanks__Algo_Muse_.D__Johnny_and_James;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DJohnnyAndJames {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].x = in.readInt();
            nodes[i].y = in.readInt();
            nodes[i].dist = Math.sqrt(nodes[i].x * nodes[i].x + nodes[i].y * nodes[i].y);
            nodes[i].id = i;
        }

        Node center;
        List<Node> top = new ArrayList<>();
        List<Node> bot = new ArrayList<>();
        List<Node> left = new ArrayList<>();
        List<Node> right = new ArrayList<>();
        Map<Long, List<Node>>[] maps = new HashMap[4];
        for (int i = 0; i < 4; i++) {
            maps[i] = new HashMap<>(n);
        }

        for (Node node : nodes) {
            if (node.x == 0 && node.y == 0) {
                center = node;
                continue;
            }
            if (node.x == 0) {
                if (node.y > 0) {
                    top.add(node);
                } else {
                    bot.add(node);
                }
                continue;
            }
            if (node.y == 0) {
                if (node.x > 0) {
                    right.add(node);
                } else {
                    left.add(node);
                }
                continue;
            }

            Map<Long, List<Node>> map = maps[dir(node.x, node.y)];
            Long key = getKey(node.x, node.y);
            List<Node> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }
            list.add(node);
        }

        List<List<Node>> allLists = new ArrayList<>(n);
        allLists.add(top);
        allLists.add(left);
        allLists.add(right);
        allLists.add(bot);
        for (Map<Long, List<Node>> map : maps) {
            allLists.addAll(map.values());
        }


    }

    public double[] function(List<Node> list) {
        int n = list.size();
        double[] ans = new double[n + 1];
        ans[0] = 0;

        double sumA = 0;
        double sumB = 0;
        for (int i = 1; i <= n; i++) {
            Node node = list.get(i - 1);
            sumA += 
        }
    }

    int dir(int x, int y) {
        if (x > 0 && y > 0) {
            return 0;
        }
        if (x < 0 && y > 0) {
            return 1;
        }
        if (x < 0 && y < 0) {
            return 2;
        }
        return 3;
    }

    long getKey(int x, int y) {
        x = Math.abs(x);
        y = Math.abs(y);
        int g = GCDs.gcd(x, y);
        x /= g;
        y /= g;
        return DigitUtils.asLong(x, y);
    }
}


class Node {
    List<Node> adj = new ArrayList<>();
    int x;
    int y;
    int id;
    double dist;
}