package on2021_09.on2021_09_03_CS_Academy___Virtual_Round__24.Vector_Size;



import template.io.FastInput;
import template.io.FastOutput;

public class VectorSize {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int sum = 0;
        int best = 0;
        for(int i = 0; i < n; i++){
            sum += in.ri() * 2 - 1;
            sum = Math.max(sum, 0);
            best = Math.max(best, sum);
        }
        out.println(best);
    }
}
