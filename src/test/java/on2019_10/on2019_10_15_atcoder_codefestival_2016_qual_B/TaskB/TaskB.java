package on2019_10.on2019_10_15_atcoder_codefestival_2016_qual_B.TaskB;



import java.io.PrintWriter;

import template.FastInput;

public class TaskB {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);


        int aCnt = 0;
        int bCnt = 0;
        int pass = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == 'a') {
                if (pass < a + b) {
                    yes(out);
                    pass++;
                } else {
                    no(out);
                }
                aCnt++;
            } else if (s[i] == 'b') {
                if (pass < a + b && bCnt < b) {
                    yes(out);
                    pass++;
                } else {
                    no(out);
                }
                bCnt++;
            } else {
                no(out);
            }
        }
    }

    public void yes(PrintWriter out) {
        out.println("Yes");
    }

    public void no(PrintWriter out) {
        out.println("No");
    }
}
