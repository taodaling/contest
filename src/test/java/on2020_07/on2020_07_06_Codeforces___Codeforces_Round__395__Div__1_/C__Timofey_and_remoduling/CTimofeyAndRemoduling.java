package on2020_07.on2020_07_06_Codeforces___Codeforces_Round__395__Div__1_.C__Timofey_and_remoduling;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerHashSet;

import java.util.stream.IntStream;

public class CTimofeyAndRemoduling {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        if (n == 1) {
            out.printf("%d %d", a[0], 0);
            return;
        }

        Modular mod = new Modular(m);

        int[] ans;
        if (n * 2 <= m) {
            ans = find(a, mod);
        } else {
            boolean[] appear = new boolean[m];
            for (int x : a) {
                appear[x] = true;
            }
            int[] b = IntStream.range(0, m).filter(i -> !appear[i]).toArray();
            ans = find(b, mod);
            if (ans != null) {
                ans[0] = mod.plus(ans[0], mod.mul(ans[1], b.length));
            }
        }

        if(ans == null){
            out.println(-1);
            return;
        }

        out.printf("%d %d", ans[0], ans[1]);
    }

    public int[] find(int[] data, Modular mod) {
        if (data.length == 0) {
            return new int[]{0, 1};
        }
        if (data.length == 1) {
            return new int[]{data[0], 1};
        }
        int delta = mod.subtract(data[1], data[0]);
        IntegerHashSet set = new IntegerHashSet(data.length, false);
        for (int x : data) {
            set.add(x);
        }
        int occur = 0;
        for (int x : data) {
            if (set.contain(mod.plus(x, delta))) {
                occur++;
            }
        }

        //data[1] = kd + data[0]
        //k, k + 1, ... , n - 1
        //(n - 1 - k + 1) = n - k
        //n - k = occur
        int n = data.length;
        int k = n - occur;
        //kd = delta
        Power power = new Power(mod);
        int d = mod.mul(delta, power.inverse(k));

        //nx+d(n-1)n/2=s
        int s = 0;
        for (int x : data) {
            s = mod.plus(x, s);
        }
        int nx = mod.subtract(s, mod.mul(d, mod.mul(n - 1, mod.mul(n, power.inverse(2)))));
        int x = mod.mul(nx, power.inverse(n));

        //check
        for (int i = 0; i < n; i++) {
            int ai = mod.plus(x, mod.mul(i, d));
            if (!set.contain(ai)) {
                return null;
            }
        }
        return new int[]{x, d};
    }
}
