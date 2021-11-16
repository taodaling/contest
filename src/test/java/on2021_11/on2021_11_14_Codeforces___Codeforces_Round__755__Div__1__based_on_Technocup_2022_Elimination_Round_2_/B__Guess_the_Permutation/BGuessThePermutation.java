package on2021_11.on2021_11_14_Codeforces___Codeforces_Round__755__Div__1__based_on_Technocup_2022_Elimination_Round_2_.B__Guess_the_Permutation;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.function.IntPredicate;

public class BGuessThePermutation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.ri();
        long total = query(0, n - 1);
        IntPredicate pred = m -> {
            return query(0, m) >= total;
        };
        int last = BinarySearch.firstTrue(pred, 0, n - 1);
        long prev = query(0, last - 1);
        int step = (int) (total - prev);
        int mid = last - step;

        long sum = query(0, mid - 1);
        long sum2 = query(0, mid - 2);
        step = (int)(sum - sum2);
        int first = mid - 1 - step;

        out.append("! ").append(first + 1).append(' ').append(mid + 1).append(' ').append(last + 1).println().flush();
    }

    FastInput in;
    FastOutput out;

    public long query(int l, int r) {
        out.append("? ").append(l + 1).append(' ').append(r + 1).println().flush();
        long ans = in.rl();
        if(ans == -1){
            throw new RuntimeException();
        }
        return ans;
    }
}
