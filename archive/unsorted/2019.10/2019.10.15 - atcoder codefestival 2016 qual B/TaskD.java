package contest;

import java.io.PrintWriter;

import template.FastInput;

public class TaskD {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] seq = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            seq[i] = in.readInt();
        }

        long totalSell = 0;
        int atLeast = 1;
        for (int i = 1; i <= n; i++) {
            if(seq[i] == atLeast){
                atLeast++;
                continue;
            }
            int sell = (seq[i] - 1) / atLeast;
            totalSell += sell;
            if(i == 1){
                atLeast = 2;
            }
        }

        out.println(totalSell);
    }
}
