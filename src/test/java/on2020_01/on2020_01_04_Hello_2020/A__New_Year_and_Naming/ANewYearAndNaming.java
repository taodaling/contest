package on2020_01.on2020_01_04_Hello_2020.A__New_Year_and_Naming;



import template.io.FastInput;
import template.io.FastOutput;

public class ANewYearAndNaming {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        String[] a = new String[n];
        String[] b = new String[m];
        for (int i = 0; i < n; i++) {
            a[i] = in.readString();
        }
        for (int i = 0; i < m; i++) {
            b[i] = in.readString();
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int y = in.readInt() - 1;
            out.append(a[y % n]).append(b[y % m]).println();
        }
    }
}
