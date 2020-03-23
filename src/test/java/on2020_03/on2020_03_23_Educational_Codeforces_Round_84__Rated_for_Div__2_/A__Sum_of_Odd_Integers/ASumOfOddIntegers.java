package on2020_03.on2020_03_23_Educational_Codeforces_Round_84__Rated_for_Div__2_.A__Sum_of_Odd_Integers;



import template.io.FastInput;
import template.io.FastOutput;

public class ASumOfOddIntegers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readInt();
        long sum = (k - 1) * k + k;
        if (n - sum >= 0 && (n - sum) % 2 == 0) {
            out.println("YES");
        } else {
            out.println("NO");
        }
    }
}
