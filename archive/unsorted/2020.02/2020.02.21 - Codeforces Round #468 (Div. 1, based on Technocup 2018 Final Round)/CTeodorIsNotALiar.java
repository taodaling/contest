package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.SequenceUtils;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class CTeodorIsNotALiar {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        IntegerBIT bit = new IntegerBIT(m + 1);
        for (int i = 0; i < n; i++) {
            int l = in.readInt();
            int r = in.readInt();
            bit.update(l, 1);
            bit.update(r + 1, -1);
        }
        int[] seq = new int[m];
        for (int i = 0; i < m; i++) {
            seq[i] = bit.query(i + 1);
        }

        int[] l2r = inc(seq);
        SequenceUtils.reverse(seq, 0, seq.length - 1);
        int[] r2l = inc(seq);
        SequenceUtils.reverse(r2l, 0, seq.length - 1);

        int ans = 0;
        for (int i = 0; i < seq.length; i++) {
            ans = Math.max(l2r[i] + r2l[i] - 1, ans);
        }

        out.println(ans);
    }

    public int[] inc(int[] seq) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        int[] ans = new int[seq.length];
        for (int i = 0; i < seq.length; i++) {
            Map.Entry<Integer, Integer> floor = map.floorEntry(seq[i]);
            if (floor == null) {
                ans[i] = 1;
            } else {
                ans[i] = floor.getValue() + 1;
            }
            while (true) {
                Map.Entry<Integer, Integer> ceil = map.ceilingEntry(seq[i]);
                if (ceil == null || ceil.getValue() > ans[i]) {
                    break;
                }
                map.remove(ceil.getKey());
            }
            map.put(seq[i], ans[i]);
        }
        return ans;
    }
}

