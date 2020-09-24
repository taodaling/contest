package contest;

import org.apache.commons.lang.math.IntRange;
import template.datastructure.MultiWayDeque;
import template.datastructure.MultiWayStack;
import template.datastructure.SimplifiedDeque;
import template.datastructure.SimplifiedStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRadix;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;

public class DBearAndChase {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].size = new int[n];
            nodes[i].id = i;
            nodes[i].probMax = new double[n];
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        int inf = (int) 1e8;
        Deque<Node> dq = new ArrayDeque<>(n);
        int[][] dists = new int[n][n];
        for (int i = 0; i < n; i++) {
            Node first = nodes[i];
            dq.clear();
            for (Node node : nodes) {
                node.dist = inf;
            }
            first.dist = 0;
            dq.addLast(first);
            while (!dq.isEmpty()) {
                Node head = dq.removeFirst();
                dists[i][head.id] = head.dist;
                first.size[head.dist]++;
                for (Node node : head.adj) {
                    if (node.dist > head.dist + 1) {
                        node.dist = head.dist + 1;
                        dq.addLast(node);
                    }
                }
            }
        }

        double maxProb = 0;
        for (int i = 0; i < n; i++) {
            Node first = nodes[i];

            double firstProb = 0;
            for (int j = 0; j < n && first.size[j] > 0; j++) {
                double levelProb = first.size[j] / (double) n;
                double directProb = 1d / first.size[j];

                for (Node node : nodes) {
                    node.prob = 0;
                    node.inq = false;
                }

                dq.clear();
                for (int k = 0; k < n; k++) {
                    if (dists[i][k] != j) {
                        continue;
                    }
                    Node possiblePos = nodes[k];
                    double distribute = directProb / possiblePos.adj.size();
                    for (Node node : possiblePos.adj) {
                        node.prob += distribute;
                        if (!node.inq) {
                            node.inq = true;
                            dq.addLast(node);
                        }
                    }
                }

                for (Node node : dq) {
                    for (int k = 0; k < n; k++) {
                        int d = dists[node.id][k];
                        nodes[k].probMax[d] = Math.max(nodes[k].probMax[d], node.prob);
                    }
                }

                //try third
                double bestChoice = 0;
                for (Node second : nodes) {
                    double secondSum = 0;
                    for (Node node : dq) {
                        int d = dists[node.id][second.id];
                        secondSum += second.probMax[d];
                        second.probMax[d] = 0;
                    }
                    bestChoice = Math.max(bestChoice, secondSum);
                }

                double bestProb = Math.max(bestChoice, directProb);
                firstProb += bestProb * levelProb;
            }

            debug.debug("i", i);
            debug.debug("firstProb", firstProb);
            maxProb = Math.max(maxProb, firstProb);
        }

        out.println(maxProb);
    }
}

class Node {
    int[] size;
    List<Node> adj = new ArrayList<>();
    int dist;
    double prob;
    int id;
    boolean inq;
    double[] probMax;

    static double prec = 1e-12;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
