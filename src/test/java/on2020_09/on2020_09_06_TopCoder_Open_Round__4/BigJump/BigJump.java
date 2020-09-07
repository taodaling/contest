package on2020_09.on2020_09_06_TopCoder_Open_Round__4.BigJump;



import template.datastructure.DSU;
import template.utils.SequenceUtils;

public class BigJump {
    public int[] construct(int n) {
        DSU dsu = new DSU(n);
        dsu.reset();
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            int match = (i * 2 - (i / (n / 2)) + n) % n;
            next[i] = match;
            dsu.merge(i, next[i]);
        }
        for (int i = 0; i < n / 2; i++) {
            int a = i;
            int b = i + n / 2;
            if (dsu.find(a) != dsu.find(b)) {
                SequenceUtils.swap(next, a, b);
                dsu.merge(a, b);
            }
        }
        int[] ans = new int[n];
        int wpos = 0;
        int cur = 0;
        while (true) {
            ans[wpos++] = cur;
            cur = next[cur];
            if (cur == 0) {
                break;
            }
        }
        return ans;
    }
}
