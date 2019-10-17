package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int sign = 0;
        for(int i = 0; i < n; i++){
            int a = in.readInt();
            if(a % 2 == 0){
                out.println(a / 2);
            }else if(sign == 0){
                sign = 1;
                out.println(Math.floorDiv(a, 2));
            }else{
                sign = 0;
                out.println(Math.floorDiv(a, 2) + 1);
            }
        }
    }
}
