package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class EPerpetualMotionMachine {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        int[] ans = new int[n];
        for (Node node : nodes) {
            circle.clear();
            if (!dfs(node, null)) {
                continue;
            }
            for (Node x : circle) {
                ans[x.id] = 1;
            }
            outputAnswer(out, ans);
            return;
        }

        for (Node node : nodes) {
            if (node.adj.size() < 4) {
                continue;
            }
            for (int i = 0; i < 4; i++) {
                ans[node.adj.get(i).id] = 1;
            }
            ans[node.id] = 2;
            outputAnswer(out, ans);
            return;
        }

        for (Node node : nodes) {
            if (node.adj.size() < 3) {
                continue;
            }
            path.clear();
            if (!findPath(node, null, node)) {
                continue;
            }
            Node head = path.get(0);
            Node tail = path.get(path.size() - 1);
            head.adj.remove(path.get(1));
            tail.adj.remove(path.get(path.size() - 2));

            for (Node nearby : head.adj) {
                ans[nearby.id] = 1;
            }
            for (Node nearby : tail.adj) {
                ans[nearby.id] = 1;
            }
            for (Node x : path) {
                ans[x.id] = 2;
            }
            outputAnswer(out, ans);
            return;
        }

        for (Node node : nodes) {
            if (node.adj.size() < 3) {
                continue;
            }

            dfsForDepth(node, null, 0);
            for (int i = 0; i < 3; i++) {
                traces[i].clear();
                dfsForLongestPath(node.adj.get(i), node, traces[i]);
            }

            Arrays.sort(traces, (a, b) -> a.size() - b.size());
            // 3 3 3
            if (traces[0].size() >= 2) {
                ans[node.id] = 3;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        ans[traces[j].get(i).id] = 2 - i;
                    }
                }
                outputAnswer(out, ans);
                return;
            }

            // 2 4 4
            if (traces[0].size() >= 1 && traces[1].size() >= 2 && traces[2].size() >= 5) {
                ans[node.id] = 6;
                ans[traces[0].get(0).id] = 3;
                for (int i = 0; i < 2; i++) {
                    ans[traces[1].get(i).id] = 6 - (i + 1) * 6 / 3;
                }
                for (int i = 0; i < 5; i++) {
                    ans[traces[2].get(i).id] = 6 - (i + 1);
                }
                outputAnswer(out, ans);
                return;
            }

            // 2 4 4
            if (traces[0].size() >= 1 && traces[1].size() >= 3 && traces[2].size() >= 3) {
                ans[node.id] = 4;
                ans[traces[0].get(0).id] = 2;
                for (int i = 0; i < 3; i++) {
                    ans[traces[1].get(i).id] = 4 - (i + 1);
                    ans[traces[2].get(i).id] = 4 - (i + 1);
                }
                outputAnswer(out, ans);
                return;
            }
        }

        out.println("NO");
    }

    int limit = (int) 1e5;
    List<Node> path = new ArrayList<>(limit);
    List<Node>[] traces = new List[]{new ArrayList(limit), new ArrayList(limit), new ArrayList(limit)};

    public void dfsForDepth(Node root, Node p, int depth) {
        root.farthest = root.depth = depth;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForDepth(node, root, depth + 1);
            root.farthest = Math.max(root.farthest, node.farthest);
        }
    }

    public void dfsForLongestPath(Node root, Node p, List<Node> trace) {
        trace.add(root);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (root.farthest != node.farthest) {
                continue;
            }
            dfsForLongestPath(node, root, trace);
            return;
        }
    }

    public boolean findPath(Node root, Node p, Node from) {
        path.add(root);
        if (root != from && root.adj.size() >= 3) {
            return true;
        }

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (findPath(node, root, from)) {
                return true;
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    public void outputAnswer(FastOutput out, int[] ans) {
        out.println("YES");
        for (int x : ans) {
            out.append(x).append(' ');
        }
        out.println();
    }

    Deque<Node> circle = new ArrayDeque<>(limit);

    public boolean dfs(Node root, Node p) {
        if (root.visited) {
            if (root.instk) {
                while (circle.peekFirst() != root) {
                    circle.removeFirst();
                }
                return true;
            }
            return false;
        }
        root.visited = true;
        root.instk = true;
        circle.addLast(root);

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (dfs(node, root)) {
                return true;
            }
        }

        root.instk = false;
        return false;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    boolean visited;
    boolean instk;
    int depth;
    int farthest;

    @Override
    public String toString() {
        return "" + id;
    }
}
