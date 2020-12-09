package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class BHTML10 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Deque<Boolean> dq = new ArrayDeque<>();
        char[] s = in.rs().toCharArray();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '<') {
                if(s[i + 1] == '/'){
                    dq.removeLast();
                }else{
                    Boolean v = s[i + 1] == 'U' ? true : false;
                    dq.addLast(v);
                }
                while(s[i] != '>'){
                    i++;
                }
                continue;
            }
            if(dq.isEmpty()){
                out.append(s[i]);
            }else if(dq.peekLast()){
                out.append(Character.toUpperCase(s[i]));
            }else{
                out.append(Character.toLowerCase(s[i]));
            }
        }
    }
}
