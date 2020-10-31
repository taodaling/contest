package on2020_10.on2020_10_24_AtCoder___AtCoder_Regular_Contest_106.E___Medals;



import template.algo.IntBinarySearch;
import template.binary.Bits;
import template.io.FastInput;
import template.polynomial.FastWalshHadamardTransform;

import java.io.PrintWriter;
import java.util.Arrays;

public class EMedals {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] type = new int[4000000];
        for (int i = 0; i < n; i++) {
            int y = a[i];
            for (int j = 0; j < type.length; j += 2 * y) {
                for (int t = 0; t < y && j + t < type.length; t++) {
                    type[j + t] |= 1 << i;
                }
            }
        }
        int[] nearby = new int[1 << n];
        int mask = (1 << n) - 1;
        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                Arrays.fill(nearby, 0);
                for (int i = 0; i < mid; i++) {
                    nearby[type[i]]++;
                }
                FastWalshHadamardTransform.orFWT(nearby, 0, nearby.length - 1);
                for (int i = 1; i < nearby.length; i++) {
                    int intersect = mid - nearby[mask - i];
                    if(intersect < Integer.bitCount(i) * k){
                        return false;
                    }
                }
                return true;
            }
        };

        //boolean cr = ibs.check(10);
        int ans = ibs.binarySearch(0, type.length);
        out.println(ans);
    }
}
