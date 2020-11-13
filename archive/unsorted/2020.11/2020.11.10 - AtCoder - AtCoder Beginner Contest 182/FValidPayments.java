package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FValidPayments {
    long[] a;
    Map<Item, Long> map = new HashMap<>();

    public Long solve(Item item) {
        if (!map.containsKey(item)) {
            int len = item.seq.length;
            if (len == 1) {
                map.put(item, 1L);
                return 1L;
            }
            long ans = solve(
                    new Item(
                            Arrays.copyOf(item.seq, len - 1)));
            if (item.seq[len - 1] > 0) {
                //add
                Item clone = new Item(Arrays.copyOf(item.seq, len - 1));
                int l = clone.seq.length;
                clone.seq[l - 1]++;
                for (int i = l - 1; i >= 0 && clone.seq[i] == a[i] / a[i + 1]; i--) {
                    clone.seq[i] = 0;
                    clone.seq[i - 1]++;
                }
                ans += solve(clone);
            }
            map.put(item, ans);
        }
        return map.get(item);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long x = in.readLong();
        a = new long[n + 1];
        for (int i = 0; i < n; i++) {
            a[i] = in.readLong();
        }

        a[n] = (long) 1e18;
        SequenceUtils.reverse(a);

        long[] seq = new long[n];
        for (int i = 0; i < n; i++) {
            seq[i] = x / a[i + 1];
            x %= a[i + 1];
        }
        long ans = solve(new Item(seq));
        out.println(ans);
    }
}

class Item {
    long[] seq;

    public Item(long[] seq) {
        this.seq = seq;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(seq);
    }

    @Override
    public boolean equals(Object obj) {
        Item item = (Item) obj;
        return Arrays.equals(seq, item.seq);
    }

    @Override
    public String toString() {
        return Arrays.toString(seq);
    }
}