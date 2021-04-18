package on2021_03.on2021_03_30_CS_Academy___Virtual_FII_Code_2020_Round__1__rated_for_all_.Boring_Operation;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class BoringOperation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Item[] items = new Item[n];
        IntegerArrayList list = new IntegerArrayList(2 * n);
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            items[i] = new Item(x, (int) 1e9 - x);
            items[i].id = i;
            list.add(items[i].a);
            list.add(items[i].b);
        }

        TreeSet<Item> set = new TreeSet<>(Comparator.<Item>comparingInt(x -> x.a).thenComparingInt(x -> x.id));
        set.addAll(Arrays.asList(items));
        list.unique();
        int ans = Integer.MAX_VALUE;
        for (int begin : list.toArray()) {
            while (!set.isEmpty() && set.first().a < begin) {
                Item first = set.pollFirst();
                if (first.a < first.b) {
                    first.swap();
                    set.add(first);
                } else {
                    break;
                }
            }
            if (set.size() < n) {
                break;
            }
            ans = Math.min(ans, set.last().a - begin);
        }
        out.println(ans);
    }
}

class Item {
    int id;
    int a;
    int b;

    public Item(int a, int b) {
        this.a = a;
        this.b = b;
        if (a > b) {
            swap();
        }
    }

    void swap() {
        int tmp = a;
        a = b;
        b = tmp;
    }
}
