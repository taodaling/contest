package on2020_04.on2020_04_13_Codeforces_Round__534__Div__1_.A__Grid_game;



import template.io.FastInput;
import template.io.FastOutput;

public class AGridGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        int[] cnts = new int[2];
        for (char c : s) {
            int v = c - '0';
            if (v == 0) {
                out.append(3).append(' ').append(cnts[v] % 4 + 1).println();
            } else {
                out.append(1).append(' ').append(cnts[v] % 2 * 2 + 1).println();
            }
            cnts[v]++;
        }
    }
}
