package on2020_01.on2020_01_17_POJ___POJ_Monthly__2006_03_26.Long_Long_Message;





import template.datastructure.SuffixTree;
import template.io.FastInput;
import template.io.FastOutput;

public class LongLongMessage {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] a = new int[100000 + 1];
        int[] b = new int[100000 + 1];
        int aLen = in.readString(a, 0);
        int bLen = in.readString(b, 0);

        int[] data = new int[aLen + 1 + bLen + 1];
        System.arraycopy(a, 0, data, 0, aLen);
        data[aLen] = 'z' + 1;
        System.arraycopy(b, 0, data, aLen + 1, bLen);
        data[aLen + 1 + bLen] = 'z' + 2;

//        SuffixTree st = new SuffixTree(data);
//        int ans = st.lcs(aLen - 1);
//        out.println(ans);
    }
}
