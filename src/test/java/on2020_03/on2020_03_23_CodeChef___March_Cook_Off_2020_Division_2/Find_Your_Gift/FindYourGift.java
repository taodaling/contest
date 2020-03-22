package on2020_03.on2020_03_23_CodeChef___March_Cook_Off_2020_Division_2.Find_Your_Gift;



import template.io.FastInput;
import template.io.FastOutput;

public class FindYourGift {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int type = -1;
        int x = 0;
        int y = 0;

        for (int i = 0; i < n; i++) {
            char c = in.readChar();
            int t = c == 'U' || c == 'D' ? 1 : 0;
            if (type == t) {
                continue;
            }
            type = t;
            if (c == 'L') {
                x--;
            } else if (c == 'R') {
                x++;
            } else if (c == 'U') {
                y++;
            } else {
                y--;
            }
        }

        out.append(x).append(' ').append(y).println();
    }
}
