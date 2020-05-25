package on2020_05.on2020_05_25_Codeforces___Tinkoff_Challenge___Final_Round__Codeforces_Round__414__rated__Div__1___Div__2_.A__Bank_Robbery;



import template.io.FastInput;
import template.io.FastOutput;

public class ABankRobbery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        int n = in.readInt();
        int[] data = new int[n];
        in.populate(data);
        long ans = 0;
        for (int x : data) {
            if (x > b && x < c) {
                ans++;
            }
        }
        out.println(ans);
    }
}
