package on2021_07.on2021_07_22_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.F__Pairwise_Modulo;



import template.io.FastInput;
import template.io.FastOutput;

public class FPairwiseModulo {
    public int sum(int[] a, int l, int r) {
        int ans = a[r];
        if (l > 0) {
            ans -= a[l - 1];
        }
        return ans;
    }

    public long sum(long[] a, int l, int r) {
        long ans = a[r];
        if (l > 0) {
            ans -= a[l - 1];
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int L = (int) 3e5;

        int[] cnt = new int[L + 1];
        long[] sum = new long[L + 1];
        int[] cntPs = new int[L + 1];
        long[] sumPs = new long[L + 1];
        long prev = 0;
        int sqrt = 500;
        int counter = 0;
        int[] buf = new int[sqrt];
        for (int x : a) {
            long part1 = 0;
            //ai mod x
            for (int i = 0; i * x <= L; i++) {
                int l = i * x;
                int r = Math.min(L, l + x - 1);
                part1 += sum(sumPs, l, r);
                part1 -= (long) sum(cntPs, l, r) * i * x;
            }
            long part2 = (long) x * sum(cntPs, 0, L);
            for (int i = 1, r; i <= x; i = r + 1) {
                int div = x / i;
                r = x / div;
                part2 -= sum(sumPs, i, r) * div;
            }
            for (int i = 0; i < counter; i++) {
                part1 += buf[i] % x;
                part2 += x % buf[i];
            }
            buf[counter++] = x;
            cnt[x]++;
            sum[x] += x;
            prev += part1 + part2;
            out.append(prev).append(' ');

            if (counter == sqrt) {
                counter = 0;
                for (int i = 0; i <= L; i++) {
                    cntPs[i] = cnt[i];
                    sumPs[i] = sum[i];
                    if (i > 0) {
                        cntPs[i] += cntPs[i - 1];
                        sumPs[i] += sumPs[i - 1];
                    }
                }
            }
        }

    }
}
