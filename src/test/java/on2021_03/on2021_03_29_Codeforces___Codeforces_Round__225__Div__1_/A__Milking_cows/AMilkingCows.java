package on2021_03.on2021_03_29_Codeforces___Codeforces_Round__225__Div__1_.A__Milking_cows;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

public class AMilkingCows {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        //0 left
        //1 right
        int[] d = in.ri(n);
        int cnt = 0;
        long ans = 0;
        for(int x : d){
            if(x == 0){
                ans += cnt;
            }else{
                cnt++;
            }
        }
        out.println(ans);
    }
}
