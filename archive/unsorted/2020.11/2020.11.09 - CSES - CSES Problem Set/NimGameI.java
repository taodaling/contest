package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class NimGameI {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int xor = 0;
        for(int i = 0; i < n; i++){
            xor ^= in.readInt();
        }
        if(xor == 0){
            out.println("second");
        }else{
            out.println("first");
        }
    }
}
