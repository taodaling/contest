package template.graph;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.MultiWayIntegerStack;

import java.util.Arrays;

public class ForestDiameter {
    private MultiWayIntegerStack edges;
    private int[] depth;
    private int[] parents;
    private int[] diameters;
    private IntegerList[] centers;
    private IntegerList[] ends;
    private int[] treeIds;
    private IntegerList collector;

    public IntegerList getCenters(int i) {
        return centers[i];
    }

    public IntegerList getEnds(int i) {
        return ends[i];
    }

    public int getDiameter(int i) {
        return diameters[i];
    }

    public int getTreeId(int i){
        return treeIds[i];
    }

    public ForestDiameter(MultiWayIntegerStack edges, int n) {
        this.edges = edges;
        depth = new int[n];
        parents = new int[n];
        centers = new IntegerList[n];
        ends = new IntegerList[n];
        treeIds = new int[n];
        diameters = new int[n];
        collector = new IntegerList(n);
        int idAllocator = 0;
        Arrays.fill(treeIds, -1);

        for (int j = 0; j < n; j++) {
            if (treeIds[j] != -1) {
                continue;
            }
            IntegerList endList = new IntegerList(2);
            IntegerList centerList = new IntegerList(2);
            collector.clear();
            dfsForCollect(j, -1);
            dfsForDepth(j, -1);
            int end = collector.get(0);
            for (IntegerIterator iterator = collector.iterator(); iterator.hasNext(); ) {
                int node = iterator.next();
                if (depth[node] > depth[end]) {
                    end = node;
                }
            }
            dfsForDepth(end, -1);
            int another = collector.get(0);
            for (IntegerIterator iterator = collector.iterator(); iterator.hasNext(); ) {
                int node = iterator.next();
                if (depth[node] > depth[another]) {
                    another = node;
                }
            }

            endList.add(end);
            endList.add(another);

            int diameter = depth[another];
            for (int i = another; i != -1; i = parents[i]) {
                if (depth[i] == DigitUtils.ceilDiv(diameter, 2) ||
                        depth[i] == DigitUtils.floorDiv(diameter, 2)) {
                    centerList.add(i);
                }
            }

            endList.unique();
            centerList.unique();

            for (IntegerIterator iterator = collector.iterator(); iterator.hasNext(); ) {
                int node = iterator.next();
                treeIds[node] = idAllocator;
                diameters[node] = diameter;
                ends[node] = endList;
                centers[node] = centerList;
            }
            idAllocator++;
        }
    }

    private void dfsForCollect(int root, int p) {
        collector.add(root);
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if (node == p) {
                continue;
            }
            dfsForCollect(node, root);
        }
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
