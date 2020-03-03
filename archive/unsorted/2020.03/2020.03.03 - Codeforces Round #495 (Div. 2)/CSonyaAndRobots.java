package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

import java.util.Arrays;

public class CSonyaAndRobots {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        boolean[] first = new boolean[(int) 1e5 + 1];
        int[] left = new int[n];
        for (int i = 0; i < n; i++) {
            if (first[a[i]]) {
                continue;
            }
            first[a[i]] = true;
            left[i] = 1;
        }
        Arrays.fill(first, false);
        int[] right = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            if (first[a[i]]) {
                continue;
            }
            first[a[i]] = true;
            right[i] = 1;
        }
        IntegerPreSum ps = new IntegerPreSum(right);
        long cnt = 0;
        for (int i = 0; i < n; i++) {
            cnt += left[i] * ps.post(i + 1);
        }
        out.println(cnt);
    }
}
