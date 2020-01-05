package on2020_01.on2020_01_04_Educational_Codeforces_Round_78__Rated_for_Div__2_.LUOGU4723;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LinearRecurrenceSolver;
import template.math.Modular;
import template.primitve.generated.IntegerList;

public class LUOGU4723 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Modular mod = new Modular(998244353);
        IntegerList coes = new IntegerList(k);
        for (int i = 0; i < k; i++) {
            coes.add(in.readInt());
        }
        LinearRecurrenceSolver solver = LinearRecurrenceSolver.newSolverFromLinearRecurrence(coes, mod);
        IntegerList a = new IntegerList(k);
        for(int i = 0; i < k; i++){
            a.add(in.readInt());
        }
        int ans = solver.solve(n, a);
        out.println(ans);
    }
}
