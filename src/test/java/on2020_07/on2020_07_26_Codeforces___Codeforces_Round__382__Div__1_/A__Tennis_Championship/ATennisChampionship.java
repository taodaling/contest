package on2020_07.on2020_07_26_Codeforces___Codeforces_Round__382__Div__1_.A__Tennis_Championship;



import template.io.FastInput;
import template.io.FastOutput;

public class ATennisChampionship {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int cur = 1;
        long f0 = 1;
        long f1 = 2;
        long n = in.readLong();
        while(f1 + f0 <= n){
            cur++;
            long tmp = f1;
            f1 += f0;
            f0 = tmp;
        }
        out.println(cur);
    }
}
