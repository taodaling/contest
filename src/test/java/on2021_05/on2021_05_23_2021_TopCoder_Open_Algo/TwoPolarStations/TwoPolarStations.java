package on2021_05.on2021_05_23_2021_TopCoder_Open_Algo.TwoPolarStations;



import template.math.ModLinearRecurrenceSolver;
import template.polynomial.IntPolyFFT;
import template.primitve.generated.datastructure.IntegerArrayList;

public class TwoPolarStations {
    int mod = (int) 1e9 + 7;

    public int count(int N, int lo, int hi) {
        int take = hi - lo + 1;
        if(take == N){
            long w1 = solve(take);
            //long w2 = solve(N - take);
            return (int) (w1 % mod);
        }
        return 0;
    }

    public int solve(int n) {
        int[] dp = new int[1000];
        dp[0] = 1;
        for(int i = 1; i < 1000; i++){
            long sum = 0;
            for(int j = 0; j < i; j++){
                sum += (long)dp[j] * (i - j) % mod;
            }
            dp[i] = (int) (sum % mod);
        }
        IntegerArrayList first = new IntegerArrayList(dp);
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver.newSolverByAutoDetectLinearRecurrence(first, mod, new IntPolyFFT(mod));
        return solver.solve(n, first);
    }
}
