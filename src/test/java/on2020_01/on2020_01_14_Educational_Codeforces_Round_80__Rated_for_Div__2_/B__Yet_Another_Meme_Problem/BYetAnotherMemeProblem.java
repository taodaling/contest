package on2020_01.on2020_01_14_Educational_Codeforces_Round_80__Rated_for_Div__2_.B__Yet_Another_Meme_Problem;



import template.io.FastInput;
import template.io.FastOutput;

public class BYetAnotherMemeProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readInt();
        long b = in.readInt();
        int cnt = 0;
        for (int i = 9; i <= b; i = i * 10 + 9) {
            cnt++;
        }
        out.println(cnt * a);
    }
}
