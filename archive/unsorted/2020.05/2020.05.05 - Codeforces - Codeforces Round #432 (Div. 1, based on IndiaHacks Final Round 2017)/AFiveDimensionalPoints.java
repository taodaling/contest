package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

public class AFiveDimensionalPoints {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] pts = new int[n][5];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 5; j++) {
                pts[i][j] = in.readInt();
            }
        }

        if (n >= 300) {
            out.println(0);
            return;
        }

        IntegerList good = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            boolean find = false;
            for (int j = 0; j < n && !find; j++) {
                for (int k = 0; k < n && !find; k++) {
                    if (i == j || k == j || i == k) {
                        continue;
                    }
                    int sum = 0;
                    for (int t = 0; t < 5; t++) {
                        sum += (pts[j][t] - pts[i][t]) * (pts[k][t] - pts[i][t]);
                    }
                    find = sum > 0;
                }
            }
            if (!find) {
                good.add(i);
            }
        }

        out.println(good.size());
        for (int i = 0; i < good.size(); i++) {
            out.println(good.get(i) + 1);
        }

    }
}
