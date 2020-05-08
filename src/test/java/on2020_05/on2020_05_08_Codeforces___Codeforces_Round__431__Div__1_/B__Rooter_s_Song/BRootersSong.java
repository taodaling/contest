package on2020_05.on2020_05_08_Codeforces___Codeforces_Round__431__Div__1_.B__Rooter_s_Song;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.CollectionUtils;
import template.utils.SequenceUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BRootersSong {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int w = in.readInt();
        int h = in.readInt();

        List<Dancer> allDancer = new ArrayList<>(n);
        List<Dancer> vertical = new ArrayList<>(n);
        List<Dancer> horizontal = new ArrayList<>(n);

        IntegerList times = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            int g = in.readInt();
            int p = in.readInt();
            int t = in.readInt();
            Dancer d = new Dancer();
            d.p = p;
            d.time = (w + h - p) + t;
            times.add(d.time);

            allDancer.add(d);
            if (g == 1) {
                vertical.add(d);
            } else {
                horizontal.add(d);
            }
        }

        Map<Integer, List<Dancer>> vMap = vertical.stream().collect(Collectors.groupingBy(x -> x.time));
        Map<Integer, List<Dancer>> hMap = horizontal.stream().collect(Collectors.groupingBy(x -> x.time));

        times.unique();
        List<int[]> ends = new ArrayList<>(n);
        for (IntegerIterator iterator = times.iterator(); iterator.hasNext(); ) {
            int t = iterator.next();

            List<Dancer> vList = vMap.getOrDefault(t, Collections.emptyList());
            List<Dancer> hList = hMap.getOrDefault(t, Collections.emptyList());
            vList.sort((a, b) -> Integer.compare(a.p, b.p));
            hList.sort((a, b) -> Integer.compare(a.p, b.p));
            Collections.reverse(hList);

            ends.clear();
            for (Dancer v : vList) {
                ends.add(new int[]{v.p, h});
            }
            for (Dancer v : hList) {
                ends.add(new int[]{w, v.p});
            }

            int offset = 0;
            for (Dancer v : hList) {
                int[] val = ends.get(offset++);
                v.x = val[0];
                v.y = val[1];
            }
            for (Dancer v : vList) {
                int[] val = ends.get(offset++);
                v.x = val[0];
                v.y = val[1];
            }
        }

        for(Dancer d : allDancer){
            out.append(d.x).append(' ').append(d.y).println();
        }
    }
}

class Dancer {
    int time;
    int p;
    int x;
    int y;
}