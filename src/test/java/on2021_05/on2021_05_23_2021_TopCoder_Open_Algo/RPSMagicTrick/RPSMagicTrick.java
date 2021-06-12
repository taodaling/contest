package on2021_05.on2021_05_23_2021_TopCoder_Open_Algo.RPSMagicTrick;



import template.math.PermutationUtils;
import template.utils.SequenceUtils;

import java.util.stream.IntStream;

public class RPSMagicTrick {
    public String guess(String exampleGuess, String exampleResponse, String volunteersActions) {
        if (exampleResponse.equals("Wrong.")) {
            exampleGuess = "" + exampleGuess.charAt(1) + exampleGuess.charAt(0);
        }
        int[] cur = IntStream.range('A', 'C' + 1).toArray();
        while (!(exampleGuess.charAt(0) == cur[0] && exampleGuess.charAt(1) == cur[1])) {
            PermutationUtils.nextPermutation(cur);
        }
        StringBuilder ans = new StringBuilder();
        for (char c : volunteersActions.toCharArray()) {
            if (c == '?') {
                ans.append((char) cur[0]).append((char) cur[1]);
            } else {
                SequenceUtils.swap(cur, 0, 1);
            }
        }
        return ans.toString();
    }
}
