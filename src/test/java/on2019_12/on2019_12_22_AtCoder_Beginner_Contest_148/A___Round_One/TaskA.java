package on2019_12.on2019_12_22_AtCoder_Beginner_Contest_148.A___Round_One;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] cnt = new int[4];
        cnt[in.readInt()]++;
        cnt[in.readInt()]++;
        for (int i = 1; i <= 3; i++) {
            if (cnt[i] == 0) {
                out.println(i);
            }
        }
    }
}
