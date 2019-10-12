package on2019_10.on2019_10_11_Atcoder_codefestival_2016.TaskB;



import template.FastInput;
import java.io.PrintWriter;

public class TaskB {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] like = new int[n + 1];
        for(int i = 1; i <= n; i++){
            like[i] = in.readInt();
        }

        int cnt = 0;
        for(int i = 1; i <= n; i++){
            if(like[like[i]] == i){
                cnt++;
            }
        }

        out.println(cnt / 2);
    }
}
