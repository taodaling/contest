package on2021_11.on2021_11_02_Codeforces___Codeforces_Round__752__Div__1_.A__Di_visible_Confusion;



import template.io.FastInput;
import template.io.FastOutput;

public class ADiVisibleConfusion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        for(int i = 0; i < n; i++){
            boolean find = false;
            for(int j = 2; j <= i + 2; j++){
                assert j <= 30;
                if(a[i] % j != 0){
                    find = true;
                    break;
                }
            }
            if(!find){
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}
