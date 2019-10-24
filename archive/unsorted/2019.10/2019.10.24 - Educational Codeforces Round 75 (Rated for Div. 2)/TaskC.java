package contest;

import template.CharList;
import template.FastInput;
import template.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class TaskC {
    char[] s = new char[300000];
    Deque<Character>[] dqs = new Deque[]{new ArrayDeque(300000),
            new ArrayDeque(300000)};

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            dqs[s[i] % 2].addLast(s[i]);
        }

        while(dqs[0].size() + dqs[1].size() > 0){
            if(dqs[0].size() > 0 && (dqs[1].size() == 0 || dqs[0].peekFirst().compareTo(dqs[1].peekFirst()) <= 0)){
                out.append(dqs[0].removeFirst().charValue());
            }else{
                out.append(dqs[1].removeFirst().charValue());
            }
        }

        out.println();
    }
}
