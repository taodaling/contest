package on2021_07.on2021_07_21_DMOJ___Wesley_s_Anger_Contest_3.Problem_2___Eat__Sleep__Code__Repeat;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class Problem2EatSleepCodeRepeat {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long hour = in.rl();
        long[] sep = new long[3];
        Arrays.fill(sep, hour / 3);
        for(int i = 0; i < hour % 3; i++){
            sep[i]++;
        }
        long ans = sep[0] * sep[1] * sep[2];
        out.println(ans);
    }
}
