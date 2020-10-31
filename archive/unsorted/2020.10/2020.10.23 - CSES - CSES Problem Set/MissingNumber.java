package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class MissingNumber {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        boolean[] occur = new boolean[n + 1];
        for (int i = 0; i < n - 1; i++) {
            occur[in.readInt()] = true;
        }
        for(int i = 1; i <= n; i++){
            if(!occur[i]){
                out.println(i);
            }
        }
    }
}
