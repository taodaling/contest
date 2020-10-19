package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class BBashsBigDay {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int limit = (int) 1e5;
        int[] cnts = new int[limit + 1];
        for (int i = 0; i < n; i++) {
            cnts[in.readInt()]++;
        }
        int max = 1;
        for(int i = 2; i <= limit; i++){
            int sum = 0;
            for(int j = i; j <= limit; j += i){
                sum += cnts[j];
            }
            max = Math.max(max, sum);
        }

        out.println(max);
    }
}
