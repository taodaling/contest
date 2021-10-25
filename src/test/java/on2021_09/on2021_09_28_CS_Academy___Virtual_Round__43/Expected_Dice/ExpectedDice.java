package on2021_09.on2021_09_28_CS_Academy___Virtual_Round__43.Expected_Dice;



import template.io.FastInput;
import template.io.FastOutput;

public class ExpectedDice {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] sum = new int[100 + 1];
        int[] a = in.ri(6);
        int[] b = in.ri(6);
        for(int x : a){
            for(int y : b){
                sum[x + y]++;
            }
        }
        int ans = 0;
        for(int i = 0; i <= 100; i++){
            if(sum[i] > sum[ans]){
                ans = i;
            }
        }
        out.println(ans);
    }
}
