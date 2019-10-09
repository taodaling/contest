package template;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Hierholzer algorithm, searching euler trace
 */
public class DirectedEulerTrace {
    private static class Node {
        List<Node> edges = new ArrayList(2);
        Deque<Node> deque = new ArrayDeque();
        int id;
        int inDegree;

        @Override
        public String toString() {
            return "" + id;
        }
    }

    Node[] nodes;
    int edgeNum;

    public DirectedEulerTrace(int n) {
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
    }

    public void addEdge(int a, int b) {
        nodes[a].edges.add(nodes[b]);
        nodes[b].inDegree++;
        edgeNum++;
    }


    public boolean isContainEulerTrace() {
        return containEulerTrace;
    }

    public boolean isEulerTraceClose() {
        return isEulerTraceClose;
    }

    public List<Node> getEulerTrace() {
        return eulerTrace;
    }

    boolean containEulerTrace;
    boolean isEulerTraceClose;
    List<Node> eulerTrace = new ArrayList();

    private void dfs(Node root) {
        while (!root.deque.isEmpty()) {
            Node tail = root.deque.removeFirst();
            dfs(tail);
        }
        eulerTrace.add(root);
    }

    public boolean findEulerTraceSince(Node root) {
        eulerTrace.clear();
        containEulerTrace = false;
        isEulerTraceClose = false;
        for (Node node : nodes) {
            node.deque.clear();
            node.deque.addAll(node.edges);
        }
        dfs(root);
        containEulerTrace = eulerTrace.size() == edgeNum + 1;
        if (!containEulerTrace) {
            return false;
        }
        isEulerTraceClose = eulerTrace.get(eulerTrace.size() - 1)
                == eulerTrace.get(0);

        return true;
    }

    public boolean findTrace() {
        eulerTrace.clear();
        containEulerTrace = false;
        isEulerTraceClose = false;

        Node p1 = null;
        Node p2 = null;
        for (Node node : nodes) {
            if (node.edges.size() != node.inDegree) {
                if (p1 == null) {
                    p1 = node;
                } else if (p2 == null) {
                    p2 = node;
                } else {
                    containEulerTrace = false;
                    return false;
                }
            }
        }

        if (p1 != null && p2 != null) {
            if (findEulerTraceSince(p1)) {
                return true;
            }
            if (findEulerTraceSince(p2)) {
                return true;
            }
        }

        for (Node node : nodes) {
            if (node.edges.size() > 0) {
                return findEulerTraceSince(node);
            }
        }

        return true;
    }
}
