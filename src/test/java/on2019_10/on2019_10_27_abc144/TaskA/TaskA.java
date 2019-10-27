package on2019_10.on2019_10_27_abc144.TaskA;



import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        if(a >= 10 || b >= 10){
            out.println(-1);
            return;
        }
        out.println(a * b);
    }
}
