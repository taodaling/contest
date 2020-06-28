package on2020_06.on2020_06_27_Codeforces___Codeforces_Round__405__rated__Div__1__based_on_VK_Cup_2017_Round_1_.A__Bear_and_Different_Names;



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
