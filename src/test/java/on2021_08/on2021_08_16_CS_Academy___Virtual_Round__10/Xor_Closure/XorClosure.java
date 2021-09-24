package on2021_08.on2021_08_16_CS_Academy___Virtual_Round__10.Xor_Closure;



import template.datastructure.LinearBasis;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashSet;

public class XorClosure {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        LinearBasis lb = new LinearBasis();
        LongHashSet set = new LongHashSet(n, false);
        for(int i = 0; i < n; i++){
            long x = in.rl();
            lb.add(x);
            set.add(x);
        }
        long ans = lb.xorNumberCount() - set.size();
        out.println(ans);
    }
}
