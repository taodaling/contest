package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FTrucksAndCities {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        Car[] cars = new Car[m];
        for (int i = 0; i < m; i++) {
            cars[i] = new Car();
            cars[i].s = in.readInt() - 1;
            cars[i].f = in.readInt() - 1;
            cars[i].c = in.readInt();
            cars[i].r = in.readInt();
        }
        Map<Integer, List<Car>> carGroupByR =
                Stream.of(cars).collect(Collectors.groupingBy(x -> x.r));

        int[][] lastDp = new int[n][n];
        int[][] nextDp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (i == j) {
                    lastDp[i][j] = 0;
                } else {
                    lastDp[i][j] = a[j] - a[i];
                }
            }
        }

        relax(lastDp, carGroupByR.get(0));
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n; j++) {
                int ptr = j;
                for (int k = j; k < n; k++) {
                    while (ptr < k && Math.max(lastDp[j][ptr], a[k] - a[ptr]) >=
                            Math.max(lastDp[j][ptr + 1], a[k] - a[ptr + 1])) {
                        ptr++;
                    }
                    nextDp[j][k] = Math.max(lastDp[j][ptr], a[j] - a[ptr]);
                }
            }
            relax(nextDp, carGroupByR.get(i));
            {
                int[][] tmp = lastDp;
                lastDp = nextDp;
                nextDp = tmp;
            }
        }

        long ans = 0;
        for(Car car : cars){
            ans = Math.max(ans, car.v);
        }

        out.println(ans);
    }

    public void relax(int[][] dp, List<Car> cars) {
        if (cars == null) {
            return;
        }
        for (Car car : cars) {
            long stepAtLeast = (long)dp[car.s][car.f] * car.c;
            car.v = stepAtLeast;
        }
    }
}

class Car {
    int s;
    int f;
    int c;
    int r;

    long v;
}