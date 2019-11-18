package on2019_11.on2019_11_18_AtCoder_Grand_Contest_010.D___Decrementing;



import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskD {
    int n;
    int[] preGcd;
    int[] sufGcd;
    NumberTheory.Gcd gcd = new NumberTheory.Gcd();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int[] a = new int[n];
        preGcd = new int[n];
        sufGcd = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        if (n == 1) {
            out.println("Second");
            return;
        }

        boolean success = win(a);

        if (success) {
            out.println("First");
        } else {
            out.println("Second");
        }
    }

    public boolean win(int[] data) {
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                preGcd[i] = data[0];
            } else {
                preGcd[i] = gcd.gcd(preGcd[i - 1], data[i]);
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (i == n - 1) {
                sufGcd[i] = data[n - 1];
            } else {
                sufGcd[i] = gcd.gcd(sufGcd[i + 1], data[i]);
            }
        }
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += data[i];
        }

        if ((sum - n) % 2 == 1) {
            return true;
        }

        for (int i = 0; i < n; i++) {
            if (data[i] == 1) {
                continue;
            }
            int p = i == 0 ? 0 : preGcd[i - 1];
            int s = i == n - 1 ? 0 : sufGcd[i + 1];
            int g = gcd.gcd(data[i] - 1, gcd.gcd(p, s));
            if (g > 1) {
                data[i]--;
                for (int j = 0; j < n; j++) {
                    data[j] /= g;
                }
                return !win(data);
            }
        }

        return false;
    }


}
