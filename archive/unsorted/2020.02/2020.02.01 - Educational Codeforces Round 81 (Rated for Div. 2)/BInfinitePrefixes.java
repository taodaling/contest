package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerEntryIterator;
import template.primitve.generated.IntegerHashMap;

public class BInfinitePrefixes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);

        IntegerHashMap map = new IntegerHashMap(n, false);
        int[] cnts = new int[2];
        for (int i = 0; i < n; i++) {
            cnts[s[i] - '0']++;
            int delta = cnts[0] - cnts[1];
            map.put(delta, map.getOrDefault(delta, 0) + 1);
        }

        if (cnts[0] - cnts[1] == 0) {
            if (map.containKey(x)) {
                out.println(-1);
                return;
            }
            out.println(x == 0 ? 1 : 0);
            return;
        }


        int ans = 0;
        for (IntegerEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            int k = iterator.getEntryKey();
            int v = iterator.getEntryValue();
            if((x - k) % (cnts[0] - cnts[1]) == 0 && (x - k) / (cnts[0] - cnts[1]) >= 0){
                ans += v;
            }
        }

        if(x == 0){
            ans++;
        }

        out.println(ans);
    }
}
