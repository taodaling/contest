package on2021_07.on2021_07_18_AtCoder___AtCoder_Regular_Contest_123.A___Arithmetic_Sequence;



import template.io.FastInput;
import template.io.FastOutput;

public class AArithmeticSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        long c = in.rl();
        long x = b - a;
        long y = c - b;
        //x + 2b = y + a + c
        if(x >= y){
            out.println(x - y);
        }else{
            //x + 2k >= y
            long k = (y - x + 1) / 2;
            x += 2 * k;
            long ans = k + x - y;
            out.println(ans);
        }
    }
}
