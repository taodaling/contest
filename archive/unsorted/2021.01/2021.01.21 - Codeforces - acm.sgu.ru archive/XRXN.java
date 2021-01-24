package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;
import java.util.Arrays;

public class XRXN {
    public int[] reverse(int n) {
        if (n == 0) {
            return new int[]{0, 1};
        }
        int tail = n % 10;
        int[] ans = reverse(n / 10);
        ans[0] += ans[1] * tail;
        ans[1] *= 10;
        return ans;
    }

    char[] s = new char[(int) 1e5];
    BigInteger[] way = new BigInteger[19];

    public long bf(int l, int r, boolean headExceed, boolean tailAdded, int[] digits) {
        if (r - l == 0) {
            long ans = 0;
            for (int i = 0; i <= 9; i++) {
                int v = i * 2;
                if (tailAdded) {
                    v += 1;
                }
                if (headExceed) {
                    v -= 10;
                }
                if (v == s[l]) {
                    ans++;
                }
            }
            return ans;
        }
        int target = s[l] * 10 + s[r];
        for (int i = 0; i <= 18; i++) {
            int v = i * 10 + i;
            if (tailAdded) {
                v += 1;
            }
            if (headExceed) {
                v -= 100;
            }
            if (v == target) {
                digits[i]++;
                return 1;
            }
        }
        return 0;
    }

    public long solve(int l, int r, boolean headExceed, boolean tailAdded, int[] digits) {
        long ans = 1;
        while (l <= r) {
            if (r - l <= 1) {
                ans *= bf(l, r, headExceed, tailAdded, digits);
                return ans;
            }

            boolean find = false;
            for (int i = 0; i <= 18; i++) {
                int head = i;
                int tail = i;
                if (headExceed) {
                    head -= 10;
                }
                if (tailAdded) {
                    tail += 1;
                }
                if (tail % 10 == s[r] && (head + 1 == s[l] || head == s[l])) {
                    //find
                    digits[i]++;
                    tailAdded = tail >= 10;
                    headExceed = head + 1 == s[l];
                    find = true;
                    break;
                }
            }

            if (!find) {
                return 0;
            }
            l++;
            r--;
        }
        return ans;
    }

    {
        Arrays.fill(way, BigInteger.ZERO);
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                way[i + j] = way[i + j].add(BigInteger.ONE);
            }
        }
    }

    long[] cache = new long[1000];

    {
        int[] rev = new int[1000];
        for (int i = 0; i < 1000; i++) {
            rev[i] = reverse(i)[0];
        }
        for (int v = 1; v < 1000; v++) {
            for (int j = 1; j <= v; j++) {
                if (j + rev[j] == v) {
                    cache[v]++;
                }
            }
        }
    }

    public BigInteger calc(long x, int[] digits) {
        if (x == 0) {
            return BigInteger.ZERO;
        }
        BigInteger ans = BigInteger.valueOf(x);
        for (int i = 0; i < digits.length; i++) {
            if (digits[i] == 0) {
                continue;
            }
            ans = ans.multiply(way[i].pow(digits[i]));
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s);
        if (n == 1 && s[0] == '0') {
            throw new UnknownError();
        }
        if (n <= 3) {
            int v = Integer.parseInt(new String(s, 0, n));
            out.println(cache[v]);
            return;
        }

        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }
        BigInteger sum = BigInteger.ZERO;
        //same length
        for (int i = 1; i <= 18; i++) {
            if (s[n - 1] == i % 10 && (s[0] == i || s[0] == i + 1)) {
                boolean tailAdded = i >= 10;
                boolean headExceed = s[0] == i + 1;
                int[] digits = new int[19];
                long ans = solve(1, n - 2, headExceed, tailAdded, digits);
                long local = way[i].longValue();
                if (i <= 9) {
                    local--;
                }
                ans *= local;
                sum = sum.add(calc(ans, digits));
                break;
            }
        }

        //differ length
        if (s[0] == 1) {
            for (int i = 1; i <= 18; i++) {
                int head = i - 10;
                int tail = i;
                if (s[n - 1] == tail % 10 && (s[1] == head || s[1] == head + 1)) {
                    boolean tailAdded = tail >= 10;
                    boolean headExceed = s[1] == head + 1;
                    int[] digits = new int[19];
                    long ans = solve(2, n - 2, headExceed, tailAdded, digits);
                    long local = way[i].longValue();
                    if (i <= 9) {
                        local--;
                    }
                    ans *= local;
                    sum = sum.add(calc(ans, digits));
                    break;
                }
            }
        }
        out.println(sum.toString());
    }
}
