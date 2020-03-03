package on2020_03.on2020_03_03_Ozon_Tech_Challenge_2020__Div_1___Div_2__Rated__T_shirts___prizes__.C__Kuroni_and_Impossible_Calculation;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class CKuroniAndImpossibleCalculation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Modular mod = new Modular(m);
        if (n > m) {
            out.println(0);
            return;
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int prod = mod.valueOf(1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                prod = mod.mul(prod, Math.abs(a[i] - a[j]));
            }
        }
        out.println(prod);
    }
}
