package on2020_02.on2020_02_28_Codeforces_Round__453__Div__1_.A__Hashing_Trees;



import template.io.FastInput;
import template.io.FastOutput;

public class AHashingTrees {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int[] a = new int[h + 1];
        for(int i = 0; i <= h; i++){
            a[i] = in.readInt();
        }
        boolean valid = true;
        for(int i = 1; i <= h; i++){
            if(a[i - 1] >= 2 && a[i] >= 2){
                valid = false;
            }
        }
        if(valid){
            out.println("perfect");
            return;
        }
        out.println("ambiguous");
        int id = 1;
        out.append(0).append(' ');
        int[] last = new int[]{id};
        for(int i = 1; i <= h; i++){
            int[] cur = new int[a[i]];
            for(int j = 0; j < a[i]; j++){
                cur[j] = ++id;
                out.append(last[0]).append(' ');
            }
            last = cur;
        }
        out.println();
        out.append(0).append(' ');
        id = 1;
        last = new int[]{id};
        for(int i = 1; i <= h; i++){
            int[] cur = new int[a[i]];
            for(int j = 0; j < a[i]; j++){
                cur[j] = ++id;
                out.append(last[j % last.length]).append(' ');
            }
            last = cur;
        }
        out.println();
    }
}
