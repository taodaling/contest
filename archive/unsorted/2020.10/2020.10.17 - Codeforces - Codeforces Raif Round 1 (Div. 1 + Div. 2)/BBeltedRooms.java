package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class BBeltedRooms {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);
        boolean leftOccur = false;
        boolean rightOccur = false;
        for (char c : s) {
            if (c == '<') {
                leftOccur = true;
            }
            if (c == '>') {
                rightOccur = true;
            }
        }
        if (!leftOccur || !rightOccur) {
            out.println(n);
            return;
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            char left = s[(i + n - 1) % n];
            char right = s[i];
            if (left == '-' || right == '-') {
                ans++;
            }
        }
        out.println(ans);
    }
}
