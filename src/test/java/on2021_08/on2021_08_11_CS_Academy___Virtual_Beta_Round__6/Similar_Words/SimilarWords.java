package on2021_08.on2021_08_11_CS_Academy___Virtual_Beta_Round__6.Similar_Words;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class SimilarWords {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        String s = in.rs();
        int ans = 0;
        for(int i = 0; i < n; i++){
            debug.debug("i", i);
            String t = in.rs();
            if(similar(s, t) || contain(s, t) || contain(t, s)){
                debug.log("true");
                ans++;
            }
        }
        out.println(ans);
    }

    public boolean similar(String a, String b){
        if(a.length() != b.length()){
            return false;
        }
        int diff = 0;
        for(int i = 0; i < a.length(); i++){
            if(a.charAt(i) != b.charAt(i)){
                diff++;
            }
        }
        return diff <= 1;
    }

    public boolean contain(String a, String b){
        if(a.length() != b.length() + 1){
            return false;
        }
        int match = 0;
        for(int i = 0; i < a.length(); i++){
            if(match < b.length() && b.charAt(match) == a.charAt(i)){
                match++;
            }
        }
        return match == b.length();
    }
}
