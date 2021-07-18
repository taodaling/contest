package on2021_07.on2021_07_17_Codeforces___Codeforces_Round__733__Div__1___Div__2__based_on_VK_Cup_2021___Elimination__Engine__.D__Secret_Santa;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class DSecretSanta {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        debug.debug("test", testNumber);
        int n = in.ri();
        int[] a = in.ri(n);
        for(int i = 0; i < n; i++){
            a[i]--;
        }
        int[] b = new int[n];
        boolean[] used = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (used[a[i]]) {
                b[i] = -1;
            } else {
                used[a[i]] = true;
                b[i] = a[i];
            }
        }
        IntegerArrayList cand = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                cand.add(i);
            }
        }
        for (int i = 0; i < n; i++) {
            if (b[i] == -1) {
                b[i] = select(cand, i);
            }
        }
        debug.debug("b", b);
        for (int i = 0; i < n; i++) {
            if (b[i] == i) {
                //cool
                for (int j = 0; j < n; j++) {
                    if (b[j] == a[i]) {
                        b[j] = i;
                        break;
                    }
                }
                b[i] = a[i];
                break;
            }
        }

        int fill = 0;
        for(int i = 0; i < n; i++){
            assert b[i] != i;
            if(a[i] == b[i]){
                fill++;
            }
        }
        out.println(fill);
        for(int i = 0; i < n; i++){
            out.append(b[i] + 1).append(' ');
        }
        out.println();
    }

    public int select(IntegerArrayList cand, int me) {
        int back = cand.pop();
        if (back == me) {
            if (!cand.isEmpty()) {
                int next = cand.pop();
                cand.add(back);
                back = next;
            }
        }
        return back;
    }
}
