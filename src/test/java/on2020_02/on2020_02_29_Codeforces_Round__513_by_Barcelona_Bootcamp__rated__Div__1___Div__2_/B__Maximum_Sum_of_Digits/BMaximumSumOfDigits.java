package on2020_02.on2020_02_29_Codeforces_Round__513_by_Barcelona_Bootcamp__rated__Div__1___Div__2_.B__Maximum_Sum_of_Digits;



import template.io.FastInput;
import template.io.FastOutput;

public class BMaximumSumOfDigits {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long ans = solve(n);
        out.println(ans);
    }

    public long solve(long n) {
        if(n < 10){
            return n;
        }
        if(n % 10 == 9){
            return 9 + solve(n / 10);
        }
        return 10 + n % 10 + solve(n / 10 - 1);
    }
}
