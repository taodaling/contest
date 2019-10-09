package template;

import java.util.ArrayList;
import java.util.List;

public class KMAlgo {
    public static class Node {
        List<Node> nodes = new ArrayList(2);
        int visited;
        Node partner;
        int id;
        boolean leftSide;
        boolean inMinVertexCover;

        @Override
        public String toString() {
            return "" + id;
        }
    }

    Node[] leftSides;
    Node[] rightSides;
    int version;

    public void findMinVertexCover() {
        prepare();
        for (Node r : rightSides) {
            if (r.partner == null) {
                dfsRight(r);
            }
        }

        for (Node l : leftSides) {
            l.inMinVertexCover = l.visited == version;
        }
        for (Node r : rightSides) {
            r.inMinVertexCover = r.visited != version;
        }
    }

    private void dfsRight(Node node) {
        if (node.visited == version) {
            return;
        }
        node.visited = version;
        for (Node next : node.nodes) {
            dfsLeft(next);
        }
    }

    private void dfsLeft(Node node) {
        if (node.partner == null) {
            return;
        }
        if (node.visited == version) {
            return;
        }
        node.visited = version;
        dfsRight(node.partner);
    }

    public KMAlgo(int l, int r) {
        leftSides = new Node[l];
        for (int i = 0; i < l; i++) {
            leftSides[i] = new Node();
            leftSides[i].id = i;
            leftSides[i].leftSide = true;
        }
        rightSides = new Node[r];
        for (int i = 0; i < r; i++) {
            rightSides[i] = new Node();
            rightSides[i].id = i;
        }
    }

    public void addEdge(int lId, int rId) {
        leftSides[lId].nodes.add(rightSides[rId]);
        rightSides[rId].nodes.add(leftSides[lId]);
    }

    private void prepare() {
        version++;
    }

    /**
     * Determine can we find a partner for a left node to enhance the matching degree.
     */
    public boolean matchLeft(int id) {
        if (leftSides[id].partner != null) {
            return false;
        }
        prepare();
        return findPartner(leftSides[id]);
    }

    /**
     * Determine can we find a partner for a right node to enhance the matching degree.
     */
    public boolean matchRight(int id) {
        if (rightSides[id].partner != null) {
            return false;
        }
        prepare();
        return findPartner(rightSides[id]);
    }

    private boolean findPartner(Node src) {
        if (src.visited == version) {
            return false;
        }
        src.visited = version;
        for (Node node : src.nodes) {
            if (!tryRelease(node)) {
                continue;
            }
            node.partner = src;
            src.partner = node;
            return true;
        }
        return false;
    }

    private boolean tryRelease(Node src) {
        if (src.visited == version) {
            return false;
        }
        src.visited = version;
        if (src.partner == null) {
            return true;
        }
        if (findPartner(src.partner)) {
            src.partner = null;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < leftSides.length; i++) {
            if (leftSides[i].partner == null) {
                continue;
            }
            builder.append(leftSides[i].id).append(" - ").append(leftSides[i].partner.id).append("\n");
        }
        return builder.toString();
    }
}