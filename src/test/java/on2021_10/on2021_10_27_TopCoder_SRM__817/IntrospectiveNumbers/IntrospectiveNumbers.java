package on2021_10.on2021_10_27_TopCoder_SRM__817.IntrospectiveNumbers;



import template.binary.Bits;
import template.math.DigitUtils;
import template.math.EulerSieve;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class IntrospectiveNumbers {
    public String nth(long index) {
        if(index == 0){
            return "1";
        }
        List<Item> items = new ArrayList<>();
        for (int i = 1; i < 1 << 10; i++) {
            if (Bits.get(i, 0) == 1) {
                continue;
            }
            Item item = new Item();
            item.bit = i;
            item.initData();
            item.length = Arrays.stream(item.data).sum();
            items.add(item);
        }
//        debug.debug("items", items);
        Map<Integer, List<Item>> groupByLength = items.stream().collect(Collectors.groupingBy(x -> x.length));
        long k = index + 1;
        int length = 0;
        while (true) {
            List<Item> list = groupByLength.getOrDefault(length, Collections.emptyList());
            long sum = 0;
            for (Item item : list) {
                item.initData();
                sum = safeadd(sum, way(item.data));
            }
//            debug.debug("length", length);
//            debug.debug("sum", sum);
            if (sum < k) {
                k -= sum;
                length++;
            } else {
                break;
            }
        }
//        debug.debug("length", length);
        Item[] consider = groupByLength.get(length).stream().toArray(n -> new Item[n]);
//        debug.debug("consider", consider);
//        debug.debug("k", k);
        BigInteger low = BigInteger.valueOf(10).pow(length - 1);
        BigInteger high = BigInteger.valueOf(10).multiply(low).subtract(BigInteger.ONE);
        while (low.compareTo(high) < 0) {
//            debug.debug("low", low);
//            debug.debug("high", high);
            BigInteger mid = low.add(high).add(BigInteger.ONE).shiftRight(1);
            if (check(consider, mid) >= k) {
                high = mid.subtract(BigInteger.ONE);
            } else {
                low = mid;
            }
        }

        return low.toString();
    }
//    Debug debug = new Debug(false);

    public long check(Item[] items, BigInteger mid) {
        char[] data = mid.toString().toCharArray();
        for (int i = 0; i < data.length; i++) {
            data[i] -= '0';
        }

        long sum = 0;
        for(Item item : items){
            sum = safeadd(sum, calc(item, data));
        }
        return sum;
    }

    public long calc(Item item, char[] data) {
        item.initData();
        long way = 0;
        int[] val = item.data;
        for (int i = 0; i < data.length; i++) {
            //first diff
            for (int j = 0; j < data[i]; j++) {
                if (val[j] > 0) {
                    val[j]--;
                    way = safeadd(way, way(val));
                    val[j]++;
                }
            }

            //otherwise the same
            val[data[i]]--;
            if (val[data[i]] < 0) {
                break;
            }
        }
        return way;
    }

    int[] pow;
    EulerSieve sieve = new EulerSieve(45);
    int[] primes;

    {
        int n = sieve.getPrimeCount();
        pow = new int[n];
        IntegerArrayList primes = new IntegerArrayList(n);
        for (int i = 0; i <= 45; i++) {
            if (sieve.isPrime(i)) {
                primes.add(i);
            }
        }
        this.primes = primes.toArray();
    }

    private void contrib(int n, int sign) {
        for (int i = 0; i < primes.length && primes[i] <= n; i++) {
            int k = primes[i];
            int m = n;
            while (m >= k) {
                m /= k;
                pow[i] += sign * m;
            }
        }
    }

    long inf = (long) 2e18;

    public long safemul(long a, long b) {
        return DigitUtils.mul(a, b, inf, inf);
    }

    public long safeadd(long a, long b) {
        return Math.min(a + b, inf);
    }

    public long way(int[] fact) {
        Arrays.fill(pow, 0);
        int sum = Arrays.stream(fact).sum();
        contrib(sum, 1);
        for (int i = 0; i < fact.length; i++) {
            contrib(fact[i], -1);
        }
        long ans = 1;
        for (int i = 0; i < primes.length; i++) {
            while (pow[i] > 0) {
                ans = safemul(ans, primes[i]);
                pow[i]--;
            }
        }
        return ans;
    }
}

class Item {
    int bit;
    int length;
    int[] data = new int[10];

    public void initData() {
        for (int i = 0; i < 10; i++) {
            data[i] = Bits.get(bit, i) * i;
        }
    }

    @Override
    public String toString() {
        return Integer.toBinaryString(bit);
    }
}
