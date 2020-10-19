package on2020_10.on2020_10_17_Codeforces___Codeforces_Raif_Round_1__Div__1___Div__2_.E__Carrots_for_Rabbits;



import template.io.FastInput;

import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class ECarrotsForRabbits {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        long now = 0;
        PriorityQueue<Item> pq = new PriorityQueue<>(n, (x, y) -> -Long.compare(x.profit, y.profit));
        int cut = k - n;
        for (int i = 0; i < n; i++) {
            now += pow2(a[i]);
            pq.add(new Item(profit(a[i], 0) - profit(a[i], 1), 1, i));
        }
        while (cut > 0) {
            Item head = pq.remove();
            now -= head.profit;
            cut--;
            pq.add(new Item(profit(a[head.index], head.cut) - profit(a[head.index], head.cut + 1), head.cut + 1, head.index));
        }

        out.println(now);
    }

    public long profit(long a, long x) {
        x++;
        if (a <= x) {
            return a;
        }
        long avg = a / x;
        long more = a % x;
        return pow2(avg) * (x - more) + pow2(avg + 1) * more;
    }

    public long pow2(long a) {
        return a * a;
    }
}

class Item {
    long profit;
    int cut;
    int index;


    public Item(long profit, int cut, int index) {
        this.profit = profit;
        this.cut = cut;
        this.index = index;
    }
}