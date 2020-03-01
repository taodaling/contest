package on2020_02.on2020_02_29_Codeforces_Round__513_by_Barcelona_Bootcamp__rated__Div__1___Div__2_.A__Phone_Numbers;



import template.io.FastInput;
import template.io.FastOutput;

public class APhoneNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int eight = 0;
        for (int i = 0; i < n; i++) {
            if (in.readChar() == '8') {
                eight++;
            }
        }
        int ans = Math.min(n / 11, eight);
        out.println(ans);
    }
}
