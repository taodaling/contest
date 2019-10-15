package contest;

import template.FastInput;
import java.io.PrintWriter;

public class TaskA {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        String target = "CODEFESTIVAL2016";
        String source = in.readString();
        int n = target.length();
        int need = 0;
        for(int i = 0; i < n; i++){
            if(source.charAt(i) != target.charAt(i)){
                need++;
            }
        }
        out.println(need);
    }
}
