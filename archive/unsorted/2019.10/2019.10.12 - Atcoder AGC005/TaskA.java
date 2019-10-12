package contest;

import java.io.PrintWriter;

import template.FastInput;

public class TaskA {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        char[] s = new char[200000];
        int n = in.readString(s, 0);
        int sCnt = 0;
        int tCnt = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == 'S') {
                sCnt++;
            } else {
                if(sCnt > 0){
                    sCnt--;
                }
                else{
                    tCnt++;
                }
            }
        }

        out.println(sCnt + tCnt);
    }
}
