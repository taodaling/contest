package on2020_05.on2020_05_19_Dynamic_Programming_Practice_DIV_1.RandomSwaps;



import java.util.Arrays;

public class RandomSwaps {
    public int pick2(int n) {
        return n * (n - 1) / 2;
    }

    int n;

    public double[] merge(double[] a, double[] b) {
        double[] ans = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ans[(i + j) % n] += a[i] * b[j];
            }
        }
        return ans;
    }

    public double[] pow(double[] x, int exp) {
        if (exp == 0) {
            double[] ans = new double[n];
            ans[0] = 1;
            return ans;
        }
        double[] ans = pow(x, exp / 2);
        ans = merge(ans, ans);
        if (exp % 2 == 1) {
            ans = merge(ans, x);
        }
        return ans;
    }

    public double getProbability(int arrayLength, int swapCount, int a, int b) {
        this.n = arrayLength;
        double total = pick2(arrayLength);
        double stay = pick2(arrayLength - 1) / total;
        double move = (1 - stay) / (arrayLength - 1);

        double[] x = new double[n];
        Arrays.fill(x, move);
        x[0] = stay;

        double[] xt = pow(x, swapCount);
        double[] state = new double[n];
        state[a] = 1;
        double[] finalState = merge(state, xt);

        double ans = finalState[b];

        return ans;
    }
}
