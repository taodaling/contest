package on2020_10.on2020_10_20_Library_Checker.Segment_Add_Get_Min;



import template.datastructure.LongLiChaoSegment;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SegmentAddGetMin {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int q = in.readInt();

        List<long[]> predefined = new ArrayList<>(n);
        List<long[]> querys = new ArrayList<>(q);
        LongArrayList list = new LongArrayList((n + q) * 2);
        for (int i = 0; i < n; i++) {
            long[] data = new long[4];
            in.populate(data);
            data[1]--;
            list.add(data[0]);
            list.add(data[1]);
            predefined.add(data);
        }
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 0) {
                long[] data = new long[5];
                data[0] = t;
                for (int j = 1; j < 5; j++) {
                    data[j] = in.readLong();
                }
                data[2]--;
                querys.add(data);
                list.add(data[1]);
                list.add(data[2]);
            } else {
                long[] data = new long[2];
                data[0] = t;
                data[1] = in.readLong();
                querys.add(data);
                list.add(data[1]);
            }
        }

        list.unique();
        int m = list.size();
        IntToLongFunction func = i -> list.get(i);

        LongLiChaoSegment segment = new LongLiChaoSegment(0, m - 1);
        for (long[] line : predefined) {
            segment.update(list.binarySearch(line[0]), list.binarySearch(line[1]), 0, m - 1, new LongLiChaoSegment.Line(-line[2], -line[3]), func);
        }
        for (long[] query : querys) {
            if (query[0] == 0) {
                segment.update(list.binarySearch(query[1]), list.binarySearch(query[2]), 0, m - 1, new LongLiChaoSegment.Line(-query[3], -query[4]), func);
            } else {
                int pt = list.binarySearch(query[1]);
                long ans = -segment.query(pt, pt, 0, m - 1, query[1]);
                if (Math.abs(ans) == LongLiChaoSegment.INF) {
                    out.println("INFINITY");
                } else {
                    out.println(ans);
                }
            }
        }
    }
}
