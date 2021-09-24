package on2021_07.on2021_07_22_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.A__Digits_Sum;



import template.io.FastInput;
import template.io.FastOutput;

public class ADigitsSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        //9 + k * 10 <= n
        //k <= (n - 9) / 10;
        //k start from 0
        if (n < 9) {
            out.println(0);
        } else {
            out.println((n - 9) / 10 + 1);
        }
    }
}
