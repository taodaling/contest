package on2019_10.on2019_10_23_codefestival_2016_final.TaskE;



import template.FastInput;
import template.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long a = in.readLong();

        if (n == 1) {
            out.println(1);
            return;
        }

        long min = n;
        for (int k = 0; k <= 50; k++) {
            double limit = Math.pow(n, 1.0 / (k + 1));
            long s = (long) limit - 1;
            while (Math.pow(s, k + 1) < n) {
                s++;
            }
            long sum = a * k + (s - 1) * (k + 1);
            long p = 1;
            for (int i = 0; i <= k; i++) {
                p *= (s - 1);
            }

            while (p < n) {
                p /= (s - 1);
                p *= s;
                sum++;
            }

            min = Math.min(sum, min);
        }

        out.println(min);
    }

}
