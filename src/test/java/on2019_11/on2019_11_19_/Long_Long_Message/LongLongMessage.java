package on2019_11.on2019_11_19_.Long_Long_Message;



import template.FastInput;
import template.FastOutput;
import template.SAIS;

public class LongLongMessage {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[200001];
        int len1 = in.readString(s, 0);
        s[len1] = '$';
        int len2 = in.readString(s, len1 + 1);
        SAIS sais = new SAIS(s, 0, len1 + len2);
        int ans = 0;
        for(int i = 1; i <= len1 + len2; i++){
            int f1 = sais.queryKth(i - 1);
            int f2 = sais.queryKth(i);
            if(f1 < len1 && f2 > len1 || f1 > len1 && f2 < len1){
                ans = Math.max(ans, sais.longestCommonPrefixBetween(i));
            }
        }
        out.println(ans);
    }
}
