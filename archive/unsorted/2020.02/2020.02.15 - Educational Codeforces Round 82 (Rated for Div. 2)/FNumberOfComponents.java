package contest;

import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerBIT;
import template.primitve.generated.MultiWayIntegerStack;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FNumberOfComponents {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        int[][] mat = new int[n + 1][m + 1];
        int[][] times = new int[n + 1][m + 1];

        MultiWayStack<Event> events = new MultiWayStack((int)2e6 + 1, n * m + q);
        for (int i = 1; i <= q; i++) {
            int x = in.readInt();
            int y = in.readInt();
            int c = in.readInt();
            if (mat[x][y] == c) {
                continue;
            }
            events.addLast(mat[x][y], new Event(x, y, times[x][y], i));
            mat[x][y] = c;
            times[x][y] = i;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                events.addLast(mat[i][j], new Event(i, j, times[i][j], q + 1));
            }
        }
        int[] ans = new int[q + 2];
        Node[][] nodes = new Node[n + 1][m + 1];
        List<Event> list = new ArrayList<>(q);
        int[][] dirs = new int[][]{
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        for (int i = 0; i < events.numberOfQueue(); i++) {
            if (events.isEmpty(i)) {
                continue;
            }
            fill(nodes);
            list.clear();

            while (!events.isEmpty(i)) {
                list.add(events.removeLast(i));
            }
            list.sort((a, b) -> a.add - b.add);
            int cc = 0;
            for (Event event : list) {
                int o = cc;
                int x = event.x;
                int y = event.y;
                nodes[x][y] = new Node();
                cc++;
                for (int[] dir : dirs) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    if (nx < 1 || nx > n || ny < 1 || ny > m) {
                        continue;
                    }
                    if (nodes[nx][ny] == null) {
                        continue;
                    }
                    if (nodes[nx][ny].find() == nodes[x][y].find()) {
                        continue;
                    }
                    Node.merge(nodes[nx][ny], nodes[x][y]);
                    cc--;
                }
                ans[event.add] += cc - o;
            }

            fill(nodes);
            list.sort((a, b) -> -(a.die - b.die));
            cc = 0;
            for (Event event : list) {
                int o = cc;
                int x = event.x;
                int y = event.y;
                nodes[x][y] = new Node();
                cc++;
                for (int[] dir : dirs) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    if (nx < 1 || nx > n || ny < 1 || ny > m) {
                        continue;
                    }
                    if (nodes[nx][ny] == null) {
                        continue;
                    }
                    if (nodes[nx][ny].find() == nodes[x][y].find()) {
                        continue;
                    }
                    Node.merge(nodes[nx][ny], nodes[x][y]);
                    cc--;
                }
                ans[event.die] += -(cc - o);
            }
        }

        for (int i = 1; i <= q; i++) {
            ans[i] += ans[i - 1];
            out.println(ans[i]);
        }
    }

    public void fill(Node[][] nodes) {
        for (Node[] x : nodes) {
            Arrays.fill(x, null);
        }
    }
}

class Node {
    Node p = this;
    int rank = 0;

    public Node find() {
        return p.p == p ? p : (p = p.find());
    }

    public static void merge(Node a, Node b) {
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
        } else {
            a.p = b;
        }
    }
}

class Event {
    int x;
    int y;
    int add;
    int die;

    public Event(int x, int y, int add, int die) {
        this.x = x;
        this.y = y;
        this.add = add;
        this.die = die;
    }
}

class Request {
    int x;
    int y;
    int c;
}