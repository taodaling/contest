package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.*;

public class BookPile {
    List<String> bottom;
    Deque<String> dq;
    int k;
    boolean rev;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        k = in.readInt();
        bottom = new ArrayList<>(n + m);
        dq = new ArrayDeque<>(k + 1);

        for (int i = 0; i < n; i++) {
            String s = in.readString();
            addFirst(s);
        }
        while (dq.size() > k) {
            bottom.add(removeFirst());
        }
        char[] cmd = new char[128];
        for (int i = 0; i < m; i++) {
            int len = in.readString(cmd, 0);
            if (cmd[0] == 'A') {
                int l = -1;
                int r = -1;
                for (int j = 0; j < len; j++) {
                    if (cmd[j] == '(') {
                        l = j + 1;
                    }
                    if (cmd[j] == ')') {
                        r = j - 1;
                    }
                }
                String s = String.valueOf(cmd, l, r - l + 1);
                add(s);
            } else {
                rotate();
            }
        }

        while (!dq.isEmpty()) {
            bottom.add(removeFirst());
        }
        SequenceUtils.reverse(bottom);
        for (String s : bottom) {
            out.println(s);
        }
    }

    public void addFirst(String s) {
        if (rev) {
            dq.addLast(s);
        } else {
            dq.addFirst(s);
        }
    }

    public void addLast(String s) {
        if (rev) {
            dq.addFirst(s);
        } else {
            dq.addLast(s);
        }
    }

    public String removeFirst() {
        if (rev) {
            return dq.removeLast();
        } else {
            return dq.removeFirst();
        }
    }

    public String removeLast() {
        if (rev) {
            return dq.removeFirst();
        } else {
            return dq.removeLast();
        }
    }

    public void add(String s) {
        addLast(s);
        if (dq.size() > k) {
            bottom.add(removeFirst());
        }
    }

    public void rotate() {
        rev = !rev;
    }
}
