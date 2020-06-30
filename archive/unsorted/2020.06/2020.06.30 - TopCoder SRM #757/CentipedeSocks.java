package contest;

import java.util.Arrays;

public class CentipedeSocks {
    public int fewestSocks(int C, int F, int[] sockCounts) {
        int ans = 0;
        int block = 0;
        int sum = Arrays.stream(sockCounts).sum();
        for (int i = 0; i < sockCounts.length; i++) {
            int move = Math.min(sockCounts[i], F - 1);
            sockCounts[i] -= move;
            ans += move;

            int b = sockCounts[i] / F;
            sockCounts[i] -= b * F;
            block += b;
        }

        block = Math.min(block, C - 1);
        ans += block * F;
        Arrays.sort(sockCounts);
        for (int i = sockCounts.length - 1; i >= 0 && block < C - 1; i--) {
            ans += sockCounts[i];
            block++;
        }
        ans++;
        if(ans > sum){
            return -1;
        }
        return ans;
    }
}
