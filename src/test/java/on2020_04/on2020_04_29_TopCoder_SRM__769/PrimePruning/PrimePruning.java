package on2020_04.on2020_04_29_TopCoder_SRM__769.PrimePruning;



import java.util.Arrays;

public class PrimePruning {

    public String maximize(int N, int E) {
        int len = N - E;
        StringBuilder ans = new StringBuilder(len);

        if (N >= 12077) {
            for (int i = 0; i < len; i++) {
                ans.append('z');
            }
        } else {
            StringBuilder buf = new StringBuilder(N);
            for (int i = 2; buf.length() < N; i++) {
                if (isPrime(i)) {
                    buf.append((char) ('a' + (i % 26)));
                }
            }

            int[][] next = new int[N + 1][26];
            Arrays.fill(next[N], N);
            for (int i = buf.length() - 1; i >= 0; i--) {
                System.arraycopy(next[i + 1], 0, next[i], 0, 26);
                next[i][buf.charAt(i) - 'a'] = i;
            }

            int index = 0;
            while (ans.length() < len) {
                int c = -1;
                for (int i = 25; i >= 0; i--) {
                    if (ans.length() + 1 + N - 1 - next[index][i] >= len) {
                        c = i;
                        break;
                    }
                }
                ans.append((char) ('a' + c));
                index = next[index][c] + 1;
            }
        }

        return ans.toString();
    }

    public boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
