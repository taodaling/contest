package on2021_11.on2021_11_09_AtCoder___AtCoder_Beginner_Contest_226.A___Round_decimals;



import template.io.FastInput;
import template.io.FastOutput;

public class ARoundDecimals {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        double x = in.rd();
        long h = (long) Math.ceil(x);
        long f = (long) Math.floor(x);
        if(h - x <= x - f){
            out.println(h);
        }else{
            out.println(f);
        }
    }
}
