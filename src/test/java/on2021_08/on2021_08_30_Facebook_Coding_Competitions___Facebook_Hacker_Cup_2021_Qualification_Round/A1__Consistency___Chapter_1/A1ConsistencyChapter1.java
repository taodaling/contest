package on2021_08.on2021_08_30_Facebook_Coding_Competitions___Facebook_Hacker_Cup_2021_Qualification_Round.A1__Consistency___Chapter_1;



import template.io.FastInput;
import template.io.FastOutput;

public class A1ConsistencyChapter1 {
    int[] type = new int[128];

    {
        for (char c : "AEIOU".toCharArray()) {
            type[c] = 1;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int ans = (int)1e9;
        for(int i = 'A'; i <= 'Z'; i++){
            ans = Math.min(ans, solve(s, i));
        }
        out.printf("Case #%d: %d", testNumber, ans).println();
    }
    public int solve(char[] s, int x){
        int ans = 0;
        for(int i = 0; i < s.length; i++){
            if(s[i] == x){
                continue;
            }
            if(type[s[i]] != type[x]){
                ans++;
            }else{
                ans += 2;
            }
        }
        return ans;
    }
}
