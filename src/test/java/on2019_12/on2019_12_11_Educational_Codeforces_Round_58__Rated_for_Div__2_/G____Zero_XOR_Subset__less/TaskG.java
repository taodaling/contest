package on2019_12.on2019_12_11_Educational_Codeforces_Round_58__Rated_for_Div__2_.G____Zero_XOR_Subset__less;



import template.algo.PreXor;
import template.datastructure.LinearBasis;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        PreXor px = new PreXor(a);
        if(px.prefix(n - 1) == 0){
            out.println(-1);
            return;
        }
        LinearBasis lb = new LinearBasis();
        for (int i = 0; i < n; i++) {
            lb.add(px.prefix(i));
        }
        out.println(lb.size());
    }
}
