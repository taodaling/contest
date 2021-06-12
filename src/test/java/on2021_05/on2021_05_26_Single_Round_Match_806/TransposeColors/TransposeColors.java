package on2021_05.on2021_05_26_Single_Round_Match_806.TransposeColors;



import java.util.ArrayList;
import java.util.List;

public class TransposeColors {
    int N;
    int id(int i, int j){
        return i * N + j;
    }
    int empty(){
        return N * N;
    }
    public int[] move(int N) {
        this.N = N;
        if(N == 1){
            return new int[0];
        }
        List<Integer> seq = new ArrayList<>();
        seq.add(id(1, 0));
        for(int i = 1; i < N; i++){
            int y = i;
            int j;
            for(j = 0; y < N; j++, y++){
                seq.add(id(j, y));
            }
            y = i - 1;
            for(int t = N - 1; y >= 0; t--, y--){
                seq.add(id(t, y));
            }
        }
        seq.remove(seq.size() - 1);
        seq.add(empty());
        return seq.stream().mapToInt(Integer::intValue).toArray();
    }
}
