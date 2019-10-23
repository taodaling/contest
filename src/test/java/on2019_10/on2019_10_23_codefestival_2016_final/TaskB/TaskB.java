package on2019_10.on2019_10_23_codefestival_2016_final.TaskB;



import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int sum = 0;
        for (int i = 1;; i++) {
            sum += i;
            if (sum < n) {
                continue;
            }
            int delete = sum - n;
            for (int j = 1; j <= i; j++) {
                if (j == delete) {
                    continue;
                }
                out.println(j);
            }
            return;
        }
    }
}
