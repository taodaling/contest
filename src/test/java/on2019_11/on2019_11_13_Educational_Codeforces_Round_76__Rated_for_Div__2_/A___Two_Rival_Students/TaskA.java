package on2019_11.on2019_11_13_Educational_Codeforces_Round_76__Rated_for_Div__2_.A___Two_Rival_Students;



import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int a = in.readInt();
        int b = in.readInt();

        int dist = Math.min(Math.abs(a - b) + x, n - 1);
        out.println(dist);
    }
}
