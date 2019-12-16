package on2019_12.on2019_12_14_Codeforces_Round__606__Div__1__based_on_Technocup_2020_Elimination_Round_4_.A__As_Simple_as_One_and_Two;



import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;

public class AAsSimpleAsOneAndTwo {
    char[] s = new char[2000000];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int len = in.readString(s, 0);
        IntList indexes = new IntList(len);
        for (int i = 0; i < len; i++) {
            boolean prefix = s[i] == 'o' && i >= 2 && s[i - 1] == 'w' && s[i - 2] == 't';
            boolean suffix = s[i] == 'o' && i + 2 < len && s[i + 1] == 'n' && s[i + 2] == 'e';
            if (prefix && suffix) {
                indexes.add(i);
            } else if (prefix) {
                indexes.add(i - 1);
            } else if (suffix) {
                indexes.add(i + 1);
            }
        }

        out.println(indexes.size());
        for (int i = 0; i < indexes.size(); i++) {
            out.append(indexes.get(i) + 1).append(' ');
        }
        out.println();
    }
}
