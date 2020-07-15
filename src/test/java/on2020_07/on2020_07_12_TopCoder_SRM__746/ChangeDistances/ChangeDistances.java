package on2020_07.on2020_07_12_TopCoder_SRM__746.ChangeDistances;



import java.util.ArrayList;
import java.util.List;

public class ChangeDistances {
    public String[] findGraph(String[] g) {
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < g.length; i++) {
            StringBuilder builder = new StringBuilder(g[i]);
            for(int j = 0; j < g.length; j++){
                if(i == j){
                    continue;
                }
                builder.setCharAt(j, (char) ('1' - builder.charAt(j) + '0'));
            }
            ans.add(builder.toString());
        }
        return ans.toArray(new String[0]);
    }
}
