package on2021_10.on2021_10_09_AtCoder___AtCoder_Beginner_Contest_221.SmallWeightKnapsackProblem;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.KnapsackProblemWithBoundedWeights;

public class SmallWeightKnapsackProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int C = in.ri();
        int[] w = in.ri(n);
        int ans = KnapsackProblemWithBoundedWeights.solve(w, C);
        boolean[] sol = KnapsackProblemWithBoundedWeights.solution(w, C);
        int sum = 0;
        for(int i = 0; i < n; i++){
            if(sol[i]){
                sum += w[i];
            }
        }

        out.println(ans);
    }
}
