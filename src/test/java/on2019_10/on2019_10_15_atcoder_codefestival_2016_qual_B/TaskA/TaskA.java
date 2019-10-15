package on2019_10.on2019_10_15_atcoder_codefestival_2016_qual_B.TaskA;



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
