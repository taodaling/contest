package on2020_07.on2020_07_25_hackercup_2020_qualification_round.Alchemy;



import framework.io.FileUtils;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.File;

public class Alchemy {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);
        int[] cnts = new int[2];
        for (int i = 0; i < n; i++) {
            if (s[i] == 'A') {
                cnts[0]++;
            } else {
                cnts[1]++;
            }
        }

        out.println(Math.abs(cnts[0] - cnts[1]) != 1 ? "N" : "Y");
    }
}
