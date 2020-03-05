package on2020_03.on2020_03_05_Dwango_Programming_Contest_V.B___Sum_AND_Subarrays;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongList;
import template.primitve.generated.datastructure.LongPreSum;

public class BSumANDSubarrays {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        LongPreSum ps = new LongPreSum(a);
        LongList list = new LongList(n * n / 2);
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                list.add(ps.intervalSum(i, j));
            }
        }
        long[] data = list.toArray();
        int m = data.length;
        long mask = 0;
        for (int i = 60; i >= 0; i--) {
            long test = Bits.setBit(mask, i, true);
            int cnt = 0;
            for (long x : data) {
                if (Bits.subset(test, x)) {
                    cnt++;
                }
            }

            if (cnt >= k) {
                mask = test;
            }
        }

        out.println(mask);
    }

}
