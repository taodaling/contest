package contest;


import template.algo.WQSBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahamSummation;
import template.utils.SortUtils;

import java.util.Comparator;

public class ProgrammersAndArtists {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        int n = in.ri();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].a = in.ri();
            items[i].b = in.ri();
        }

        WQSBinarySearch wqs = new WQSBinarySearch() {
            @Override
            protected double getBest() {
                return best;
            }

            @Override
            protected int getTime() {
                return time;
            }

            int time;
            double best;

            @Override
            protected void check(double costPerOperation) {
                for (Item item : items) {
                    item.va = item.a - costPerOperation;
                    item.vb = item.b - Math.max(item.va, 0);
                }
                if (b > 0) {
                    SortUtils.theKthSmallestElement(items, Comparator.comparingDouble(x -> -x.vb), 0, n, b);
                }
                best = 0;
                KahamSummation sum = new KahamSummation();
                for (int i = 0; i < b; i++) {
                    sum.add(items[i].b);
                }
                time = 0;
                for (int i = b; i < n; i++) {
                    if (items[i].va > 0) {
                        time++;
                        sum.add(items[i].va);
                    }
                }
                best = sum.sum();
            }
        };
        double ans = wqs.search(0, 1e9, 100, a, true);
        out.println(Math.round(ans));
    }
}

class Item {
    int a;
    int b;
    double va;
    double vb;
}