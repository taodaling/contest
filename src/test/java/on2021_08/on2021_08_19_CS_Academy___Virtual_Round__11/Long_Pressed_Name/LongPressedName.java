package on2021_08.on2021_08_19_CS_Academy___Virtual_Round__11.Long_Pressed_Name;



import template.io.FastInput;
import template.io.FastOutput;

public class LongPressedName {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String a = in.rs();
        String b = in.rs();
        int last = -1;
        int matchIndex = 0;
        for (char c : b.toCharArray()) {
            if (matchIndex < a.length() && a.charAt(matchIndex) == c) {
                last = a.charAt(matchIndex++);
                continue;
            }
            if (last != c) {
                out.println(0);
                return;
            }
        }
        if(matchIndex == a.length()) {
            out.println(1);
        }else{
            out.println(0);
        }
    }
}
