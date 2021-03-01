package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ABearAndPoker {
    int trim(int x){
        while(x % 2 == 0){
            x /= 2;
        }
        while(x % 3 == 0){
            x /= 3;
        }
        return x;
    }
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int first = trim(in.ri());
        for(int i = 1; i < n; i++) {
            if(trim(in.ri()) != first){
                out.println("No");
                return;
            }
        }
        out.println("Yes");
    }
}
