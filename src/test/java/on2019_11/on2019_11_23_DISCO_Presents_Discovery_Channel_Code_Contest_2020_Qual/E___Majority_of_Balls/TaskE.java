package on2019_11.on2019_11_23_DISCO_Presents_Discovery_Channel_Code_Contest_2020_Qual.E___Majority_of_Balls;



import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.rand.Randomized;

import java.util.Random;

public class TaskE {
    public static void main(String[] args) {
        char[] data = new char[99 * 2];
        for (int i = 0; i < 99; i++) {
            data[i] = 'R';
        }
        for (int i = 0; i < 99; i++) {
            data[i + 99] = 'B';
        }
        Randomized.randomizedArray(data, 0, data.length);
        System.out.println(String.valueOf(data));
    }

    FastInput in;
    FastOutput out;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        this.in = in;
        this.out = out;

        IntList question = new IntList(n);
        boolean state;
        for (int i = 1; i <= n; i++) {
            question.add(i);
        }
        state = redMore(question);
        int red = 1;
        int blue = 2;
        int[] color = new int[2 * n + 1];
        int sep;

        int l = 1;
        int r = n;

        while (l < r) {
            sep = (l + r) >>> 1;
            question.clear();
            for (int j = 1; j <= 2 * n; j++) {
                if (j <= n - sep || j > n && j <= n + sep) {
                    question.add(j);
                }
            }
            if (state != redMore(question)) {
                r = sep;
            } else {
                l = sep + 1;
            }
        }

        sep = r;
        color[n - sep + 1] = state ? red : blue;
        color[n + sep] = 3 - color[n - sep + 1];
        question.clear();
        for (int j = 1; j <= 2 * n; j++) {
            if (j <= n - sep || j > n && j <= n + sep) {
                question.add(j);
            }
        }

        for (int i = 1; i <= n - sep; i++) {
            replace(question, i, n - sep + 1);
            if (state != redMore(question)) {
                color[i] = state ? red : blue;
            } else {
                color[i] = state ? blue : red;
            }
            replace(question, n - sep + 1, i);
        }
        for (int i = n + sep + 1; i <= 2 * n; i++) {
            replace(question, n + sep, i);
            if (state == redMore(question)) {
                color[i] = state ? red : blue;
            } else {
                color[i] = state ? blue : red;
            }
            replace(question, i, n + sep);
        }

        for (int i = n - sep + 2; i <= n; i++) {
            replace(question, n + sep, i);
            if (state == redMore(question)) {
                color[i] = state ? red : blue;
            } else {
                color[i] = state ? blue : red;
            }
            replace(question, i, n + sep);
        }

        for (int i = n + 1; i < n + sep; i++) {
            replace(question, i, n - sep + 1);
            if (state != redMore(question)) {
                color[i] = state ? red : blue;
            } else {
                color[i] = state ? blue : red;
            }
            replace(question, n - sep + 1, i);
        }

        out.append("! ");
        for (int i = 1; i <= 2 * n; i++) {
            out.append(color[i] == red ? "R" : "B");
        }
        out.flush();
    }

    public void replace(IntList x, int a, int b) {
        x.set(x.indexOf(a), b);
    }



    public boolean redMore(IntList a) {
        out.append("? ");
        for (int i = 0; i < a.size(); i++) {
            out.append(a.get(i)).append(' ');
        }
        out.println();
        out.flush();
        return in.readString().equals("Red");
    }
}
