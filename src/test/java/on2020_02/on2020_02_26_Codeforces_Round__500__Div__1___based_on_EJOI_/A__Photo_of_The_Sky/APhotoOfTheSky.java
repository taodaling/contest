package on2020_02.on2020_02_26_Codeforces_Round__500__Div__1___based_on_EJOI_.A__Photo_of_The_Sky;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class APhotoOfTheSky {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] nums = new long[n * 2];
        for(int i = 0; i < n * 2; i++){
            nums[i] = in.readInt();
        }
        Randomized.shuffle(nums);
        Arrays.sort(nums);
        long ans = (nums[n - 1] - nums[0]) * (nums[2 * n - 1] - nums[n]);
        for(int i = 0; i + n <= 2 * n; i++){
            ans = Math.min(ans, (nums[2 * n - 1] - nums[0]) * (nums[i + n - 1] - nums[i]));
        }
        out.println(ans);
    }
}
