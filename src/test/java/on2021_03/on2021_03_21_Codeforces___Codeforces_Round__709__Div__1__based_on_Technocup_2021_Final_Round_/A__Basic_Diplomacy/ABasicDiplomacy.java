package on2021_03.on2021_03_21_Codeforces___Codeforces_Round__709__Div__1__based_on_Technocup_2021_Final_Round_.A__Basic_Diplomacy;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SortUtils;

import java.util.Arrays;

public class ABasicDiplomacy {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] choice = new int[m][];
        int[] occur = new int[n];
        for (int i = 0; i < m; i++) {
            int k = in.ri();
            choice[i] = new int[k];
            for (int j = 0; j < k; j++) {
                choice[i][j] = in.ri() - 1;
            }
            if (k == 1) {
                occur[choice[i][0]]++;
            }
        }

        if(Arrays.stream(occur).max().orElse(-1) > DigitUtils.ceilDiv(m, 2)){
            out.println("NO");
            return;
        }
        int[] res = new int[m];
        for (int i = 0; i < m; i++) {
            if(choice[i].length == 1){
                res[i] = choice[i][0];
                continue;
            }
            SortUtils.quickSort(choice[i], (a, b) -> Integer.compare(occur[a], occur[b]), 0, choice[i].length);
            res[i] = choice[i][0];
            occur[res[i]]++;
        }

        out.println("YES");
        for (int x : res) {
            out.append(x + 1).append(' ');
        }
        out.println();
    }
}
