package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class CABBB {
    char[] s = new char[(int) 2e5];

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readString(s, 0);
        int remove = 0;
        int bCnt = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (s[i] == 'A') {
                if (bCnt > 0) {
                    bCnt--;
                    remove++;
                }
                continue;
            }
            bCnt++;
        }
        remove += bCnt / 2;
        out.println(n - remove * 2);
    }
}
