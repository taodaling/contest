package on2020_06.on2020_06_18_Codeforces___Codeforces_Global_Round_8.A__C__;



import template.io.FastInput;
import template.io.FastOutput;

public class AC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readInt();
        long b = in.readInt();
        long n = in.readInt() + 1;
        if (a >= n || b >= n) {
            out.println(0);
            return;
        }
        int round = 0;
        while (a + b < n) {
            round++;
            if(a < b){
                a += b;
            }else{
                b += a;
            }
        }
        out.println(round + 1);
    }
}
