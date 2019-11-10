package on2019_11.on2019_11_09_NIKKEI_Programming_Contest_2019_2.E___Non_triangular_Triplets;



import template.FastInput;
import template.FastOutput;
import template.Randomized;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TaskE {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        long sumL = range(0, 2 * n - 1);
        long sumR = range(2 * n - k, 3 * n - 1 - k);

        if (sumL > sumR) {
            out.println(-1);
            return;
        }

        TreeSet<Integer> l1 = new TreeSet<>();
        TreeSet<Integer> l2 = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            l1.add(i);
            l2.add(i);
        }

        for (int i = n - 1; i >= 0; i--) {
            int container = n + i - k;
            if (l1.last() <= l2.last()) {
                Integer x = l2.pollLast();
                Integer y = l1.floor(container - x);
                l1.remove(y);
                answer(out, y + k, x + n + k, container + n + k + k);
            }else{
                Integer x = l1.pollLast();
                Integer y = l2.floor(container - x);
                l2.remove(y);
                answer(out, x + k, y + n + k, container + n + k + k);
            }
        }
    }

    public void answer(FastOutput out, int a, int b, int c){
        out.append(a).append(' ').append(b).append(' ').append(c).append('\n');
    }

    /**
     * l + (l + 1) + ... + r
     */
    public long range(long l, long r) {
        return (r + l) * (r - l + 1) / 2;
    }

}
