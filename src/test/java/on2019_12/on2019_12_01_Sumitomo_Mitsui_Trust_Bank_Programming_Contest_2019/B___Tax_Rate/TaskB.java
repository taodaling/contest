package on2019_12.on2019_12_01_Sumitomo_Mitsui_Trust_Bank_Programming_Contest_2019.B___Tax_Rate;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        for (int i = 0; i <= n; i++) {
            if (n - Math.floor(i * 1.08) == 0) {
                out.println(i);
                return;
            }
        }
        out.println(":(");
    }
}
