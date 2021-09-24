package on2021_09.on2021_09_17_Single_Round_Match_813.PrettyPrimes;



import template.math.LongMillerRabin;
import template.utils.Debug;

import java.util.HashSet;
import java.util.Set;

public class PrettyPrimes {
    int mod = (int) 1e9 + 7;
    int best = -1;
    long sum = 0;

    public int solve(int pattern, int D) {
        int[] digit = new int[D];
        if(paint(digit, 0, D - 1, pattern)) {
            update(digit, pattern);
        }

        for (int i = 0; i < D; i++) {
            if (!paint(digit, 0, i - 1, pattern) || !paint(digit, i + 1, D - 1, pattern)) {
                continue;
            }
            for (int j = 0; j < 10; j++) {
                digit[i] = j;
                update(digit, pattern);
            }
        }
        for (int i = 0; i < D; i++) {
            for (int i2 = i + 1; i2 < D; i2++) {
                if (!paint(digit, 0, i - 1, pattern) || !paint(digit, i + 1, i2 - 1, pattern)
                        || !paint(digit, i2 + 1, D - 1, pattern)) {
                    continue;
                }
                for (int j = 0; j < 10; j++) {
                    for (int k = 0; k < 10; k++) {
                        digit[i] = j;
                        digit[i2] = k;
                        update(digit, pattern);
                    }
                }
            }
        }
        for (int i = 0; i < D; i++) {
            for (int i2 = i + 1; i2 < D; i2++) {
                for (int i3 = i2 + 1; i3 < D; i3++) {
                    if (!paint(digit, 0, i - 1, pattern) || !paint(digit, i + 1, i2 - 1, pattern)
                            || !paint(digit, i2 + 1, i3 - 1, pattern)
                            || !paint(digit, i3 + 1, D - 1, pattern)) {
                        continue;
                    }
                    for (int j = 0; j < 10; j++) {
                        for (int k = 0; k < 10; k++) {
                            for (int z = 0; z < 10; z++) {
                                digit[i] = j;
                                digit[i2] = k;
                                digit[i3] = z;
                                update(digit, pattern);
                            }
                        }
                    }
                }
            }
        }
//        debug.debug("best", best);
        sum %= mod;
        return (int) sum;
    }

//    Debug debug = new Debug(false);
    Set<Long> consider = new HashSet<>();

    public void update(int[] digit, int x) {
        if (digit[0] == 0) {
            return;
        }
        int cnt = calc(digit, x);
        if (cnt < best) {
            return;
        }
        long v = eval(digit);
        if (consider.contains(v)) {
            return;
        }
        consider.add(v);
        if (!LongMillerRabin.mr(v, 10)) {
            return;
        }

        if (cnt > best) {
            best = cnt;
            sum = 0;
        }
        sum = sum + v;
    }

    public boolean paint(int[] digit, int l, int r, int x) {
        if (x >= 10) {
            if ((r - l + 1) % 2 != 0) {
                return false;
            }
            int base = 10;
            for (int i = l; i <= r; i++) {
                digit[i] = x / base % 10;
                base = base == 10 ? 1 : 10;
            }
        } else {
            for (int i = l; i <= r; i++) {
                digit[i] = x;
            }
        }
        return true;
    }

    public long eval(int[] digit) {
        long ans = 0;
        for (int x : digit) {
            ans = ans * 10 + x;
        }
        return ans;
    }

    public int calc(int[] digit, int x) {
        int ans = 0;
        if (x < 10) {
            for (int i = 0; i < digit.length; i++) {
                if (digit[i] == x) {
                    ans++;
                }
            }
        } else {
            int high = x / 10;
            int low = x % 10;
            for (int i = 1; i < digit.length; i++) {
                if (digit[i - 1] == high && digit[i] == low) {
                    ans++;
                }
            }
        }
        return ans;
    }
}
