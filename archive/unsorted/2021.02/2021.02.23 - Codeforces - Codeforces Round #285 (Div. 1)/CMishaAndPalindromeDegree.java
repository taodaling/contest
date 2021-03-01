package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.function.IntPredicate;

public class CMishaAndPalindromeDegree {
    Debug debug = new Debug(true);
    boolean changeCenter = false;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();

        rbegin = n / 2;
        if (n % 2 == 1) {
            rbegin++;
        }
        int[] a = in.ri(n);
        lmatch = new boolean[n];
        cnts = new int[n + 1];
        occur = new int[n + 1];
        for (int i = 0; i < n; i++) {
            add(a[i], 1, 0);
        }
        int deserveCenter = -1;
        int oddCnt = 0;
        for (int i = 1; i <= n; i++) {
            oddCnt += cnts[i] & 1;
            if (cnts[i] % 2 == 1) {
                deserveCenter = i;
            }
        }
        if (deserveCenter != -1 && a[n / 2] != deserveCenter) {
            changeCenter = true;
        }
        if (oddCnt > 1) {
            out.println(0);
            return;
        }

        for (int i = 0; i < n; i++) {
            lmatch[i] = a[i] == a[mirror(i)];
        }
        rmatch = lmatch.clone();
        mmatch = lmatch.clone();
        for (int i = 1; i < n; i++) {
            lmatch[i] = lmatch[i] && lmatch[i - 1];
        }
        for (int i = n - 2; i >= 0; i--) {
            rmatch[i] = rmatch[i] && rmatch[i + 1];
        }
        for (int i = rbegin - 2; i >= 0; i--) {
            mmatch[i] = mmatch[i] && mmatch[i + 1];
        }

        lmajor = new boolean[n];
        rmajor = new boolean[n];
        for (int i = 0; i < n; i++) {
            add(a[i], 0, 1);
            lmajor[i] = major > 0;
        }
        for (int i = 0; i < n; i++) {
            add(a[i], 0, -1);
        }
        for (int i = n - 1; i >= 0; i--) {
            add(a[i], 0, 1);
            rmajor[i] = major > 0;
        }
        for (int i = 0; i < n; i++) {
            add(a[i], 0, -1);
        }
        for(int i = 0; i < rbegin; i++){
            add(a[i], 0, 1);
        }
        if(deserveCenter != -1) {
            add(deserveCenter, 0, -1);
        }
        isLeftMajor = major > 0;
        for(int i = 0; i < rbegin; i++){
            add(a[i], 0, -1);
        }
        if(deserveCenter != -1) {
            add(deserveCenter, 0, 1);
        }
        for(int i = rbegin - n % 2; i < n; i++){
            add(a[i], 0, -1);
        }
        if(deserveCenter != -1) {
            add(deserveCenter, 0, -1);
        }
        isRightMajor = major > 0;


        long sum = 0;
        for (int i = 0; i < n; i++) {
            int finalI = i;
            IntPredicate predicate = j -> check(finalI, j);
            int to = BinarySearch.firstTrue(predicate, i, n - 1);
            debug.debug("i", i);
            debug.debug("to", to);
            sum += n - to;
        }
        out.println(sum);
    }

    boolean isRightMajor;
    boolean isLeftMajor;

    public boolean check(int l, int r) {
        if (changeCenter && (r < rbegin - 1 || l >= rbegin)) {
            return false;
        }
        int ml = mirror(l);
        int mr = mirror(r);
        if (r >= rbegin && l < rbegin) {
            int L = Math.min(l, mr);
            int R = Math.max(r, ml);
            if (L > 0) {
                if (!(lmatch[L - 1] && rmatch[R + 1])) {
                    return false;
                }
            }
            if (L == l) {
                if (r + 1 < n && rmajor[r + 1]) {
                    return false;
                }
            } else {
                if (l - 1 >= 0 && lmajor[l - 1]) {
                    return false;
                }
            }
            return true;
        }
        if (l >= rbegin) {
            return check(mirror(r), mirror(l));
        }
        if (r < rbegin - 1 && !mmatch[r + 1]) {
            return false;
        }
        if (l > 0 && !lmatch[l - 1]) {
            return false;
        }
        return !isLeftMajor && !isRightMajor;
    }

    int rbegin;
    int n;
    boolean[] mmatch;
    boolean[] lmatch;
    boolean[] rmatch;
    boolean[] lmajor;
    boolean[] rmajor;

    int mirror(int x) {
        return n - 1 - x;
    }

    int[] cnts;
    int[] occur;
    int major;

    public void add(int x, int a, int b) {
        if (occur[x] * 2 > cnts[x]) {
            major--;
        }
        cnts[x] += a;
        occur[x] += b;
        if (occur[x] * 2 > cnts[x]) {
            major++;
        }
    }

}
