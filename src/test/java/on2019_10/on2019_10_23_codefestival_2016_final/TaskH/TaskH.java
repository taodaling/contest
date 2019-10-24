package on2019_10.on2019_10_23_codefestival_2016_final.TaskH;



import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

public class TaskH {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n + 1];

        for (int i = 1; i < n; i++) {
            a[i] = in.readInt();
        }

        int preSum = 0;
        for (int i = 3; i < n; i++) {
            preSum += a[i];
        }

        int limit = 1_000_001;
        RotateArray ra = new RotateArray(limit);
        int[] buf = new int[limit];
        for (int j = 0; j < limit; j++) {
            ra.set(j, j);
        }
        for (int i = 3; i < n; i++) {
            for (int j = 1; j <= a[i]; j++) {
                buf[j] = ra.get(j);
            }
            for (int j = 1; j <= a[i]; j++) {
                ra.set(-j, buf[j]);
            }
            ra.rotate(-a[i]);
        }

        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int x = in.readInt();
            int ans;
            if (x >= limit) {
                ans = x - preSum;
            } else {
                ans = ra.get(x);
            }
            out.println(ans + a[1] - a[2]);
        }
    }
}


class RotateArray {
    private int offset;
    private int[] data;
    private int n;

    public RotateArray(int cap) {
        data = new int[cap];
        n = cap;
    }

    public int get(int i) {
        return data[DigitUtils.mod(i + offset, n)];
    }

    public void set(int i, int v) {
        data[DigitUtils.mod(i + offset, n)] = v;
    }

    public void rotate(int x) {
        offset += x;
    }

    public int cap() {
        return n;
    }

}
