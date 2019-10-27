package on2019_10.on2019_10_27_abc144.TaskC;



import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long min = n + 1;
        for(int i = 1; (long)i * i <= n; i++){
            if(n % i != 0){
                continue;
            }
            min = Math.min(min, i + n / i);
        }
        out.println(min - 2);
    }
}
