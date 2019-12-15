package template.graph;

import template.datastructure.IntIterator;
import template.datastructure.IntList;
import template.datastructure.MultiWayIntStack;
import template.math.DigitUtils;

public class TreeDiameter {
    private MultiWayIntStack edges;
    private int[] depth;
    private int[] parents;
    private int diameter;
    private IntList centers;
    private IntList ends;

    public IntList getCenters() {
        return centers;
    }

    public IntList getEnds() {
        return ends;
    }

    public int getDiameter() {
        return diameter;
    }

    public TreeDiameter(MultiWayIntStack edges, int n) {
        this.edges = edges;
        depth = new int[n];
        centers = new IntList(2);
        ends = new IntList(2);
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
        for (IntIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfsForDepth(node, root);
        }
    }
}
