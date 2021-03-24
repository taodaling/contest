package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.utils.SortUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.BitSet;

public class BPresent {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] cnt = new int[2];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            cnt[a[i] % 2]++;
        }

        Randomized.shuffle(a);
        Arrays.sort(a);

        int ans = 0;
        if ((cnt[0] % 2) * (cnt[1] % 2) == 1) {
            ans = 1;
        }
        IntegerList[] list = new IntegerList[]{new IntegerList(n), new IntegerList(n)};
        int mask = 1;
        for (int i = 1; i < 30; i++) {
            mask = Bits.setBit(mask, i - 1, true);
            list[0].clear();
            list[1].clear();
            for (int x : a) {
                list[Bits.bitAt(x, i)].add(x & mask);
            }
            for (int j = 0; j < 2; j++) {
                SortUtils.radixSort(list[j].getData(), 0, list[j].size() - 1);
            }
            debug.debug("list", list);
            long total = checkExceed(list[0], mask) + checkExceed(list[1], mask);
            total += checkNotExceed(list[0], list[1], mask);
            if(total % 2 == 1){
                ans = Bits.setBit(ans, i, true);
            }
        }

        out.println(ans);
    }

    public long checkNotExceed(IntegerList a, IntegerList b, int mask) {
        int na = a.size();
        int nb = b.size();
        int[] da = a.getData();
        int[] db = b.getData();
        long ans = 0;
        for (int l = 0, r = na - 1; l < nb; l++) {
            while (r >= 0 && db[l] + da[r] > mask) {
                r--;
            }
            ans += r + 1;
        }
        return ans;
    }

    public long checkExceed(IntegerList list, int mask) {
        int n = list.size();
        int[] data = list.getData();
        long cnt = 0;
        for (int i = n - 1, l = 0; i >= 1; i--) {
            if (data[i] + data[i - 1] <= mask) {
                break;
            }
            while (l + 1 <= i && data[l] + data[i] <= mask) {
                l++;
            }
            cnt += Math.max(0, i - l);
        }
        return cnt;
    }
}
