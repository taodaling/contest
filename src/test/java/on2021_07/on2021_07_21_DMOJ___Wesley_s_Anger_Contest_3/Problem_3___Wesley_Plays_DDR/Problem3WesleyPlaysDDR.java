package on2021_07.on2021_07_21_DMOJ___Wesley_s_Anger_Contest_3.Problem_3___Wesley_Plays_DDR;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;

public class Problem3WesleyPlaysDDR {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.rs();
        int m = in.ri();
        Combo[] combos = new Combo[m];
        for (int i = 0; i < m; i++) {
            combos[i] = new Combo();
            combos[i].s = in.rs();
            combos[i].score = in.ri();
        }
        Arrays.sort(combos, Comparator.comparingInt(x -> -x.s.length()));
        int score = s.length();
        while (s.length() > 0) {
            boolean find = false;
            for(Combo combo : combos){
                if(s.startsWith(combo.s)){
                    s = s.substring(combo.s.length());
                    score += combo.score;
                    find = true;
                    break;
                }
            }
            if(!find){
                s = s.substring(1);
            }
        }
        out.println(score);
    }
}

class Combo {
    String s;
    int score;
}