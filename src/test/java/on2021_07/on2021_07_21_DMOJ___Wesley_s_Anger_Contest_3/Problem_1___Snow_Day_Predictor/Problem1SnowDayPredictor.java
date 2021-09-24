package on2021_07.on2021_07_21_DMOJ___Wesley_s_Anger_Contest_3.Problem_1___Snow_Day_Predictor;



import template.io.FastInput;
import template.io.FastOutput;

public class Problem1SnowDayPredictor {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int cond = 0;

        int t = in.ri();
        int d = in.ri();
        int b = in.ri();
        if(t < -40){
            cond++;
        }
        if(d >= 15){
            cond++;
        }
        if(b > 50){
            cond++;
        }
        out.println(cond >= 2 ? "YES" : "NO");
    }
}
