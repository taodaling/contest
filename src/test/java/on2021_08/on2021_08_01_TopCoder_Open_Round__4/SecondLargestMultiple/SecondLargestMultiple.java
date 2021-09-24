package on2021_08.on2021_08_01_TopCoder_Open_Round__4.SecondLargestMultiple;



import template.binary.Bits;
import template.binary.FixedSizeSubsetGenerator;
import template.math.DigitUtils;
import template.math.PermutationUtils;
import template.primitve.generated.datastructure.LongComparator;
import template.primitve.generated.datastructure.LongFixedMinHeap;
import template.primitve.generated.datastructure.LongHashMap;

public class SecondLargestMultiple {
    public static void main(String[] args) {
        long ans = 0;
        for (int i = 0; i < 1 << 12; i++) {
            int n = Integer.bitCount(i);
            if (n > 6) {
                continue;
            }
            ans += fact(n);
        }
        System.out.println(ans);
    }

    public static long fact(int n) {
        return n == 0 ? 1 : fact(n - 1) * n;
    }

    public static long comb(long n, long m) {
        return m == 0 ? 1 : comb(n - 1, m - 1) * n / m;
    }

    public long insert(long x, int a) {
        int high = DigitUtils.highBit(x);
        int low = DigitUtils.lowBit(x);
        if (high < a) {
            low = high;
            high = a;
        } else if (low < a) {
            low = a;
        }
        return DigitUtils.asLong(high, low);
    }

    public long key(long remainder, long bit) {
        return (remainder << 12) | bit;
    }

    LongHashMap map = new LongHashMap((int) 1e6, false);

    public long find(long N, int B) {
        if (N >= 1e15) {
            return -1;
        }

        long defaultVal = DigitUtils.asLong(-1, -1);
        FixedSizeSubsetGenerator gen = new FixedSizeSubsetGenerator();
        LongFixedMinHeap heap = new LongFixedMinHeap(2, LongComparator.REVERSE_ORDER);
        heap.add(0);
        map.put(key(0, 0), insert(defaultVal, 0));
        int mask = (1 << B) - 1;
        for (int take = 1; take <= B / 2; take++) {
            gen.init(mask, take);
            int[] buf = new int[take];
            while (gen.next()) {
                int wpos = 0;
                int m = gen.mask();
                for (int i = 0; i < B; i++) {
                    if (Bits.get(m, i) == 1) {
                        buf[wpos++] = i;
                    }
                }
                do {
                    int val = 0;
                    for (int i = 0; i < take; i++) {
                        val = val * B + buf[i];
                    }
                    long key = key(val % N, m);
                    map.put(key, insert(map.getOrDefault(key, defaultVal), val));
                } while (PermutationUtils.nextPermutation(buf));
            }
        }
        int test = 0;
        for (int set = 1; set < 1 << B; set++) {
            int bc = Integer.bitCount(set);
            int take = (bc + 1) / 2;
            gen.init(set, take);
            int[] buf = new int[take];
            while (gen.next()) {
                int wpos = 0;
                int m = gen.mask();
                for (int i = 0; i < B; i++) {
                    if (Bits.get(m, i) == 1) {
                        buf[wpos++] = i;
                    }
                }
                do {
                    if (buf[0] == 0) {
                        continue;
                    }
                    long val = 0;
                    for (int i = 0; i < take; i++) {
                        val = val * B + buf[i];
                    }
                    for (int i = 0; i < bc - take; i++) {
                        val = val * B;
                    }
                    long key = key((N - val % N) % N, set ^ m);
                    test++;
                    long res = map.getOrDefault(key, defaultVal);
                    long high = DigitUtils.highBit(res);
                    long low = DigitUtils.lowBit(res);
                    if (high != -1) {
                        heap.add(val + high);
                    }
                    if (low != -1) {
                        heap.add(val + low);
                    }
                } while (PermutationUtils.nextPermutation(buf));
            }
        }
        System.out.println(map.size());
        System.out.println(test);
        if (heap.size() < 2) {
            return -1;
        }
        return heap.getKthSmallest(1);
    }
}
