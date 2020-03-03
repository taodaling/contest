package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerList;

public class BKuroniAndSimpleStrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        IntegerList left = new IntegerList(s.length);
        IntegerList right = new IntegerList(s.length);

        check(s, 0, s.length - 1, left, right);
        if(left.size() == 0){
            out.println(0);
            return;
        }
        out.println(1);
        out.println(left.size() + right.size());
        right.reverse();
        for(int i = 0; i < left.size(); i++){
            out.append(left.get(i) + 1).append(' ');
        }
        for(int i = 0; i < right.size(); i++){
            out.append(right.get(i) + 1).append(' ');
        }
    }

    public void check(char[] s, int l, int r, IntegerList left, IntegerList right) {
        if (l >= r) {
            return;
        }
        if (s[l] == '(' && s[r] == ')') {
            left.add(l);
            right.add(r);
            l++;
            r--;
        } else if (s[l] == '(') {
            r--;
        } else {
            l++;
        }
        check(s, l, r, left, right);
    }
}
