package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class EdgeDirections {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        for(int i = 0; i < m; i++){
            int u = in.readInt();
            int v = in.readInt();
            if(u > v){
                int tmp = u;
                u = v;
                v = tmp;
            }
            out.append(u).append(' ').append(v).println();
        }
    }
}
