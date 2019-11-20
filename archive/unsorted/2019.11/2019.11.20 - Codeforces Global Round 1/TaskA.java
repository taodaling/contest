package contest;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        NumberTheory.Modular mod = new NumberTheory.Modular(2);
        int b = in.readInt();
        int k = in.readInt();
        int val = 0;
        for(int i = 0; i < k; i++){
            val = mod.mul(val, b);
            val = mod.plus(val, in.readInt());
        }

        out.println(val == 0 ? "even" : "odd");
    }
}
