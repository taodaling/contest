package contest;

import template.datastructure.XorDeltaDSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

import java.util.List;

public class DApplemanAndComplicatedTask {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        XorDeltaDSU dsu = new XorDeltaDSU(n + 2);
        dsu.init();
        int zero = 0;
        for (int i = 0; i < k; i++) {
            int x = in.ri() - 1;
            int y = in.ri() - 1;
            int v = in.rc() == 'o' ? 1 : 0;
            int a = Math.abs(y - x);
            int b = y + x;
            if (b >= n) {
                b = (n - 1) - (b - (n - 1));
            }
            assert a <= b;
            dsu.merge(a, b + 2, v);
        }
        int cc = 0;
        for(int i = 0; i <= n + 1; i++){
            if(dsu.find(i) == i){
                cc++;
            }
        }
        if(!dsu.valid()){
            out.println(0);
        }else{
            out.println(pow.pow(2, cc - 2));
        }
    }
}
