package on2020_09.on2020_09_06_2020_TopCoder_Open_Algo.SortInversions;



public class SortInversions {
    public long choose2(long n) {
        return n * (n - 1) / 2;
    }

    public long sum(long l, long r) {
        return (l + r) * (r - l + 1) / 2;
    }

    public long count(int N) {
        long ans = 0;
        long[] begin = new long[13];
        long[] end = new long[13];
        begin[1] = 1;
        end[1] = 9;
        for (int i = 2; i < 13; i++) {
            begin[i] = begin[i - 1] * 10;
            end[i] = begin[i] * 10 - 1;
        }
        for (long i = 1; i <= N; i = i * 10) {
            long l = i;
            long r = Math.min(i * 10 - 1, N);
            ans += choose2(r - l + 1);
            String s = "" + r;
            int len = s.length();
            long pref = 0;
            for (int j = 1; j < len; j++) {
                pref = pref * 10 + s.charAt(j - 1) - '0';
                //100 101 ... 999
                long b = begin[j];
                long e = end[j];

                long ll = b;
                long rr = pref - 1;
                if (ll <= rr) {
                    ans += sum(ll - b + 1, rr - b + 1) * begin[len - j + 1];
                }

                //special
                long cnt = r - (pref * begin[len - j + 1]) + 1;
                ans += cnt * (pref - b + 1);
            }
        }

        return choose2(N) - ans;
    }
}
