package contest;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();

        NumberTheory.Gcd gcd = new NumberTheory.Gcd();
        if(gcd.gcd(a, b) == 1){
            out.println("Finite");
        }else{
            out.println("Infinite");
        }
    }
}
