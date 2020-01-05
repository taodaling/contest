package on2020_01.on2020_01_05_Codeforces_Round__612__Div__1_.C1__Madhouse__Easy_version_;



import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProbCInteractor {
    public Verdict interact(InputStream input, InputStream solutionOutput, OutputStream solutionInput, State<Boolean> state) {
        FastInput fi = new FastInput(input);
        FastInput si = new FastInput(solutionOutput);
        FastOutput so = new FastOutput(solutionInput);

        String s = fi.readString();
        so.println(s.length());
        so.flush();
        int time = 0;
        while(true){
            if(si.readChar() == '?'){
                if(++time > 3){
                    return Verdict.WA;
                }
                for(String x : all(s.substring(si.readInt() - 1, si.readInt()))){
                    so.println(x);
                }
                so.flush();
            }else{
                return s.equals(si.readString()) ? Verdict.OK : Verdict.WA;
            }
        }
    }

    public List<String> all(String s) {
        List<String> ans = new ArrayList<>();
        for(int i = 0; i < s.length(); i++){
            for(int j = i; j < s.length(); j++){
                ans.add(shuffle(s.substring(i, j + 1)));
            }
        }
        Randomized.randomizedList(ans);
        return ans;
    }

    public String shuffle(String s) {
        char[] seq = s.toCharArray();
        Randomized.randomizedArray(seq);
        return new String(seq);
    }
}
