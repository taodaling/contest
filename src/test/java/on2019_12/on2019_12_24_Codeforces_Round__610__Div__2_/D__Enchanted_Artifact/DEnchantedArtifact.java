package on2019_12.on2019_12_24_Codeforces_Round__610__Div__2_.D__Enchanted_Artifact;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class DEnchantedArtifact {
    FastOutput out;
    FastInput in;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.out = out;
        this.in = in;
        char[] allA = new char[300];
        char[] allB = new char[300];
        for (int i = 0; i < 300; i++) {
            allA[i] = 'a';
            allB[i] = 'b';
        }
        int aCnt = 300 - cast(allA);
        if (aCnt == 300) {
            return;
        }
        int bCnt = 300 - cast(allB);
        if (bCnt == 300) {
            return;
        }
        int n = aCnt + bCnt;
        char[] data = new char[n];
        Arrays.fill(data, 'a');
        int status = n - aCnt;
        for (int i = 0; i < n - 1; i++) {
            data[i] = 'b';
            int newStatus = cast(data);
            if (newStatus > status) {
                data[i] = 'a';
                continue;
            }
            status = newStatus;
        }

        if (status == 0) {
            cast(data);
            return;
        } else {
            data[n - 1] = 'b';
            cast(data);
        }
    }

    public int cast(char[] s) {
        for (char c : s) {
            out.append(c);
        }
        out.println();
        out.flush();
        return in.readInt();
    }
}
