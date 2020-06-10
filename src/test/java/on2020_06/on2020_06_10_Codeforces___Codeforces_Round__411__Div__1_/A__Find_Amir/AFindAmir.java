package on2020_06.on2020_06_10_Codeforces___Codeforces_Round__411__Div__1_.A__Find_Amir;



import template.io.FastInput;
import template.io.FastOutput;

public class AFindAmir {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if(n <= 2){
            out.println(0);
            return;
        }
        int ans = n / 2 - 1 + n % 2;
        out.println(ans);
    }
}
