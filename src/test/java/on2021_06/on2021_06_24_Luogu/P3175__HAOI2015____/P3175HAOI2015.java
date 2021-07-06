package on2021_06.on2021_06_24_Luogu.P3175__HAOI2015____;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastWalshHadamardTransform;

public class P3175HAOI2015 {
    double prec = 1e-8;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        double[] p = in.rd(1 << n);
        int or = 0;
        for(int i = 0; i < 1 << n; i++){
            if(p[i] > prec){
                or |= i;
            }
        }
        if(or != (1 << n) - 1){
            out.println("INF");
            return;
        }
        FastWalshHadamardTransform.orFWT(p, 0, p.length - 1);
        double[] q = new double[1 << n];
        for (int i = 0; i < 1 << n; i++) {
            if (p[i] >= 1 - prec) {
                q[i] = 0;
            } else {
                q[i] = 1 / (p[i] - 1);
            }
        }
        FastWalshHadamardTransform.orIFWT(q, 0, q.length - 1);
        out.println(q[(1 << n) - 1]);
    }
}
