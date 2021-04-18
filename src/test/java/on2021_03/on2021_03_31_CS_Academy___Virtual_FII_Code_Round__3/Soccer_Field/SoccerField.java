package on2021_03.on2021_03_31_CS_Academy___Virtual_FII_Code_Round__3.Soccer_Field;



import template.io.FastInput;
import template.io.FastOutput;

public class SoccerField {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] a = in.ri(2);
        int[] b = in.ri(2);
        int best = 0;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                best = Math.max(best, solve(a[0 ^ i], a[1 ^ i], b[0 ^ j], b[1 ^ j]));
            }
        }
        best = Math.max(a[0] * a[1], best);
        best = Math.max(b[0] * b[1], best);
        out.println(best);
    }

    int solve(int a, int b, int c, int d) {
        int h = Math.min(a, c);
        return h * (b + d);
    }
}
