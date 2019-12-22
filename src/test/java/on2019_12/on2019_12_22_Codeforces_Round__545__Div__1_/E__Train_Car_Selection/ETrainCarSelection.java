package on2019_12.on2019_12_22_Codeforces_Round__545__Div__1_.E__Train_Car_Selection;



import template.geometry.LongConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class ETrainCarSelection {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        LongConvexHullTrick cht = new LongConvexHullTrick();
        int l = 1;
        int r = n;
        Map<LongConvexHullTrick.Line, Integer> map = new HashMap<>(m + 1);
        map.put(cht.insert(0, 0), 1);
        long bSum = 0;
        long sSum = 0;
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            if (t == 1) {
                int k = in.readInt();
                cht.clear();
                map.put(cht.insert(0, 0), l - k);
                bSum = sSum = 0;
                l -= k;
            } else if (t == 2) {
                int k = in.readInt();
                //size * sSum + b + bSum = 0
                // b = -bSum - size * sSum
                int size = r - l + 1;
                map.put(cht.insert(-size, -(-bSum - size * sSum)), r + 1);
                r += k;
            } else {
                bSum += in.readInt();
                sSum += in.readInt();
            }

            LongConvexHullTrick.Line line = cht.queryLine(sSum);
            long ans = -line.y(sSum) + bSum;
            out.append(map.get(line) - l + 1).append(' ').append(ans).println();
        }
    }
}
