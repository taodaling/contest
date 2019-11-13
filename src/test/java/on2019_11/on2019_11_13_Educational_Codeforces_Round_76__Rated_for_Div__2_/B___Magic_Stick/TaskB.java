package on2019_11.on2019_11_13_Educational_Codeforces_Round_76__Rated_for_Div__2_.B___Magic_Stick;



import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int y = in.readInt();

        if(x == 1){
            out.println(y <= 1 ? "YES" : "NO");
            return;
        }

        if(x <= 3){
            out.println(y <= 3 ? "YES" : "NO");
            return;
        }

        out.println("YES");
    }
}
