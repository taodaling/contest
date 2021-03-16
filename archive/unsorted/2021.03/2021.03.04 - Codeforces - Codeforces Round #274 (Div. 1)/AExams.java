package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;

public class AExams {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(in.ri(), in.ri());
        }
        Arrays.sort(items, Comparator.<Item>comparingInt(x -> x.a).thenComparingInt(x -> x.b));
        int day = 0;
        for (Item item : items) {
            if (item.b >= day) {
                day = item.b;
            } else {
                day = item.a;
            }
        }
        out.println(day);
    }
}

class Item {
    int a;
    int b;

    public Item(int a, int b) {
        this.a = a;
        this.b = b;
    }
}