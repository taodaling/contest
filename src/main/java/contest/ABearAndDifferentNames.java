package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ABearAndDifferentNames {
    char[] name = new char[2];

    {
        name[0] = 'A';
        name[1] = 'a';
    }

    private String name() {
        String ans = String.valueOf(name);
        name[0]++;
        if (name[0] > 'Z') {
            name[0] = 'A';
            name[1]++;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        String[] names = new String[n];
        for (int i = 0; i < k - 1; i++) {
            names[i] = name();
        }
        for (int i = k - 1; i < n; i++) {
            if (in.readString().equals("YES")) {
                names[i] = name();
            } else {
                names[i] = names[i - (k - 1)];
            }
        }

        for(String s : names){
            out.println(s);
        }
    }
}
