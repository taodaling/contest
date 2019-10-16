package on2019_10.on2019_10_15_atcoder_ARC062.TaskD;



import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[100000];
        int n = in.readString(s, 0);
        int score = n / 2;
        for(int i = 0; i < n; i++){
            if(s[i] == 'p'){
                score--;
            }
        }
        out.println(score);
    }
}
