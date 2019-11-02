package contest;

import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i + 1;
            nodes[i].x = in.readInt();
            nodes[i].y = in.readInt();

        }

        for (int i = 0; i < n; i++) {
            nodes[i].c = in.readInt();
            nodes[i].dist = nodes[i].c;
        }

        for (int i = 0; i < n; i++) {
            nodes[i].k = in.readInt();
        }


        List<Node> light = new ArrayList<>();
        List<Node[]> edges = new ArrayList<>();
        long totalFee = 0;
        for (int i = 0; i < n; i++) {
            Node nearest = null;
            for (int j = 0; j < n; j++) {
                if (nodes[j].handled) {
                    continue;
                }
                if (nearest == null || nearest.dist > nodes[j].dist) {
                    nearest = nodes[j];
                }
            }
            nearest.handled = true;
            totalFee += nearest.dist;
            if (nearest.last == null) {
                light.add(nearest);
            } else {
                edges.add(SequenceUtils.wrapObjectArray(nearest, nearest.last));
            }
            for (int j = 0; j < n; j++) {
                if (nodes[j].handled) {
                    continue;
                }
                long dist = distBetween(nearest, nodes[j]);
                if (dist < nodes[j].dist) {
                    nodes[j].dist = dist;
                    nodes[j].last = nearest;
                }
            }
        }

        out.println(totalFee);
        out.println(light.size());
        for (Node node : light) {
            out.append(node.id).append(' ');
        }
        out.println();
        out.println(edges.size());
        for (Node[] e : edges) {
            out.append(e[0].id).append(' ').append(e[1].id).append('\n');
        }
    }

    public long distBetween(Node a, Node b) {
        return (long) (a.k + b.k) * (Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
    }
}


class Node {
    int x;
    int y;
    int k;
    int c;
    int id;

    Node last;
    boolean handled;
    long dist;
}
