package on2021_04.on2021_04_23_Codeforces___Contest_2050_and_Codeforces_Round__718__Div__1___Div__2_.A__Sum_of_2050;



import template.io.FastInput;
import template.io.FastOutput;

public class ASumOf2050 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        if(n % 2050 != 0){
            out.println(-1);
            return;
        }
        long v = n / 2050;
        long ans = 0;
        while(v > 0){
            ans += v % 10;
            v /= 10;
        }
        out.println(ans);
    }
}
