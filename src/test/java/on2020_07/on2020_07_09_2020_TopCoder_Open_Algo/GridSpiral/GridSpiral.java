package on2020_07.on2020_07_09_2020_TopCoder_Open_Algo.GridSpiral;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GridSpiral {
    public long findCell(int D) {
        if (D == 1 || D == 3) {
            return 0;
        }
        List<Long> candidate = new ArrayList<>();
        //4d - 3 = D
        if ((D + 3) % 4 == 0) {
            long d = (D + 3) / 4;
            long num = d * d + 1;
            candidate.add(num - D);
        }
        //4d - 1 == D
        if ((D + 1) % 4 == 0) {
            long d = (D + 1) / 4;
            long num = d * d + d + 1;
            candidate.add(num - D);
        }
        //4d + 1 == D
        if ((D - 1) >= 0 && (D - 1) % 4 == 0) {
            long d = (D - 1) / 4;
            long num = d * d + (d + 1) * 2;
            candidate.add(num - D);
        }
        //4d + 3 == D
        if ((D - 3) >= 0 && (D - 3) % 4 == 0) {
            long d = (D - 3) / 4;
            long num = d * d + (d + 1) * 3;
            candidate.add(num - D);
        }
        //4d - 5 == D
        if((D + 5) % 4 == 0){
            long d = (D - 3) / 4;
            long num = d * d;
            candidate.add(num);
        }
        candidate = candidate.stream().filter(x -> x >= 0).collect(Collectors.toList());
        candidate.sort(Comparator.naturalOrder());
        if(candidate.isEmpty()){
            return -1;
        }
        return candidate.get(0);
    }
}
