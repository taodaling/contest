package contest;

import template.datastructure.IntEntryIterator;
import template.datastructure.IntHashMap;
import template.datastructure.LongObjectHashMap;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[1000000];
        IntHashMap map = new IntHashMap(n, true);
        for (int i = 0; i < n; i++) {
            int m = in.readString(s, 0);
            int cnt = 0;
            for (int j = 0; j < m; j++) {
                if (s[j] == '(') {
                    cnt++;
                } else {
                    cnt--;
                }
            }

            boolean valid = true;
            int left = cnt >= 0 ? 0 : -cnt;
            for (int j = 0; j < m; j++) {
                if (s[j] == '(') {
                    left++;
                } else {
                    left--;
                    valid = valid && left >= 0;
                }
            }

            if (!valid) {
                continue;
            }
            map.put(cnt, map.getOrDefault(cnt, 0) + 1);
        }

        int ans = 0;
        for (IntEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            if (iterator.getEntryKey() >= 0) {
                continue;
            }
            ans += Math.min(iterator.getEntryValue(), map.getOrDefault(-iterator.getEntryKey(), 0));
        }

        ans += map.getOrDefault(0, 0) / 2;
        out.println(ans);
    }
}
