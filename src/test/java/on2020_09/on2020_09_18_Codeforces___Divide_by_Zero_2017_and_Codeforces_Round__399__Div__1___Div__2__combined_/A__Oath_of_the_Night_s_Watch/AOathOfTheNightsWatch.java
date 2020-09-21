package on2020_09.on2020_09_18_Codeforces___Divide_by_Zero_2017_and_Codeforces_Round__399__Div__1___Div__2__combined_.A__Oath_of_the_Night_s_Watch;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class AOathOfTheNightsWatch {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] data = new int[n];
        in.populate(data);
        int min = Arrays.stream(data).min().getAsInt();
        int max = Arrays.stream(data).max().getAsInt();
        int ans = 0;
        for(int x : data){
            if(min < x && x < max){
                ans++;
            }
        }
        out.println(ans);
    }
}
