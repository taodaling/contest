package on2019_11.on2019_11_11_Codeforces_Round__586__Div__1___Div__2_.A___Cards;



import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = in.readString().toCharArray();
        int[] cnts = new int[128];
        for(int i = 0; i < n; i++){
            cnts[s[i]]++;
        }
        for(int i = 0; i < cnts['n']; i++){
            out.append(1).append(' ');
        }
        for(int i = 0; i < cnts['r']; i++){
            out.append(0).append(' ');
        }
    }
}
