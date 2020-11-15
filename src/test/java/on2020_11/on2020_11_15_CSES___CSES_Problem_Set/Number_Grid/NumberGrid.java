package on2020_11.on2020_11_15_CSES___CSES_Problem_Set.Number_Grid;



import template.io.FastInput;
import template.io.FastOutput;

public class NumberGrid {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt() - 1;
        int y = in.readInt() - 1;
        out.println(x ^ y);
    }
}
