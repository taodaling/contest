package on2021_03.on2021_03_23_Codeforces___Codeforces_Round__239__Div__1_.E__k_d_sequence;



import template.datastructure.MinQueue;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerMinQueue;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

public class EKDSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        k = in.ri();
        int d = in.ri();

        if (d == 0) {
            int[] a = in.ri(n);
            int ans = 0;
            for (int i = 0; i < n; i++) {
                int l = i;
                int r = i;
                while (r + 1 < n && a[r + 1] == a[r]) {
                    r++;
                }
                i = r;
                ans = Math.max(ans, r - l + 1);
            }
            out.println(ans);
            return;
        }

        a = new int[n];
        items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
        }
        next = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            a[i] = DigitUtils.floorDiv(x, d);
            b[i] = x - a[i] * d;
        }
        best = 1;
        IntegerHashMap map = new IntegerHashMap(n, false);
        for (int i = n - 1; i >= 0; i--) {
            next[i] = map.getOrDefault(a[i], n);
            map.put(a[i], i);
        }
    }

    int[] a;
    int[] next;
    int k;
    int best = 0;
    Item[] items;

    Deque<Item> minMaxMinRDq = new ArrayDeque<>((int) 2e5);
    Deque<Item> minMaxRDq = new ArrayDeque<>((int) 2e5);
    Deque<Item> minMinRDq = new ArrayDeque<>((int) 2e5);
    Deque<Item> minRDq = new ArrayDeque<>((int) 2e5);

    public void dac(int l, int r) {
        if (l == r) {
            return;
        }
        int m = (l + r) / 2;
        dac(l, m);
        dac(m + 1, r);
        minMaxMinRDq.clear();
        minMaxRDq.clear();
        minMinRDq.clear();
        minRDq.clear();
        int rDup = r;
        for (int i = m; i >= l; i--) {
            items[i].index = i;
            items[i].min = a[i];
            items[i].max = a[i];
            if (i < m) {
                items[i].min = Math.min(items[i].min, items[i + 1].min);
                items[i].max = Math.max(items[i].max, items[i + 1].max);
            }
        }
        for (int i = m + 1; i <= r; i++) {
            items[i].index = i;
            items[i].min = a[i];
            items[i].max = a[i];
            if (i > m + 1) {
                items[i].min = Math.min(items[i].min, items[i - 1].min);
                items[i].max = Math.max(items[i].max, items[i - 1].max);
            }
            items[i].minMaxMinR = items[i].max - items[i].min - items[i].index;
            items[i].minMaxR = items[i].max - items[i].index;
            items[i].minR = -items[i].index;
            items[i].minMinR = -items[i].min - items[i].index;
        }
        int dup = r;
        for(int i = m + 1; i <= r; i++){
            dup = Math.min(dup, next[i] - 1);
        }
        for(int i = m; i >= l; i--){
            dup = Math.min(dup, next[i] - 1);

        }
    }
}

class Item {
    int index;
    int max;
    int min;
    int minMaxMinR;
    int minMaxR;
    int minMinR;
    int minR;
}