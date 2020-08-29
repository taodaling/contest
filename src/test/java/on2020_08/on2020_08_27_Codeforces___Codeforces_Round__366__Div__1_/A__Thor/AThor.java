package on2020_08.on2020_08_27_Codeforces___Codeforces_Round__366__Div__1_.A__Thor;



import template.datastructure.MultiWayDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerMultiWayDeque;

public class AThor {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int read = 0;
        int total = 0;
        boolean[] readed = new boolean[q];
        IntegerDeque dq = new IntegerDequeImpl(q);
        IntegerMultiWayDeque mdq = new IntegerMultiWayDeque(n, q);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int x = in.readInt() - 1;
                dq.addLast(i);
                mdq.addLast(x, i);
                total++;
            }else if(t == 2){
                int x = in.readInt() - 1;
                while(!mdq.isEmpty(x)){
                    int head = mdq.removeFirst(x);
                    if(!readed[head]) {
                        readed[head] = true;
                        total--;
                    }
                }
            }else{
                int x = in.readInt();
                while(read < x){
                    int head = dq.removeFirst();
                    if(!readed[head]){
                        readed[head] = true;
                        total--;
                    }
                    read++;
                }
            }

            out.println(total);
        }
    }
}
