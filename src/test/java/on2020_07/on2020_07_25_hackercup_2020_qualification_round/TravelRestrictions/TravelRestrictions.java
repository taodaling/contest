package on2020_07.on2020_07_25_hackercup_2020_qualification_round.TravelRestrictions;



import framework.io.FileUtils;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.File;

public class TravelRestrictions {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d:", testNumber).println();
        int n = in.readInt();
        boolean[] enter = new boolean[n];
        boolean[] leave = new boolean[n];
        for (int i = 0; i < n; i++) {
            enter[i] = in.readChar() == 'Y';
        }
        for (int i = 0; i < n; i++) {
            leave[i] = in.readChar() == 'Y';
        }
        boolean[][] g = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            g[i][i] = true;
        }
        for (int i = 0; i + 1 < n; i++) {
            if (enter[i + 1] && leave[i]) {
                g[i][i + 1] = true;
            }
        }
        for (int i = 1; i < n; i++) {
            if (enter[i - 1] && leave[i]) {
                g[i][i - 1] = true;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    g[j][k] = g[j][k] || g[j][i] && g[i][k];
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(g[i][j] ? 'Y' : 'N');
            }
            out.println();
        }
//
//        String ans = out.toString();
//        File file = new File("D:\\download\\travel_restrictions_output.txt");
//        FileUtils.writeFile(file, ans);
    }
}
