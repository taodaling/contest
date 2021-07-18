package on2021_07.on2021_07_13_Single_Round_Match_809.RectangleBreaking;



public class RectangleBreaking {
    int solve(int x, int y) {
        int t = 1;
        while (t * 2 <= y)
            t <<= 1;
        return x / t;
    }

    public int[] solve(int[] posR, int[] posC) {
        int sum = 0;
        for (int i = 0; i < posR.length; i++) {
            int x = posR[i];
            int y = posC[i];
            if (x > y) {
                sum += solve(x, y) - 1;
            } else {
                sum -= solve(y, x) - 1;
            }
        }

        int[] ans = new int[8];
        ans[0] = -1;
        ans[1] = sum + 1 > 0 ? -1 : -sum;
        ans[2] = sum + 1 > 0 ? 0 : -sum + 1;
        ans[3] = sum + 1 >= 0 ? -1 : 0;

        ans[0 + 4] = -1;
        ans[1 + 4] = sum - 1 < 0 ? -1 : sum;
        ans[2 + 4] = sum - 1 <= 0 ? -1 : 0;
        ans[3 + 4] = sum - 1 < 0 ? 0 : sum + 1;

        for(int i = 0; i < 8; i++){
            ans[i]++;
            if(ans[i] != 0){
                ans[i] = Math.max(ans[i], 2);
            }
        }

        return ans;
    }
}
