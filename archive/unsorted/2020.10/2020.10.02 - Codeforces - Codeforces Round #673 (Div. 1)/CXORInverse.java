package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerHashMap;

import java.io.PrintWriter;

public class CXORInverse {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        in.populate(a);
        int x = 0;
        for (int i = 29; i >= 0; i--) {
            for(int j = 0; j < n; j++){
                b[j] = a[j] ^ (1 << i);
            }
            if(inversion(a, i) > inversion(b, i)){
                x |= 1 << i;
            }
        }
        out.println(inversionAll(a, x));
        out.println(x);
    }

    int limit = 300000;
    IntegerHashMap map = new IntegerHashMap(limit, false);

    public long inversion(int[] a, int bit) {
        map.clear();
        int preBits = -1 ^ ((1 << (bit + 1)) - 1);
        long ans = 0;
        for (int x : a) {
            int key = x & preBits;
            int val = Bits.get(x, bit);
            if (val == 0) {
                ans += map.getOrDefault(key, 0);
            } else {
                map.put(key, map.getOrDefault(key, 0) + 1);
            }
        }
        return ans;
    }


    public long inversionAll(int[] a, int x) {
        IntegerArrayList list = new IntegerArrayList(limit);
        IntegerBIT bit = new IntegerBIT(limit);
        for (int b : a) {
            list.add(b ^ x);
        }
        map.clear();
        list.unique();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), i);
        }
        bit.clear();
        long ans = 0;
        for (int i = a.length - 1; i >= 0; i--) {
            int val = map.get(a[i] ^ x) + 1;
            ans += bit.query(val - 1);
            bit.update(val, 1);
        }

        return ans;
    }
}
