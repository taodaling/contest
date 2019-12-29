package on2019_12.on2019_12_29_Educational_Codeforces_Round_79__Rated_for_Div__2_.A__New_Year_Garland;



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
