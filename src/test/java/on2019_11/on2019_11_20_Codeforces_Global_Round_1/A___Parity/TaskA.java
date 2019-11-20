package on2019_11.on2019_11_20_Codeforces_Global_Round_1.A___Parity;



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
