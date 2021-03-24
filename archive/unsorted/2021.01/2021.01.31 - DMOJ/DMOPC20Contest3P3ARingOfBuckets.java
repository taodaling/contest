package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;
import template.utils.SortUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DMOPC20Contest3P3ARingOfBuckets {
    int mod = (int) 1e9 + 7;
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        long b = in.ri();

        List<Item> list = new ArrayList<>(3);
        {
            //period 1
            if (m % (k + 2) == 0) {
                Item item = new Item();
                long x = m / (k + 2);
                item.seq = new long[]{x, x, x, x, x, x};
                if (b == 1) {
                    item.value = n % mod;
                } else {
                    item.value = (long) (power.pow((int) b, n) - 1) * power.inverse((int) b - 1) % mod * x;
                }
                item.value = DigitUtils.mod(item.value, mod);
                list.add(item);
            }
        }
        {
            //period 2
            if (n % 2 == 0 && m % 2 == 0 && k == 2) {
                long x = 0;
                long y = m / 2;
                Item item = new Item();
                item.seq = new long[]{x, y, x, y, x, y};
                if (b == 1) {
                    item.value = (x + y) * (n / 2) % mod;
                } else {
                    long b2 = (long) b * b % mod;
                    item.value = (x + y * b) % mod * (power.pow((int) b2, n / 2 - 1 + 1) - 1) % mod *
                            power.inverse((int) b2 - 1) % mod;
                }
                item.value = DigitUtils.mod(item.value, mod);
                list.add(item);
            }
        }
        {
            //period 3
            if (n % 3 == 0 && k == 1) {
                long x = 0;
                long y = 0;
                long z = m;
                Item item = new Item();
                item.seq = new long[]{x, y, z, x, y, z};
                if (b == 1) {
                    item.value = (x + y + z) * (n / 3) % mod;
                } else {
                    long b3 = (long) b * b % mod * b % mod;
                    item.value = (x + y * b % mod + z * b % mod * b % mod) % mod * (power.pow((int) b3, n / 3 - 1 + 1) - 1) % mod *
                            power.inverse((int) b3 - 1) % mod;
                }
                item.value = DigitUtils.mod(item.value, mod);
                list.add(item);
            }
        }

        list.sort((x, y) -> SortUtils.compareArray(x.seq, 0, 5, y.seq, 0, 5));
        if(list.isEmpty()){
            out.println(-1);
            return;
        }
        out.println(list.get(0).value);
    }
}

class Item {
    long[] seq;
    long value;
}
