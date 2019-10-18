package on2019_10.on2019_10_18_Codeforces_Round__593__Div__2_.F___Alice_and_the_Cactus;



import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskF {

    NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
    NumberTheory.Power pow = new NumberTheory.Power(mod);
    int ONE_TWO = pow.inverse(2);
    int ONE_FOUR = pow.inverse(4);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            Edge edge = new Edge(nodes[in.readInt()], nodes[in.readInt()]);
            edge.a.next.add(edge);
            edge.b.next.add(edge);
            edges[i] = edge;
        }

        if(n == 1){
            out.println(ONE_FOUR);
            return;
        }

        List<Circle> circles = new ArrayList<>(m);
        List<Node> stack = new ArrayList<>(n);
        dfsForCircle(nodes[1], null, circles, stack);

        int exp = 0;
        int loopContri = 0;
        int edgeContri = 0;
        int nodeContri = 0;
        for (Circle c : circles) {
            loopContri = mod.plus(loopContri, c.prob);
        }
        edgeContri = mod.mul(m, pow.pow(ONE_TWO, 2));
        nodeContri = mod.mul(n, pow.pow(ONE_TWO, 1));
        exp = mod.plus(loopContri, mod.subtract(nodeContri, edgeContri));

        int exp2 = 0;
        int loop2Contri = 0;
        int node2Contri = 0;
        int edge2Contri = 0;
        int loopNodeContri = 0;
        int loopEdgeContri = 0;
        int nodeEdgeContri = 0;

        int allCircleProbSum = 0;
        for (Circle c : circles) {
            allCircleProbSum = mod.plus(allCircleProbSum, pow.pow(ONE_TWO, c.nodes.size()));
        }

        for (Circle c : circles) {
            int local = 0;
            local = mod.plus(local, c.prob);
            local = mod.plus(local, mod.mul(c.prob, mod.subtract(allCircleProbSum, c.prob)));
            for (Node node : c.nodes) {
                local = mod.plus(local, mod.mul(c.prob, mod.subtract(node.probSum, c.prob)));
            }
            loop2Contri = mod.plus(loop2Contri, local);
        }

        node2Contri = mod.plus(node2Contri, mod.mul(n, ONE_TWO));
        node2Contri = mod.plus(node2Contri, mod.mul(mod.mul(n, n - 1), ONE_FOUR));

        for(Edge e : edges){
            int local = ONE_FOUR;
            int related = e.a.next.size() + e.b.next.size() - 2;
            int unrelated = m - related - 1;
            local = mod.plus(local, mod.mul(related, pow.pow(ONE_TWO, 3)));
            local = mod.plus(local, mod.mul(unrelated, pow.pow(ONE_TWO, 4)));
            edge2Contri = mod.plus(edge2Contri, local);
        }

        for(Circle c : circles){
            int related = c.nodes.size();
            int unrelated = n - related;
            int local1 = mod.mul(related, pow.pow(ONE_TWO, c.nodes.size()));
            int local2 = mod.mul(unrelated, pow.pow(ONE_TWO, c.nodes.size() + 1));
            int local = mod.plus(local1, local2);
            loopNodeContri = mod.plus(loopNodeContri, local);
        }
        loopNodeContri = mod.mul(loopNodeContri, 2);

        for(Circle c : circles){
            int relatedEdge = 0;
            for(Node node : c.nodes){
                relatedEdge += node.next.size() - 2;
            }
            int unrelatedEdge = m - c.nodes.size() - relatedEdge;
            int local1 = mod.mul(c.nodes.size(), c.prob);
            int local2 = mod.mul(relatedEdge, mod.mul(c.prob, ONE_TWO));
            int local3 = mod.mul(unrelatedEdge, mod.mul(c.prob, ONE_FOUR));
            int local = local1;
            local = mod.plus(local, local2);
            local = mod.plus(local, local3);
            loopEdgeContri = mod.plus(loopEdgeContri, local);
        }
        loopEdgeContri = mod.mul(loopEdgeContri, 2);

        nodeEdgeContri = mod.mul(2, ONE_FOUR);
        nodeEdgeContri = mod.plus(nodeEdgeContri, mod.mul(n - 2, pow.pow(ONE_TWO, 3)));
        nodeEdgeContri = mod.mul(nodeEdgeContri, m);
        nodeEdgeContri = mod.mul(nodeEdgeContri, 2);

        exp2 = mod.plus(exp2, loop2Contri);
        exp2 = mod.plus(exp2, node2Contri);
        exp2 = mod.plus(exp2, edge2Contri);
        exp2 = mod.plus(exp2, loopNodeContri);
        exp2 = mod.subtract(exp2, loopEdgeContri);
        exp2 = mod.subtract(exp2, nodeEdgeContri);

        int ans = mod.mul(exp, exp);
        ans = mod.subtract(exp2, ans);

        out.println(ans);
    }

    public int valueOf(int a, int b){
        return mod.mul(a, pow.inverse(b));
    }

    public void dfsForCircle(Node root, Edge from, List<Circle> circles, List<Node> stack) {
        if (root.visited) {
            if(!root.instack){
                return;
            }
            int len = from.other(root).depth - root.depth + 1;
            Circle c = new Circle(len);
            c.prob = pow.pow(ONE_TWO, len);
            circles.add(c);

            for (int i = stack.size() - 1; i >= 0; i--) {
                Node node = stack.get(i);
                c.nodes.add(node);
                node.probSum = mod.plus(node.probSum, c.prob);
                if (node == root) {
                    break;
                }
            }
            return;
        }
        root.instack = true;
        stack.add(root);
        root.depth = stack.size();
        root.visited = true;
        for (Edge e : root.next) {
            if(e == from){
                continue;
            }
            Node node = e.other(root);
            dfsForCircle(node, e, circles, stack);
        }
        stack.remove(stack.size() - 1);
        root.instack = false;
    }
}


class Circle {
    public Circle(int cap) {
        nodes = new ArrayList<>(cap);
    }

    int prob;
    List<Node> nodes;
}


class Edge {
    Node a;
    Node b;

    public Edge(Node a, Node b) {
        this.a = a;
        this.b = b;
    }

    Node other(Node x) {
        return a == x ? b : a;
    }
}


class Node {
    List<Edge> next = new ArrayList<>(2);
    int probSum;
    int id;
    int depth;
    boolean visited = false;
    boolean instack;

    @Override
    public String toString() {
        return "" + id;
    }
}
