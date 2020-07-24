package on2020_07.on2020_07_21_Codeforces___Codeforces_Round__658__Div__1_.C__Mastermind;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.PriorityQueue;

public class CMastermind {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        int oldX = x;
        int oldY = y;
        y -= x;
        int[] b = new int[n];
        in.populate(b);
        for (int i = 0; i < n; i++) {
            b[i]--;
        }
        int[] cnts = new int[n + 1];
        for (int q : b) {
            cnts[q]++;
        }
        int notExist = -1;
        for (int i = 0; i <= n; i++) {
            if (cnts[i] == 0) {
                notExist = i;
            }
        }
        Color[] colors = new Color[n + 1];
        for (int i = 0; i <= n; i++) {
            colors[i] = new Color();
            colors[i].c = i;
            colors[i].dq = new IntegerDequeImpl(cnts[i]);
        }
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            colors[b[i]].dq.addLast(i);
        }
        PriorityQueue<Color> pq = new PriorityQueue<>(n, (q, w) -> -Integer.compare(q.dq.size(), w.dq.size()));
        pq.addAll(Arrays.asList(colors));
        Arrays.fill(ans, notExist);
        while (x > 0) {
            Color top = pq.remove();
            ans[top.dq.removeFirst()] = top.c;
            pq.add(top);
            x--;
        }

        IntegerArrayList list = new IntegerArrayList(n);
        int remain = n - oldX;
        int max = 0;
        for (Color c : colors) {

            while (c.dq.size() * 2 > remain) {
                remain--;
                c.dq.removeFirst();
            }
            list.addAll(c.dq.iterator());
            max = Math.max(max, c.dq.size());
        }
        IntegerArrayList rotated = new IntegerArrayList(list);
        if(rotated.size() > 0) {
            SequenceUtils.rotate(rotated.getData(), 0, rotated.size() - 1, max % rotated.size());
        }
        if (list.size() < y) {
            out.println("NO");
            return;
        }
        for (int i = 0; i < y; i++) {
            int from = rotated.get(i);
            int rotate = list.get(i);
            ans[rotate] = b[from];
        }

        out.println("YES");
        for (int i = 0; i < n; i++) {
            out.append(ans[i] + 1).append(' ');
        }
//
//        if (oldX != getX(b, ans)) {
//            throw new RuntimeException();
//        }
//        if (oldY != getY(b, ans)) {
//            throw new RuntimeException();
//        }
        out.println();
    }

    public static int getX(int[] a, int[] b) {
        int ans = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b[i]) {
                ans++;
            }
        }
        return ans;
    }

    public static int getY(int[] a, int[] b) {
        int[] cnts = new int[a.length + 1];
        for (int x : a) {
            cnts[x]++;
        }
        int match = 0;
        for (int x : b) {
            cnts[x]--;
            if (cnts[x] >= 0) {
                match++;
            }
        }
        return match;
    }
}

class Color {
    int c;
    IntegerDequeImpl dq;
}
