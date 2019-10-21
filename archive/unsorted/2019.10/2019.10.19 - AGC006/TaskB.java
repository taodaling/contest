package contest;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;

import java.util.TreeSet;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        if (x == 1 || x == 2 * n - 1) {
            out.println("No");
            return;
        }
        out.println("Yes");
        if (n == 2) {
            out.println("1 2 3");
            return;
        }

        int[] data = new int[2 * n];
        TreeSet<Integer> remain = new TreeSet<Integer>();
        for (int i = 1; i < 2 * n; i++) {
            remain.add(i);
        }
        if (x == 2) {
            data[n] = x;
            remain.remove(x);
            data[n - 1] = x + 1;
            remain.remove(x + 1);
            data[n + 1] = x - 1;
            remain.remove(x - 1);
            data[n + 2] = x + 2;
            remain.remove(x + 2);
        } else {
            data[n] = x;
            remain.remove(x);
            data[n - 1] = x - 1;
            remain.remove(x - 1);
            data[n + 1] = x + 1;
            remain.remove(x + 1);
            data[n + 2] = x - 2;
            remain.remove(x - 2);
        }

        for (int i = 1; i < 2 * n; i++) {
            if (data[i] == 0) {
                data[i] = remain.pollFirst();
            }
        }

        for(int i = 1; i < 2 * n; i++){
            out.println(data[i]);
        }
    }

}
