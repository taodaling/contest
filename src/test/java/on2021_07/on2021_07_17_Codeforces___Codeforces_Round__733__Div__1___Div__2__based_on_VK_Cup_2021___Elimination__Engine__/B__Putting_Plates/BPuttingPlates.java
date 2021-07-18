package on2021_07.on2021_07_17_Codeforces___Codeforces_Round__733__Div__1___Div__2__based_on_VK_Cup_2021___Elimination__Engine__.B__Putting_Plates;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class BPuttingPlates {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        int w = in.ri();
        int[] array = new int[(h + w - 2) * 2];
        array[0] = array[w - 1] = array[w - 1 + h - 1] = array[w - 1 + h - 1 + w - 1] = 1;
        for (int i = 0; i < array.length; i++) {
            int prev = DigitUtils.mod(i - 1, array.length);
            int next = DigitUtils.mod(i + 1, array.length);

            if (array[prev] + array[next] == 0) {
                array[i] = 1;
            }
        }
        int[][] ans = new int[h][w];
        int rpos = 0;
        for (int i = 0; i < w - 1; i++) {
            ans[0][i] = array[rpos++];
        }

        for (int i = 0; i < h - 1; i++) {
            ans[i][w - 1] = array[rpos++];
        }

        for (int i = w - 1; i > 0; i--) {
            ans[h - 1][i] = array[rpos++];
        }

        for (int i = h - 1; i > 0; i--) {
            ans[i][0] = array[rpos++];
        }

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                out.append(ans[i][j]);
            }
            out.println();
        }
        out.println();
    }
}
