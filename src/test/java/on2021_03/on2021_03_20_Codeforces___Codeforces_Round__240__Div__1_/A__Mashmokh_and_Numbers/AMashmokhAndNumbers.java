package on2021_03.on2021_03_20_Codeforces___Codeforces_Round__240__Div__1_.A__Mashmokh_and_Numbers;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;

public class AMashmokhAndNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int round = n / 2;
        k -= round;
        if (k < 0) {
            out.println("-1");
            return;
        }
        if(k > 0 && round == 0){
            out.println(-1);
            return;
        }
        int start = (int) 5e8;
        for (int i = 0; i < n / 2; i++) {
            if (k > 0) {
                out.append((k + 1)).append(' ').append((k + 1) * 2).append(' ');
                k = 0;
            } else {
                out.append(start).append(' ').append(start + 1).append(' ');
                start += 2;
            }
        }
        if (n % 2 == 1) {
            out.println(start);
        }
    }
}
