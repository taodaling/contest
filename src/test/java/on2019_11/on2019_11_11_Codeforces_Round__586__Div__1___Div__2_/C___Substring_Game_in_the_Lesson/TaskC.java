package on2019_11.on2019_11_11_Codeforces_Round__586__Div__1___Div__2_.C___Substring_Game_in_the_Lesson;



import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[500000];
        int n = in.readString(s, 0);

        char minChar = 'z';
        for (int i = 0; i < n; i++) {
            if (s[i] <= minChar) {
                out.println("Mike");
            } else {
                out.println("Ann");
            }
            minChar = (char) Math.min(minChar, s[i]);
        }
    }
}
