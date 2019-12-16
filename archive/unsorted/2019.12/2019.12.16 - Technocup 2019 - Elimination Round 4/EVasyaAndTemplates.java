package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class EVasyaAndTemplates {
    int[] s = new int[1000000];
    int[] a = new int[1000000];
    int[] b = new int[1000000];
    int[] perm = new int[26];
    int[] used = new int[26];
    int n;
    int m;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readString(s, 0);
        in.readString(a, 0);
        in.readString(b, 0);

        for (int i = 0; i < m; i++) {
            s[i] -= 'a';
            a[i] -= 'a';
            b[i] -= 'a';
        }

        Arrays.fill(perm, -1);
        Arrays.fill(used, -1);
        valid = true;
        handle(0, true, true, true);
        if(!valid){
            Arrays.fill(perm, -1);
            Arrays.fill(used, -1);
            valid = true;
            handle(0, true, true, false);
        }

        if(!valid){
            out.println("NO");
            return;
        }

        out.println("YES");
        for(int i = 0; i < n; i++){
            out.append((char)(perm[i] + 'a'));
        }
        out.println();
    }

    boolean valid;

    private void check(int i, boolean bot, boolean top) {
        if (bot && perm[s[i]] < a[i]) {
            valid = false;
        }
        if (top && perm[s[i]] > b[i]) {
            valid = false;
        }
    }

    public void handle(int i, boolean bot, boolean top, boolean pickUp) {
        if (!valid) {
            return;
        }
        if (i >= m || (!bot && !top)) {
            int usedIter = 0;
            for (int j = 0; j < n; j++) {
                if (perm[j] != -1) {
                    continue;
                }
                while (usedIter < n && used[usedIter] != -1) {
                    usedIter++;
                }
                perm[j] = usedIter++;
            }
            return;
        }

        if (perm[s[i]] != -1) {
            check(i, bot, top);
            handle(i + 1, perm[s[i]] == a[i] && bot, perm[s[i]] == b[i] && top, pickUp);
            return;
        }

        if (bot != top) {
            int j;
            if (bot) {
                j = n - 1;
                for (; used[j] != -1; j--) ;
            } else {
                j = 0;
                for (; used[j] != -1; j++) ;
            }
            perm[s[i]] = j;
            used[j] = s[i];
            check(i, bot, top);
            handle(i + 1, perm[s[i]] == a[i] && bot, perm[s[i]] == b[i] && top, pickUp);
            return;
        }

        int j = -1;
        if (pickUp) {
            for (int k = a[i]; k <= b[i]; k++) {
                if (used[k] == -1 && (j == -1 || j == a[i])) {
                    j = k;
                }
            }
        } else {
            for (int k = b[i]; k >= a[i]; k--) {
                if (used[k] == -1 && (j == -1 || j == b[i])) {
                    j = k;
                }
            }
        }
        if(j == -1){
            valid = false;
            return;
        }
        perm[s[i]] = j;
        used[j] = s[i];

        handle(i + 1, perm[s[i]] == a[i] && bot, perm[s[i]] == b[i] && top, pickUp);
    }
}
