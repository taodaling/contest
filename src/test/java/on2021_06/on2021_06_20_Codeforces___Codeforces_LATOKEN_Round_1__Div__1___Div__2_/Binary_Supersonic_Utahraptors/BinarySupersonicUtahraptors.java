package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.Binary_Supersonic_Utahraptors;



import template.io.FastInput;
import template.io.FastOutput;

public class BinarySupersonicUtahraptors {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int R = 0;
        for(int i = 0; i < n + m; i++){
            R += in.ri();
        }
        for(int i = 0; i < k; i++){
            in.ri();
        }
        out.println(Math.abs(n - R));
    }
}
