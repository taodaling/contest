package on2020_10.on2020_10_29_AtCoder___AtCoder_Beginner_Contest_130.F___Minimum_Bounding_Box;



import template.algo.DoubleTernarySearch;
import template.algo.LongTernarySearch;
import template.geometry.ConvexHullTrick;
import template.geometry.LongConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.DoubleArrayList;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import java.util.function.LongUnaryOperator;

public class FMinimumBoundingBox {
    int inf = (int) 1e9;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] maxX = new int[3];
        int[] minX = new int[3];
        int[] maxY = new int[3];
        int[] minY = new int[3];


        Arrays.fill(maxX, -inf);
        Arrays.fill(minX, inf);
        Arrays.fill(maxY, -inf);
        Arrays.fill(minY, inf);
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            char d = in.readChar();
            int lrType = 1;
            int duType = 1;
            if (d == 'L') {
                lrType = 0;
            }
            if (d == 'R') {
                lrType = 2;
            }
            if (d == 'D') {
                duType = 0;
            }
            if (d == 'U') {
                duType = 2;
            }
            maxX[lrType] = Math.max(maxX[lrType], x);
            minX[lrType] = Math.min(minX[lrType], x);
            maxY[duType] = Math.max(maxY[duType], y);
            minY[duType] = Math.min(minY[duType], y);
        }

        ConvexHullTrick lr = build(minX, maxX);
        ConvexHullTrick du = build(minY, maxY);
        DoubleArrayList list = new DoubleArrayList();
        list.add(0);
        list.add((int) 2e8);
        for (ConvexHullTrick.Line line : lr) {
            if (line.l >= 0 && line.l < 2e8) {
                list.add(line.l);
            }
        }
        for (ConvexHullTrick.Line line : du) {
            if (line.l >= 0 && line.l < 2e8) {
                list.add(line.l);
            }
        }
        list.unique();
        double ans = (long) 2e18;
        for (int i = 1; i < list.size(); i++) {
            double l = list.get(i - 1);
            double r = list.get(i);
            ConvexHullTrick.Line h = lr.queryLine((l + r) / 2);
            ConvexHullTrick.Line v = du.queryLine((l + r) / 2);
            double local = Math.min(h.y(l) * v.y(l), h.y(r) * v.y(r));
            DoubleUnaryOperator op = x -> -h.y(x) * v.y(x);
            DoubleTernarySearch lts = new DoubleTernarySearch(op, 1e-10, 1e-10);
            double index = lts.find(l, r);
            local = Math.min(local, -op.applyAsDouble(index));
            ans = Math.min(ans, local);
        }

        out.println(ans);
    }

    private boolean isValid(long x) {
        return x > -inf && x < inf;
    }

    public ConvexHullTrick build(int[] minX, int[] maxX) {
        ConvexHullTrick right = new ConvexHullTrick();
        for (int i = 0; i < 3; i++) {
            if (isValid(maxX[i])) {
                right.insert(i - 1, maxX[i]);
            }
        }
        //low
        ConvexHullTrick left = new ConvexHullTrick();
        for (int i = 0; i < 3; i++) {
            if (isValid(minX[i])) {
                left.insert(1 - i, -minX[i]);
            }
        }
        ConvexHullTrick lr = ConvexHullTrick.plus(left, right);
        return lr;
    }
}


