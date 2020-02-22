package on2020_02.on2020_02_19_HDU_Online_Judge.The_Closest_M_Points;



import structures.KdTreePointQuery;
import template.datastructure.KdTree3D;
import template.datastructure.LongKDTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.ToLongFunction;

import java.util.ArrayList;
import java.util.List;

public class TheClosestMPoints {
    List<LongKDTree.Node> ans = new ArrayList<>(10);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        int n = in.readInt();
        int k = in.readInt();
        long[][] pts = new long[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                pts[i][j] = in.readInt();
            }
        }

        LongKDTree kdTree = new LongKDTree(k, (long) 2e18, 0.75, n, pts);
        int t = in.readInt();
        long[] coords = new long[k];

        ToLongFunction<long[]> function = new ToLongFunction<long[]>() {
            @Override
            public long apply(long[] delta) {
                long sum = 0;
                for (long x : delta) {
                    sum += x * x;
                }
                return sum;
            }
        };
        for (int i = 0; i < t; i++) {
            for (int j = 0; j < k; j++) {
                coords[j] = in.readInt();
            }
            int m = in.readInt();
            ans.clear();
            kdTree.searchNearest(coords, function, m, ans);
            ans.sort((a, b) -> Long.compare(a.dist, b.dist));
            out.append("the closest ").append(m).append(" points are:").println();
            for (LongKDTree.Node node : ans) {
                for (long x : node.coordinates) {
                    out.append(x).append(' ');
                }
                out.pop(1);
                out.println();
            }
        }
    }
}


