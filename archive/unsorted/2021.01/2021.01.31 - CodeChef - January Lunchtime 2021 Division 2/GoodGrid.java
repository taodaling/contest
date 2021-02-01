package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class GoodGrid {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        for(int i = 1; i * i <= x; i++){
            if(x % i != 0){
                continue;
            }
            if(i <= n && x / i <= n){
                out.println("Yes");
                return;
            }
        }
        out.println("No");
    }
}
