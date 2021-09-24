package on2021_08.on2021_08_20_CS_Academy___Virtual_Beta_Round__2.Online_Gcd;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class OnlineGcd {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        int g = 0;
        for(int x : a){
            g = GCDs.gcd(g, x);
        }
        for(int i = 0; i < m; i++){
            int index = in.ri() - 1;
            int v = in.ri();
            a[index] /= v;
            g = GCDs.gcd(g, a[index]);
            out.println(g);
        }
    }
}
