package on2021_07.on2021_07_27_CodeChef___Practice_Medium_.Valet_Parking;



import template.datastructure.DSU;
import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValetParking {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Car[] cars = new Car[n];
        for (int i = 0; i < n; i++) {
            cars[i] = new Car();
        }
        for (int i = 0; i < n; i++) {
            cars[i].l = in.ri();
        }
        for (int i = 0; i < n; i++) {
            cars[i].x = in.ri();
        }
        for (int i = 0; i < n; i++) {
            cars[i].r = cars[i].l + in.ri();
        }
        for (int i = 0; i < n; i++) {
            cars[i].a = in.ri();
        }
        int L = 4000;
        Arrays.sort(cars, Comparator.comparingInt(x -> -x.a));
        Map<Integer, List<Car>> groupBy = Arrays.stream(cars).collect(Collectors.groupingBy(x -> x.x));
        int[][] move = new int[L + 1][L * 2 + 1];
        DSUExt dsu = new DSUExt(L * 2 + 1);
//        RangeTree rt = new RangeTree(L * 2 + 1);
        for (Map.Entry<Integer, List<Car>> entry : groupBy.entrySet()) {
            int x = entry.getKey();
            dsu.init();

            for (Car c : entry.getValue()) {
                int l = dsu.rightest[dsu.find(c.l)];
                int r = c.r;
                while (l <= r) {
                    move[x][l] = Math.max(move[x][l], c.a);
                    if (l + 1 <= r) {
                        dsu.merge(l, l + 1);
                        l = dsu.rightest[dsu.find(c.l)];
                    } else {
                        break;
                    }
                }
            }
        }

        long[] dp = new long[L * 3 + 1];
        dp[0] = 0;
        for (int i = 0; i <= L * 2; i++) {
            for (int j = 0; j <= L; j++) {
                dp[i + j] = Math.max(dp[i + j], dp[i] + move[j][i]);
            }
        }
        debug.debug("dp", dp);
        debug.debug("move", move);
        long ans = Arrays.stream(dp).max().orElse(-1);
        out.println(ans);
    }
    Debug debug = new Debug(false);
}


class Car {
    int l;
    int r;
    int x;
    int a;
}

class DSUExt extends DSU {
    int[] rightest;

    public DSUExt(int n) {
        super(n);
        rightest = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        for (int i = 0; i < n; i++) {
            rightest[i] = i;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        rightest[a] = Math.max(rightest[a], rightest[b]);
    }
}