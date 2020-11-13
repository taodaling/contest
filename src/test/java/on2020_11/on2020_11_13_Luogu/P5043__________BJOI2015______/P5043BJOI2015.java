package on2020_11.on2020_11_13_Luogu.P5043__________BJOI2015______;



import org.apache.commons.lang.math.IntRange;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.HashData;
import template.rand.HashOnTree;
import template.rand.HashUtils;
import template.rand.IntRangeHash;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class P5043BJOI2015 {
    HashData hd1 = new HashData(100);
    HashData hd2 = new HashData(100);
    IntRangeHash rh = new IntRangeHash(hd1, hd2, 100);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        long[] hash = new long[m];
        Map<Long, Integer> cntMap = new HashMap<>();
        for (int i = 0; i < m; i++) {
            int n = in.readInt();
            List<UndirectedEdge>[] g = Graph.createGraph(n);
            for (int j = 0; j < n; j++) {
                int p = in.readInt();
                if (p == 0) {
                    continue;
                }
                Graph.addUndirectedEdge(g, p - 1, j);
            }
            HashOnTree hot = new HashOnTree(g, rh, 0);
            hash[i] = hot.hashWholeTree();
            int finalI = i;
            out.println(cntMap.computeIfAbsent(hash[i], x -> finalI) + 1);
        }
    }
}
