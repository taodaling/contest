package on2021_03.on2021_03_17_DMOJ.DMOPC__20_Contest_4_P6___Petty_Children;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Mod2GuassianElimination;

public class DMOPC20Contest4P6PettyChildren {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();

        Mod2GuassianElimination ge = new Mod2GuassianElimination(n, n);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            ge.modifyLeft(a, a, 1);
            ge.modifyLeft(a, b, 1);
            ge.modifyRight(a, 1);
            ge.modifyLeft(b, a, 1);
            ge.modifyLeft(b, b, 1);
            ge.modifyRight(b, 1);
        }
        boolean ans = ge.solve();
        assert ans;
        BitSet sol = ge.getSolutions();
        out.println(n);
        for(int i = 0; i < n; i++){
            out.append(sol.get(i) ? 2 : 1);
            if(i + 1 < n){
                out.append(' ');
            }
        }
        out.println();
    }
}
