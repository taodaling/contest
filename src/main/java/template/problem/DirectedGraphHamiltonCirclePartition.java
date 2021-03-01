package template.problem;

import template.graph.DinicBipartiteMatch;

public class DirectedGraphHamiltonCirclePartition {
    DinicBipartiteMatch match;
    int n;

    public DirectedGraphHamiltonCirclePartition(int n) {
        match = new DinicBipartiteMatch(n, n);
        this.n = n;
    }

    public void addEdge(int i, int j) {
        match.addEdge(i, j);
    }

    public boolean solve() {
        return match.solve() == n;
    }

    public int next(int i) {
        return match.mateOfLeft(i);
    }
}
