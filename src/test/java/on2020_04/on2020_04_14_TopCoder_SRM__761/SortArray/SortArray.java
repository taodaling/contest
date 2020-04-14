package on2020_04.on2020_04_14_TopCoder_SRM__761.SortArray;



import template.binary.Bits;

public class SortArray {
    public int[] verify(int N, int[] commands) {
        int[] vals = new int[N];
        for (int i = 0; i < (1 << N); i++) {
            for (int j = 0; j < N; j++) {
                vals[j] = Bits.bitAt(i, j);
            }
            sort(N, commands, vals);
            boolean order = true;
            for (int j = 1; j < N; j++) {
                order = order && vals[j] >= vals[j - 1];
            }
            if (!order) {
                int[] offsets = new int[2];
                offsets[0] = 0;
                offsets[1] = N - Integer.bitCount(i);
                for (int j = 0; j < N; j++) {
                    vals[j] = offsets[vals[j]]++;
                }
                return vals;
            }
        }
        return new int[0];
    }

    public void sort(int N, int[] commands, int[] vals) {
        for (int cmd : commands) {
            int cnt = 0;
            for (int j = 0; j < N; j++) {
                if (Bits.bitAt(cmd, j) == 0) {
                    continue;
                }
                cnt += vals[j];
            }
            for (int j = N - 1; j >= 0; j--) {
                if (Bits.bitAt(cmd, j) == 0) {
                    continue;
                }
                if (cnt > 0) {
                    vals[j] = 1;
                    cnt--;
                } else {
                    vals[j] = 0;
                }
            }
        }
    }
}
