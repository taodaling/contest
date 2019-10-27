package contest;

import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        for(int i = 1; i < 10; i++){
            for(int j = 1; j < 10; j++){
                if(n == i * j){
                    out.println("Yes");
                    return;
                }
            }
        }
        out.println("No");
    }
}
