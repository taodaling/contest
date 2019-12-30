package template.graph;

import template.primitve.generated.IntegerIterator;
import template.primitve.generated.IntegerList;
import template.primitve.generated.MultiWayIntegerStack;

public class CircularPath {
    MultiWayIntegerStack edges;
    boolean[] instk;
    boolean[] visited;
    int n;
    IntegerList circular;

    public CircularPath(MultiWayIntegerStack edges) {
        this.edges = edges;
        n = edges.stackNumber();
        instk = new boolean[n];
        visited = new boolean[n];
        circular = new IntegerList(n);
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

    public IntegerList getCircular() {
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
