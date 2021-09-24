package on2021_08.on2021_08_22_TopCoder_SRM__811.MonotoneStrings;



import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class MonotoneStrings {
    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial((int) 1e4, mod);
    Combination comb = new Combination(fact);

    public int eval(char c) {
        if (c == '?') {
            return 1;
        } else if (c == '-') {
            return 10;
        } else if (c == '+') {
            return 100;
        }
        return 0;
    }

    public int count(int S, String pattern) {
        char[] pat = pattern.toCharArray();
        int n = pat.length;
        int[] left = new int[128];
        int[] right = new int[128];
        Arrays.fill(right, -1);
        Arrays.fill(left, n);
        for (int i = 0; i < n; i++) {
            if (eval(pat[i]) == 0) {
                left[pat[i]] = Math.min(left[pat[i]], i);
                right[pat[i]] = Math.max(right[pat[i]], i);
            }
        }
        for (int i = 0; i < 128; i++) {
            if (left[i] < right[i]) {
                for (int j = left[i]; j <= right[i]; j++) {
                    if (eval(pat[j]) == 0 && pat[j] != i) {
                        return 0;
                    }
                    pat[j] = (char) i;
                }
            }
        }
        int used = 0;
        for (int i = 0; i < 128; i++) {
            if (left[i] <= right[i]) {
                used++;
            }
        }
        int remain = S - used;

        long[] prev = new long[remain + 2];
        long[][] cprev = new long[2][remain + 2];
        long[][] cnext = new long[2][remain + 2];
        prev[0] = 1;
        for (int i = 0; i < n; i++) {
            if (eval(pat[i]) == 0) {
                continue;
            }
            int l = i;
            int r = i;
            int len = eval(pat[i]);
            while (r + 1 < n && eval(pat[r + 1]) > 0) {
                r++;
                len += eval(pat[r]);
            }
            i = r;
//            debug.debug("l", l);
//            debug.debug("r", r);
//            debug.debug("len", len);
            SequenceUtils.deepFill(cprev, 0L);

            if (l == 0) {
                len--;
                for (int j = 1; j <= remain + 1; j++) {
                    cprev[1][j] = prev[j - 1];
                }
            } else {
                for (int j = 0; j <= remain + 1; j++) {
                    cprev[0][j] = prev[j];
                }
            }
            while (len > 0) {
//                debug.debug("len", len);
//                debug.debug("cprev", cprev);
                len--;
                SequenceUtils.deepFill(cnext, 0L);
                for (int j = 0; j <= remain + 1; j++) {
                    for (int t = 0; t < 2; t++) {
                        if (cprev[t][j] == 0) {
                            continue;
                        }
                        //continue
                        cnext[t][j] += cprev[t][j];
                        if (j + 1 <= remain + 1) {
                            cnext[1][j + 1] += cprev[t][j];
                        }
                    }
                }
                for (int j = 0; j <= remain + 1; j++) {
                    for (int t = 0; t < 2; t++) {
                        cnext[t][j] = DigitUtils.modWithoutDivision(cnext[t][j], mod);
                    }
                }
                long[][] tmp = cprev;
                cprev = cnext;
                cnext = tmp;
            }
//            debug.debug("len", 0);
//            debug.debug("cprev", cprev);
            //same with end
            //or not

            if (r + 1 < n) {
                SequenceUtils.deepFill(cnext, 0L);

                for (int j = 0; j <= remain + 1; j++) {
                    for (int t = 0; t < 2; t++) {
                        cnext[t][j] += cprev[t][j];
                        if (t == 1 && j > 0) {
                            cnext[t][j - 1] += cprev[t][j];
                        }
                    }
                }
                long[][] tmp = cprev;
                cprev = cnext;
                cnext = tmp;

//                debug.log("shrink");
//                debug.debug("cprev", cprev);
            }

            for (int j = 0; j <= remain + 1; j++) {
                prev[j] = DigitUtils.modWithoutDivision(cprev[0][j] + cprev[1][j], mod);
            }
        }

//        debug.debug("prev", prev);
        long sum = 0;
        for (int i = 0; i <= remain; i++) {
            sum += prev[i] * comb.combination(remain, i) % mod * fact.fact(i) % mod;
        }
        sum = DigitUtils.mod(sum, mod);
        return (int) sum;
    }

//    Debug debug = new Debug(false);
}
