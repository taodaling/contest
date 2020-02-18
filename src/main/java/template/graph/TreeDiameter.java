package template.graph;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

public class TreeDiameter {
    private IntegerMultiWayStack edges;
    private int[] depth;
    private int[] parents;
    private int diameter;
    private IntegerList centers;
    private IntegerList ends;

    public IntegerList getCenters() {
        return centers;
    }

    public IntegerList getEnds() {
        return ends;
    }

    public int getDiameter() {
        return diameter;
    }

    public TreeDiameter(IntegerMultiWayStack edges, int n) {
        this.edges = edges;
        depth = new int[n];
        centers = new IntegerList(2);
        ends = new IntegerList(2);
        parents = new int[n];

        dfsForDepth(0, -1);
        int end = 0;
        for (int i = 0; i < n; i++) {
            if (depth[i] > depth[end]) {
                end = i;
            }
        }
        dfsForDepth(end, -1);
        int another = 0;
        for (int i = 0; i < n; i++) {
            if (depth[i] > depth[another]) {
                another = i;
            }
        }

        ends.add(end);
        ends.add(another);

        diameter = depth[another];
        for (int i = another; i != -1; i = parents[i]) {
            if (depth[i] == DigitUtils.ceilDiv(diameter, 2) ||
                    depth[i] == DigitUtils.floorDiv(diameter, 2)) {
                centers.add(i);
            }
        }

        ends.unique();
        centers.unique();
    }

    private void dfsForDepth(int root, int p) {
        parents[root] = p;
        depth[root] = p != -1 ? depth[p] + 1 : 0;
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfsForDepth(node, root);
        }
    }
}
