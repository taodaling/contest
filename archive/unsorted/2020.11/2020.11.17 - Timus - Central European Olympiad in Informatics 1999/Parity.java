package contest;

import template.datastructure.XorDeltaDSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class Parity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if (n == -1) {
            throw new UnknownError();
        }
        int m = in.readInt();
        int[] left = new int[m];
        int[] right = new int[m];
        int[] xor = new int[m];
        for (int i = 0; i < m; i++) {
            left[i] = in.readInt() - 1;
            right[i] = in.readInt();
            xor[i] = in.readString().equals("odd") ? 1 : 0;
        }
        IntegerArrayList list = new IntegerArrayList(m * 2);
        list.addAll(left);
        list.addAll(right);
        list.unique();
        for (int i = 0; i < m; i++) {
            left[i] = list.binarySearch(left[i]);
            right[i] = list.binarySearch(right[i]);
        }

        XorDeltaDSU dsu = new XorDeltaDSU(list.size());

        dsu.init();
        int ans = m;
        for (int i = 0; i < m; i++) {
            int l = left[i];
            int r = right[i];
            int val = xor[i];
            if (dsu.find(l) == dsu.find(r)) {
                if (dsu.delta(l, r) != val) {
                    ans = Math.min(ans, i);
                }
            } else {
                dsu.merge(l, r, val);
            }
        }
        out.println(ans);
    }
}
