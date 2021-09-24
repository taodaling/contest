package on2021_07.on2021_07_25_AtCoder___AtCoder_Regular_Contest_124.C___LCM_of_GCDs;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerEntryIterator;
import template.primitve.generated.datastructure.IntegerHashMap;

public class CLCMOfGCDs {
    IntegerHashMap map = new IntegerHashMap((int) 1e5, false);

    public void consider(int x) {
        for (int j = 1; j * j <= x; j++) {
            if (x % j != 0) {
                continue;
            }
            map.modify(j, 1);
            if (j * j != x) {
                map.modify(x / j, 1);
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.ri();
            b[i] = in.ri();
        }
        for (int i = 0; i < n; i++) {
            consider(a[i]);
            consider(b[i]);
        }
        IntegerArrayList possibleList = new IntegerArrayList(5000);
        for (IntegerEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            if (iterator.getEntryValue() >= n) {
                possibleList.add(iterator.getEntryKey());
            }
        }
        int[] all = possibleList.toArray();
        long ans = 0;
        for (int x : all) {
            for (int y : all) {
                if (x > y || GCDs.gcd(x, y) > 1 || (long) x * y <= ans) {
                    continue;
                }
                boolean ok = true;
                for (int i = 0; i < n; i++) {
                    if (a[i] % x == 0 && b[i] % y == 0 || a[i] % y == 0 && b[i] % x == 0) {
                        continue;
                    }
                    ok = false;
                    break;
                }
                if (ok) {
                    ans = (long) x * y;
                }
            }
        }
        out.println(ans);
    }
}
