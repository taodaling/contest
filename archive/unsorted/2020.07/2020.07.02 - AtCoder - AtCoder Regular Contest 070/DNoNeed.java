package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

public class DNoNeed {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntegerArrayList list = new IntegerArrayList(n);
        int k = in.readInt();

        int ans = 0;
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            if (x >= k) {
                ans++;
                continue;
            }
            list.add(x);
        }

        int[] data = list.toArray();
        int[] revData = data.clone();
        SequenceUtils.reverse(revData);

        BitSet[] l2r = gen(data, k);
        BitSet[] r2l = gen(revData, k);
        SequenceUtils.reverse(r2l, 0, r2l.length - 1);
        for (int i = 0; i < data.length; i++) {
            BitSet left = l2r[i];
            BitSet right = r2l[i + 1];
            int min = find(left, right, k - data[i]);
            if (min < k) {
                ans++;
            }
        }

        out.println(n - ans);
    }


    public int find(BitSet a, BitSet b, int threshold) {
        int m = a.capacity();
        int l = 0;
        int r = m - 1;
        int ans = m;
        while (l < m && r >= 0) {
            if (!a.get(l)) {
                l++;
                continue;
            }
            if (!b.get(r)) {
                r--;
                continue;
            }
            if (l + r >= threshold) {
                ans = Math.min(ans, l + r);
                r--;
            } else {
                l++;
            }
        }

        return ans;
    }

    public BitSet[] gen(int[] data, int k) {
        int n = data.length;
        BitSet[] ans = new BitSet[n + 1];
        ans[0] = new BitSet(k);
        ans[0].set(0);
        for (int i = 0; i < n; i++) {
            ans[i + 1] = new BitSet(ans[i]);
            ans[i + 1].rightShift(data[i]);
            ans[i + 1].or(ans[i]);
        }
        return ans;
    }
}
