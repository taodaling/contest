package on2019_12.on2019_12_22_Codeforces_Round__545__Div__1_.LUOGU1792;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.PlantTreeProblem;

public class LUOGU1792 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long[] w = new long[n];
        for (int i = 0; i < n; i++) {
            w[i] = in.readInt();
        }

        if (n / 2 < m){
            out.println("Error!");
            return;
        }


        PlantTreeProblem problem = new PlantTreeProblem(w, m);
        out.println(problem.getAnswer());
    }
}
