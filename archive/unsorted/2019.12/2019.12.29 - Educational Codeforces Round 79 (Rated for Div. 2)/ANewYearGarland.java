package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ANewYearGarland {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] colors = new int[3];
        for(int i = 0; i < 3; i++){
            colors[i] = in.readInt();
        }
        Arrays.sort(colors);
        out.println(colors[0] + colors[1] + 1 >= colors[2] ? "Yes" : "No");
    }
}
