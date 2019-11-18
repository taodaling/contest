package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TaskA2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long[] req = new long[n];
        int[][] task = new int[m][2];
        for (int i = 0; i < m; i++) {
            int from = in.readInt();
            int to = in.readInt();
            task[i][0] = from;
            task[i][1] = DigitUtils.mod(to - from, n);
        }
        Map<Integer, List<int[]>> taskGroupByFrom = Arrays.stream(task).collect(Collectors.groupingBy(x -> x[0]));
        for (int i = 0; i < n; i++) {
            List<int[]> tasks = taskGroupByFrom.get(i + 1);
            if (tasks == null) {
                continue;
            }
            req[i] = (long) 1e18;
            for (int[] t : tasks) {
                req[i] = Math.min(req[i], t[1]);
            }
            req[i] += (long) n * (tasks.size() - 1);
        }

        for (int i = 0; i < n; i++) {
            long cost = 0;
            for (int j = 0; j < n; j++) {
                if (req[j] == 0) {
                    continue;
                }
                cost = Math.max(cost, req[j] + DigitUtils.mod(j - i, n));
            }
            out.println(cost);
        }
    }
}