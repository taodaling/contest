package on2019_10.on2019_10_29_338679.TaskB1;



import template.BulkOperationArray;
import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int k = in.readInt();
        int[] r = new int[k + 1];
        for (int i = 1; i <= k; i++) {
            r[i] = in.readInt();
        }


        BulkOperationArray b = new BulkOperationArray(0, x);
        BulkOperationArray[] blocks = new BulkOperationArray[k + 1];
        blocks[0] = b.clone();
        for (int i = 1; i <= k; i++) {
            if (i % 2 == 1) {
                // lose
                b.bulkSubtract(r[i] - r[i - 1]);
            } else {
                b.bulkAdd(r[i] - r[i - 1]);
            }
            b.truncateMax(x);
            b.truncateMin(0);
            blocks[i] = b.clone();
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            int a = in.readInt();

            int index = SequenceUtils.floorIndex(r, t);
            int remain = (int)blocks[index].get(a);

            if (index % 2 == 0) {
                // lose
                remain = Math.max(0, remain - (t - r[index]));
            } else {
                remain = Math.min(x, remain + (t - r[index]));
            }
            out.println(remain);
        }
    }
}
