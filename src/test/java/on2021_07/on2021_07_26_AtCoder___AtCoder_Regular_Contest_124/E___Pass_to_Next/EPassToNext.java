package on2021_07.on2021_07_26_AtCoder___AtCoder_Regular_Contest_124.E___Pass_to_Next;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.IntMath;
import template.math.Power;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class EPassToNext {
    int mod = 998244353;
    Power pow = new Power(mod);
    int inv6 = pow.inverse(6);

    private void normal(long[][][] data) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    data[i][j][k] %= mod;
                }
            }
        }
    }

    public long sumOfInterval2(long n) {
        if (n <= 0) {
            return 0;
        }
        return n * (n + 1) % mod * (2 * n + 1) % mod * inv6 % mod;
    }

    public long split(long a) {
        return -sumOfInterval2(a) + IntMath.sumOfInterval(1, a) % mod * a % mod;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        //is first used
        //is next assign
        //is zero occur
        long[][][] prev = new long[2][2][2];
        prev[0][0][0] = 1;
        long[][][] next = new long[2][2][2];
        Debug debug = new Debug(false);
        debug.debug("r", -1);
        debug.debug("prev", prev);
        for (int r = 0; r < n; r++) {
            long x = a[r];
            SequenceUtils.deepFill(next, 0L);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 2; k++) {
                        long way = prev[i][j][k];
                        if (way == 0) {
                            continue;
                        }
                        for (int setSelf = 0; setSelf < 2; setSelf++) {
                            for (int setNext = 0; setNext < 2; setNext++) {
                                for (int passZero = 0; passZero < 2; passZero++) {
                                    if (r > 0 && j == setSelf) {
                                        //impossible
                                        continue;
                                    }
                                    if (setNext == 1 && passZero == 1) {
                                        continue;
                                    }
                                    int ni = r == 0 ? setSelf : i;
                                    int nj = setNext;
                                    int nk = passZero == 1 || k == 1 ? 1 : 0;

                                    long transfer;
                                    if (passZero == 1) {
                                        if (setSelf == 1) {
                                            transfer = x;
                                        } else {
                                            transfer = 1;
                                        }
                                    } else if (setSelf == 1 && setNext == 1) {
                                        transfer = split(x);
                                    } else if (setSelf == 1) {
                                        transfer = (x * x - IntMath.sumOfInterval(1, x)) % mod;
                                    } else if (setNext == 1) {
                                        transfer = IntMath.sumOfInterval(1, x) % mod;
                                    } else {
                                        transfer = x;
                                    }

                                    next[ni][nj][nk] += transfer * way % mod;
                                }
                            }
                        }
                    }
                }
            }

            normal(next);
            long[][][] tmp = prev;
            prev = next;
            next = tmp;


            debug.debug("r", r);
            debug.debug("prev", prev);
        }

        long ans = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int k = 1;
                if (i == j) {
                    continue;
                }
                ans += prev[i][j][k];
            }
        }

        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
