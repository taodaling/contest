package on2021_08.on2021_08_22_Single_Round_Match_811.SmoothDivisors;



import java.util.Arrays;

public class SmoothDivisors {
    public int count(int A, int B) {
        int[] factor = new int[B + 1];
        Arrays.fill(factor, -1);
        for (int i = 2; i <= B; i++) {
            if (factor[i] != -1) {
                continue;
            }
            factor[i] = i;
            for (int j = i + i; j <= B; j += i) {
                factor[j] = i;
            }
        }
        int ans = 0;
        for (int i = A; i <= B; i++) {
            boolean ok = true;
            int x = i;
            if (x == 1) {
                ans++;
                continue;
            }
            if (factor[x] != x) {
                int f1 = factor[x];
                int time = 0;
                while (x % f1 == 0 && time < 3) {
                    time++;
                    x /= f1;
                }
                int f2 = factor[x];
                int time2 = 0;
                if (f2 != -1) {
                    while (x % f2 == 0 && time2 < 3) {
                        time2++;
                        x /= f2;
                    }
                    if (x == 1 && time + time2 <= 3) {
                        ok = false;
                    }
                }

            }
            if (ok) {
                ans++;
            }
        }
        return ans;
    }
}
