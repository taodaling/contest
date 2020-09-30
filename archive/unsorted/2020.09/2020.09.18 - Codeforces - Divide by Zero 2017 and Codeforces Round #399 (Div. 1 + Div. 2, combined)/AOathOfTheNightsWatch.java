package contest;

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
