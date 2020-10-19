package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.LongHashMap;

import java.io.PrintWriter;

public class AssociativeArray {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int q = in.readInt();
        LongHashMap lhm = new LongHashMap(q, false);
        for(int i = 0; i < q; i++){
            int t = in.readInt();
            if(t == 0){
                lhm.put(in.readLong(), in.readLong());
            }else{
                out.println(lhm.get(in.readLong()));
            }
        }
    }
}
