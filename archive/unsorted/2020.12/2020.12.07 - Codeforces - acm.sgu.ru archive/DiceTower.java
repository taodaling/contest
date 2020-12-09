package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DiceTower {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if(n < 21){
            out.println(-1);
            return;
        }
        if(n == 21){
            out.println(1);
            return;
        }
        if(n < 30){
            out.println(-1);
            return;
        }
        if(n % 14 >= 2 && n % 14 <= 12){
            out.println(n / 14);
        }else{
            out.println(-1);
        }
    }
}
