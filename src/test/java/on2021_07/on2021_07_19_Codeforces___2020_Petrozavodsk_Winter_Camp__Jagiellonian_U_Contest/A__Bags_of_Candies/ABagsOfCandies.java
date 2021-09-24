package on2021_07.on2021_07_19_Codeforces___2020_Petrozavodsk_Winter_Camp__Jagiellonian_U_Contest.A__Bags_of_Candies;



import template.io.FastInput;
import template.io.FastOutput;

public class ABagsOfCandies {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        long matching = n / 2 - 1;
        out.println(n - matching);
    }
}
