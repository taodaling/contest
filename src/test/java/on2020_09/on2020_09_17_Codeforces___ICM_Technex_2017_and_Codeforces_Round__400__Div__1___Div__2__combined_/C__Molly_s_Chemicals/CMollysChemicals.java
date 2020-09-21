package on2020_09.on2020_09_17_Codeforces___ICM_Technex_2017_and_Codeforces_Round__400__Div__1___Div__2__combined_.C__Molly_s_Chemicals;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongHashMap;

public class CMollysChemicals {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long threshold = (long) 1e15;
        int n = in.readInt();
        int k = in.readInt();
        LongArrayList list = new LongArrayList();
        if (k == 1) {
            list.add(1);
        } else if (k == -1) {
            list.add(-1);
            list.add(1);
        } else {
            long cur = 1;
            while (Math.abs(cur) <= threshold) {
                list.add(cur);
                cur *= k;
            }
        }

        long[] cand = list.toArray();
        long ans = 0;
        LongHashMap map = new LongHashMap((int) 1e6, false);
        map.put(0, 1);
        long ps = 0;
        for (int i = 0; i < n; i++) {
            ps += in.readInt();
            for (long x : cand) {
                long prefix = ps - x;
                ans += map.getOrDefault(prefix, 0);
            }
            map.put(ps, map.getOrDefault(ps, 0) + 1);
        }

        out.println(ans);
    }
}
