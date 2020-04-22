package contest;

import template.utils.Debug;

public class ArpaAsDevotee {
    Debug debug = new Debug(true);
    public String[] solve(int n, int q, int[] a, int[] lastSeen, int[] t) {
        int[] status = new int[30];
        boolean valid = true;
        for (int i = 0; i < n; i++) {
            valid = valid && lastSeen[i] <= a[i];
            for (int j = lastSeen[i] + 1; j <= a[i]; j++) {
                valid = valid && status[j] != 1;
                status[j] = -1;
            }
            valid = valid && status[lastSeen[i]] != -1;
            status[lastSeen[i]] = 1;
        }
        if (!valid) {
            return new String[0];
        }
        debug.debug("status", status);
        String[] ans = new String[q];
        for (int i = 0; i < q; i++) {
            if (status[t[i]] == 1) {
                ans[i] = "Yes";
            } else if (status[t[i]] == -1) {
                ans[i] = "No";
            } else {
                ans[i] = "Not Sure";
            }
        }

        return ans;
    }
}
