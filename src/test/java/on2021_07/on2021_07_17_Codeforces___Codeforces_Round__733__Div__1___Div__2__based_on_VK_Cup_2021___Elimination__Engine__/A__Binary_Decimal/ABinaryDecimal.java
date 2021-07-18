package on2021_07.on2021_07_17_Codeforces___Codeforces_Round__733__Div__1___Div__2__based_on_VK_Cup_2021___Elimination__Engine__.A__Binary_Decimal;



import template.io.FastInput;
import template.io.FastOutput;

public class ABinaryDecimal {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int max = 0;
        while(n > 0){
            max = Math.max(max, n % 10);
            n /= 10;
        }
        out.println(max);
    }
}
