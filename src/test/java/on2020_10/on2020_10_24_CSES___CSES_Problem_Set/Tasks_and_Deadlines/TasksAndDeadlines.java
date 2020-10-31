package on2020_10.on2020_10_24_CSES___CSES_Problem_Set.Tasks_and_Deadlines;



import template.io.FastInput;
import template.rand.Randomized;

import java.io.PrintWriter;
import java.util.Arrays;

public class TasksAndDeadlines {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] d = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            d[i] = in.readInt();
        }
        long sum = Arrays.stream(d).mapToLong(Long::valueOf)
                .sum();
        Randomized.shuffle(a);
        Arrays.sort(a);
        long ps = 0;
        for (int i = 0; i < n; i++) {
            ps += a[i];
            sum -= ps;
        }
        out.println(sum);
    }
}
