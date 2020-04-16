package on2020_04.on2020_04_16_Codeforces___Codeforces_Round__635__Div__1_.E1__Chiori_and_Doll_Picking__easy_version_0;




import template.binary.Bits;
import template.datastructure.LinearBasis;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.LongList;
import template.utils.Debug;

public class E1ChioriAndDollPickingEasyVersion {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long[] a = new long[n];
        LinearBasis lb = new LinearBasis();
        LongList list = new LongList();
        for (int i = 0; i < n; i++) {
            a[i] = in.readLong();

            if (lb.add(a[i])) {
                list.add(a[i]);
            }
        }

        cnt = new int[m + 1];
        long[] set = lb.toArray();

        debug.debug("set", set);
        for (int i = 0; i < set.length; i++) {
            int exp = m - i - 1;
            if (Bits.bitAt(set[i], exp) == 0) {
                int index = -1;
                for (int j = m - 1; j >= 0; j--) {
                    if (Bits.bitAt(set[i], j) == 1) {
                        index = j;
                        break;
                    }
                }
                for (int j = 0; j < set.length; j++) {
                    set[j] = Bits.swapBit(set[j], exp, index);
                }
            }
        }
        for (long x : set) {
            debug.debug("e", Long.toString(x, 2));
        }
        if (set.length <= 29) {
            dfs(set, 0, set.length - 1);
        } else {

        }

        Power power = new Power(mod);
        int factor = power.pow(2, n - set.length);
        for (int i = 0; i <= m; i++) {
            out.append(mod.mul(factor, cnt[i])).append(' ');
        }

    }

    Modular mod = new Modular(998244353);
    int[] cnt;

    public void dfs(long[] set, )

    public void dfs(long[] set, long xor, int i) {
        if (i < 0) {
            cnt[Long.bitCount(xor)]++;
            return;
        }
        dfs(set, xor, i - 1);
        dfs(set, xor ^ set[i], i - 1);
    }
}
