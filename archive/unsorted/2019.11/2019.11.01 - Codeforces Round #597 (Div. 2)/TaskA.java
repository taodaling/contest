package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();

        Gcd gcd = new Gcd();
        if(gcd.gcd(a, b) == 1){
            out.println("Finite");
        }else{
            out.println("Infinite");
        }
    }
}
