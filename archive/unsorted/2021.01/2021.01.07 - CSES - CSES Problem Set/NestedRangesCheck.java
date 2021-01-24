package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

public class NestedRangesCheck {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].l = in.ri();
            items[i].r = in.ri();
        }
        solve(items.clone(), x -> x.a = 1);

        for (Item item : items) {
            item.l = inf - item.l;
            item.r = inf - item.r;
        }
        solve(items.clone(), x -> x.b = 1);
        for (Item item : items) {
            out.append(item.a).append(' ');
        }
        out.println();
        for (Item item : items) {
            out.append(item.b).append(' ');
        }
    }

    int inf = (int) 1e9;

    public void solve(Item[] items, Consumer<Item> consumer) {
        for (Item item : items) {
            item.key = DigitUtils.asLong(item.r, inf - item.l);
        }
        Arrays.sort(items, Comparator.comparingLong(x -> x.key));
        int n = items.length;
        Item best = null;
        for (int i = 0; i < n; i++) {
            int to = i;
            while (to + 1 < n && items[to + 1].l == items[i].l &&
                    items[to + 1].r == items[i].r) {
                to++;
            }
            if (to > i || best != null && best.l >= items[i].l) {
                for (int j = i; j <= to; j++) {
                    consumer.accept(items[j]);
                }
            }
            if (best == null || best.l < items[i].l) {
                best = items[i];
            }
            i = to;
        }
    }
}

class Item {
    int l;
    int r;
    int a;
    int b;
    long key;
}