package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.HashData;
import template.rand.PartialHash;
import template.string.Manacher;
import template.utils.Debug;

public class TaskD {
    Debug debug = new Debug(true);
    int limit = 1000100;
    char[] s = new char[limit];
    HashData hd31 = new HashData(limit, (int) 1e9 + 7, 31);
    HashData hd61 = new HashData(limit, (int) 1e9 + 7, 61);
    PartialHash ph31 = new PartialHash(hd31);
    PartialHash ph61 = new PartialHash(hd61);
    PartialHash revPh31 = new PartialHash(hd31);
    PartialHash revPh61 = new PartialHash(hd61);
    int n;

    int mirror(int x) {
        return n - 1 - x;
    }

    boolean isP(int l, int r) {
        int revL = mirror(r);
        int revR = mirror(l);
        return ph31.hash(l, r, false) == revPh31.hash(revL, revR, false) &&
                ph61.hash(l, r, false) == revPh61.hash(revL, revR, false);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readString(s, 0);

        ph31.populate(i -> s[i], 0, n - 1);
        ph61.populate(i -> s[i], 0, n - 1);
        revPh31.populate(i -> s[n - 1 - i], 0, n - 1);
        revPh61.populate(i -> s[n - 1 - i], 0, n - 1);


        int l = 0;
        int r = n - 1;
        while (l < r && s[l] == s[r]) {
            l++;
            r--;
        }

        int pre = r;
        int post = l;
        while (!isP(l, pre)) {
            pre--;
        }
        while (!isP(post, r)) {
            post++;
        }
        if (pre - l > r - post) {
            l = pre;
            r++;
        } else {
            r = post;
            l--;
        }

        for (int i = 0; i <= l; i++) {
            out.append(s[i]);
        }
        for (int i = r; i < n; i++) {
            out.append(s[i]);
        }
        out.println();
    }
}
