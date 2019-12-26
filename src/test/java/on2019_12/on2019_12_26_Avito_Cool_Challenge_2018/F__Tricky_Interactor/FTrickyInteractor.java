package on2019_12.on2019_12_26_Avito_Cool_Challenge_2018.F__Tricky_Interactor;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerEntryIterator;
import template.primitve.generated.IntegerHashMap;

public class FTrickyInteractor {
    FastInput in;
    FastOutput out;
    int n;
    int t;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;

        n = in.readInt();
        t = in.readInt();

        int[] ans = new int[n + 1];
        int preSum = 0;
        for (int i = 1; i <= n; i++) {
            int total = (i + t - queryPrefix(i)) / 2;
            if (total > preSum) {
                ans[i] = 1;
                preSum++;
            }
        }

        out.append("! ");
        for (int i = 1; i <= n; i++) {
            out.append(ans[i]);
        }
        out.println();
        out.flush();
    }

    public int queryPrefix(int p) {
        IntegerHashMap map = new IntegerHashMap(100, true);
        int total = 10;
        for (int i = 1; i <= p && total > 0; i++, total--) {
            int ans = query(i, p);
            query(i, p);
            map.put(ans, map.getOrDefault(ans, 0) + 1);
        }
        for (int i = p + 1; i <= n && total > 0; i++, total--) {
            int ans = n - query(p + 1, i);
            query(p + 1, i);
            map.put(ans, map.getOrDefault(ans, 0) + 1);
        }

        int ans = 0;
        int cnt = 0;
        for (IntegerEntryIterator iterator = map.iterator();
             iterator.hasNext(); ) {
            iterator.next();
            if (cnt < iterator.getEntryValue()) {
                cnt = iterator.getEntryValue();
                ans = iterator.getEntryKey();
            }
        }
        System.err.println(p + "=" + ans);
        return ans;
    }

    public int query(int l, int r) {
        out.append("? ").append(l).append(' ').append(r).println();
        out.flush();
        return in.readInt();
    }
}
