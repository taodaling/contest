package on2021_07.on2021_07_22_Codeforces___Codeforces_Round__552__Div__3_.G__Minimum_Possible_LCM;



import template.io.FastInput;
import template.io.FastOutput;

public class GMinimumPossibleLCM {
    void add(int[][] pq, int i, int x) {
        int size = pq[2][i];
        if (size < 2) {
            pq[size][i] = x;
            pq[2][i]++;
            if (size == 1 && pq[1][i] < pq[0][i]) {
                int tmp = pq[0][i];
                pq[0][i] = pq[1][i];
                pq[1][i] = tmp;
            }
        } else {
            if (pq[0][i] > x) {
                pq[1][i] = pq[0][i];
                pq[0][i] = x;
            } else if (pq[1][i] > x) {
                pq[1][i] = x;
            }
        }
    }

    void addAll(int[][] data, int a, int b) {
        for (int i = 0; i < data[2][b]; i++) {
            add(data, a, data[i][b]);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int L = (int) 1e7;
        int[] a = in.ri(n);
        int[][] pq = new int[3][L + 1];
        for (int i = 0; i < n; i++) {
            add(pq, a[i], a[i]);
        }
        for (int i = 1; i <= L; i++) {
            for (int j = i + i; j <= L; j += i) {
                addAll(pq, i, j);
            }
        }
//        EratosthenesSieve.sieve(L, p -> {
//            for (int i = L / p; i >= 1; i--) {
//                addAll(pq, i, i * p);
//            }
//        });
        long lcm = (long) 1e18;
        int x = 0;
        int y = 0;
        for (int i = 1; i <= L; i++) {
            if (pq[2][i] == 2) {
                long cand = (long) pq[0][i] * pq[1][i] / i;
                if (cand < lcm) {
                    lcm = cand;
                    x = -pq[0][i];
                    y = -pq[1][i];
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (a[i] == -x) {
                x = i;
            } else if (a[i] == -y) {
                y = i;
            }
        }
        if (x > y) {
            x ^= y;
            y ^= x;
            x ^= y;
        }

        out.append(x + 1).append(' ').append(y + 1);
    }
}