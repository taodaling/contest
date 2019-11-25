package on2019_11.on2019_11_25_Educational_Codeforces_Round_60__Rated_for_Div__2_.E___Decypher_the_String;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitBase;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] t = in.readString().toCharArray();
        int n = t.length;
        int charset = 'z' - 'a' + 1;
        DigitBase base = new DigitBase(charset);
        int[] perms = new int[n];
        for (int i = 0; i < 3; i++) {
            out.append("? ");
            for (int j = 0; j < n; j++) {
                out.append((char) (base.getBit(j, i) + 'a'));
            }
            out.println();
            out.flush();
            for (int j = 0; j < n; j++) {
                int x = in.readChar() - 'a';
                perms[j] = (int) base.setBit(perms[j], i, x);
            }
        }
        char[] ans = new char[n];
        for (int i = 0; i < n; i++) {
            ans[perms[i]] = t[i];
        }
        out.append("! ");
        for (int i = 0; i < n; i++) {
            out.append(ans[i]);
        }
        out.println();
        out.flush();
    }
}
