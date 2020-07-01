package template.graph;

import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

public class CircularPath {
    IntegerMultiWayStack edges;
    boolean[] instk;
    boolean[] visited;
    int n;
    IntegerArrayList circular;

    public CircularPath(IntegerMultiWayStack edges) {
        this.edges = edges;
        n = edges.stackNumber();
        instk = new boolean[n];
        visited = new boolean[n];
        circular = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            if (dfs(i)) {
                return;
            }
        }
        circular = null;
    }

    public boolean containCircle() {
        return circular != null;
    }

    public IntegerArrayList getCircular() {
        return circular;
    }

    private boolean dfs(int root) {
        if (visited[root]) {
            if (instk[root]) {
                int index = circular.indexOf(root);
                circular.remove(0, index - 1);
                return true;
            }
            return false;
        }
        visited[root] = true;
        instk[root] = true;
        circular.push(root);

        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (dfs(node)) {
                return true;
            }
        }

        circular.pop();
        instk[root] = false;

        return false;
    }
}
