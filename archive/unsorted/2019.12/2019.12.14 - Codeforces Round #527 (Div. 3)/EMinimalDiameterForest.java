package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class EMinimalDiameterForest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        IntegerMultiWayStack edges = new IntegerMultiWayStack(n, 2 * n);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges.addLast(a, b);
            edges.addLast(b, a);
        }

        ForestDiameter forestDiameter = new ForestDiameter(edges, n);
        IntegerHashMap map = new IntegerHashMap(n, false);
        for (int i = 0; i < n; i++) {
            map.putIfNotExist(forestDiameter.getCenters(i).get(0),
                    forestDiameter.getDiameter(i));
        }

        int maxDiameter = -1;
        int centerId = -1;
        for (IntEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            int key = iterator.getEntryKey();
            int value = iterator.getEntryValue();
            if (value > maxDiameter) {
                maxDiameter = value;
                centerId = key;
            }
        }

        List<int[]> addedEdges = new ArrayList<>(n);
        for (IntEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            int key = iterator.getEntryKey();
            if(key == centerId){
                continue;
            }
            addedEdges.add(SequenceUtils.wrapArray(centerId, key));
            edges.addLast(centerId, key);
            edges.addLast(key, centerId);
        }

        TreeDiameter treeDiameter = new TreeDiameter(edges, n);
        int diameter = treeDiameter.getDiameter();

        out.println(diameter);
        for(int[] edge : addedEdges){
            out.append(edge[0] + 1).append(' ').append(edge[1] + 1).append('\n');
        }
    }
}