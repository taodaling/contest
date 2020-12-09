package contest;

import template.datastructure.LinkedListBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class TextEditor {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        LinkedListBeta<Character> list = new LinkedListBeta<>();
        LinkedListBeta.Node<Character> now = list.dummy;
        while (in.hasMore()) {
            char c = in.rc();
            if (c == 'L') {
                if (now != list.dummy) {
                    now = now.prev;
                }
            } else if (c == 'R') {
                if (now.next != list.dummy) {
                    now = now.next;
                }
            } else {
                now = list.addAfter(now, c);
            }
        }

        for (char c : list) {
            out.append(c);
        }
    }
}
