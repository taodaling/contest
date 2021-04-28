package on2021_04.on2021_04_19_Codeforces___Codeforces_Round__204__Div__1_.B__Jeff_and_Furik;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DoubleLinearFunction;
import template.utils.Debug;

public class BJeffAndFurik {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] p = in.ri(n);
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (p[i] > p[j]) {
                    cnt++;
                }
            }
        }

        if (n == 1) {
            out.println(0);
            return;
        }

        if(cnt == 0){

        }
    }
}
