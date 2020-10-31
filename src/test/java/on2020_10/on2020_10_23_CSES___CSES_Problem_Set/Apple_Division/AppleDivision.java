package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Apple_Division;



import template.binary.Bits;
import template.io.FastInput;
import java.io.PrintWriter;

public class AppleDivision {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] w = new int[n];
        in.populate(w);
        long ans = (int)1e9;
        for(int i = 0; i < 1 << n; i++){
            long d = 0;
            for(int j = 0; j < n; j++){
                if(Bits.get(i, j) == 1){
                    d += w[j];
                }else{
                    d -= w[j];
                }
            }
            ans = Math.min(ans, Math.abs(d));
        }
        out.println(ans);
    }
}
