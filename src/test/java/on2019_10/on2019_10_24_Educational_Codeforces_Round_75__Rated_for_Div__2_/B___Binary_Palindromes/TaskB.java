package on2019_10.on2019_10_24_Educational_Codeforces_Round_75__Rated_for_Div__2_.B___Binary_Palindromes;



import template.FastInput;
import template.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<Integer> lens = new ArrayList<>();
        int[] cnts = new int[2];
        for(int i = 0; i < n; i++){
            char[] s = in.readString().toCharArray();
            lens.add(s.length);
            for(char c : s){
                cnts[c - '0']++;
            }
        }

        int pair = cnts[0] / 2 + cnts[1] / 2;
        int single = cnts[0] % 2 + cnts[1] % 2;
        lens.sort(Comparator.naturalOrder());
        int cnt = 0;
        for(int len : lens){
            int pairNeed = len / 2;
            int singleNeed = len % 2;

            if(singleNeed > single && pair > 0){
                pair--;
                single += 2;
            }

            if(pairNeed > pair || singleNeed > single){
                break;
            }

            pair -= pairNeed;
            singleNeed -= singleNeed;

            cnt++;
        }

        out.println(cnt);
    }
}
