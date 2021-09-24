package on2021_07.on2021_07_22_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.Task;



import template.io.FastInput;
import template.io.FastOutput;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long sum = 0;
        int L = (int) 3e5;
        for (int i = (int)1e5; i <= L; i++) {
            for (int j = 1, r; j <= i; j = r + 1) {
                r = i / (i / j);
                sum++;
            }
        }
        out.println(sum);
    }
}
