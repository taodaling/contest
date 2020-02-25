package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class AFastFoodRestaurant {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] cnt = new int[3];
        for (int i = 0; i < 3; i++) {
            cnt[i] = in.readInt();
        }
        Arrays.sort(cnt);

        int ans = 0;
        if (cnt[2] > 0) {
            cnt[2]--;
            ans++;
        }
        if (cnt[1] > 0) {
            cnt[1]--;
            ans++;
        }
        if (cnt[0] > 0) {
            cnt[0]--;
            ans++;
        }
        if(cnt[2] > 0 && cnt[1] > 0){
            cnt[2]--;
            cnt[1]--;
            ans++;
        }
        if(cnt[2] > 0 && cnt[0] > 0){
            cnt[2]--;
            cnt[0]--;
            ans++;
        }
        if(cnt[1] > 0 && cnt[0] > 0){
            cnt[1]--;
            cnt[0]--;
            ans++;
        }

        if(cnt[1] > 0 && cnt[0] > 0 && cnt[2] > 0){
            cnt[1]--;
            cnt[0]--;
            cnt[2]--;
            ans++;
        }
        out.println(ans);
    }
}
