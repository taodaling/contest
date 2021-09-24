package on2021_07.on2021_07_23_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.H__XOR_and_Distance;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Buffer;

public class HXORAndDistance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        k = in.ri();
        items = new Item[1 << k];
        buffer = new Buffer<>(Item::new, x -> {
            x.l = x.r = x.nearest = 0;
        }, 2 << k);
        for (int i = 0; i < n; i++) {
            int a = in.ri();
            Item item = buffer.alloc();
            item.l = item.r = a;
            item.nearest = inf;
            items[a] = item;
        }
        dac(0, items.length - 1, k - 1);
        for(Item item : items){
            out.append(item.nearest).append(' ');
        }
    }

    Item[] items;

    public void dac(int l, int r, int k) {
        if (l >= r) {
            return;
        }
        int m = (l + r) / 2;
        dac(l, m, k - 1);
        dac(m + 1, r, k - 1);
        for (int i = l; i <= m; i++) {
            int mirror = m + 1 + (i - l);
            Item a = items[i];
            Item b = items[mirror];
            items[i] = add(a, b, 0);
            items[mirror] = add(b, a, 1 << k);
            buffer.release(a, b);
        }
    }

    public Item add(Item a, Item b, int xor) {
        if (a == null && b == null) {
            return null;
        }
        Item res = buffer.alloc();
        if (a == null || b == null) {
            Item c = a == null ? b : a;
            res.l = c.l ^ xor;
            res.r = c.r ^ xor;
            res.nearest = c.nearest;
            return res;
        }
        res.nearest = Math.min(a.nearest, b.nearest);
        assert (b.l ^ xor) - (a.r ^ xor) >= 0;
        res.nearest = Math.min(res.nearest, (b.l ^ xor) - (a.r ^ xor));
        res.l = a.l ^ xor;
        res.r = b.r ^ xor;
        return res;
    }

    int inf = (int) 1e9;
    int k;
    Buffer<Item> buffer;

}

class Item {
    int l;
    int r;
    int nearest;

    @Override
    public String toString() {
        return "Item{" +
                "l=" + l +
                ", r=" + r +
                ", nearest=" + nearest +
                '}';
    }
}
