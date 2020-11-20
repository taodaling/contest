package on2020_11.on2020_11_19_Codeforces___Valentines_Day_Contest_2020.G__Honeymoon;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerRange2DequeAdapter;
import template.primitve.generated.datastructure.LongBIT;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.stream.IntStream;

public class GHoneymoon {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        // max(A[l]-A[l-1],B[l]-B[l-1])+...+max(A[r]-A[l-1],B[r]-B[l-1])
        //=max(A[l]-A[l-1]-B[l]+B[l-1],0)+...+max(A[r]-A[l-1]-B[r]+B[l-1],0)+B[l]+...+B[r]-(r-l+1)B[l-1]
        //=max(A[l]-B[l],A[l-1]-B[l-1])+...+max(A[r]-B[r],A[l-1]-B[l-1])+B[l]+...+B[r]-(r-l+1)A[l-1]
        //=max(C[l],C[l-1])+...+max(C[r],C[l-1])+B[l]+...+B[r]-(r-l+1)A[l-1]
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        in.populate(a);
        in.populate(b);
        LongPreSum A = new LongPreSum(i -> a[i], n);
        LongPreSum B = new LongPreSum(i -> b[i], n);
        long[] C = IntStream.range(0, n).mapToLong(i -> A.prefix(i) - B.prefix(i)).toArray();
        LongPreSum prefB = new LongPreSum(i -> B.prefix(i), n);

        debug.debug("A", A);
        debug.debug("B", B);
        Query[] queries = new Query[q];
        for (int i = 0; i < q; i++) {
            queries[i] = new Query();
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            queries[i].l = l;
            queries[i].r = r;
            queries[i].low = A.prefix(l - 1) - B.prefix(l - 1);
            queries[i].sum = prefB.intervalSum(l, r) - (r - l + 1) * A.prefix(l - 1);
        }

        IntegerBIT occurBIT = new IntegerBIT(n);
        LongBIT sumBIT = new LongBIT(n);
        int[] indices = IntStream.range(0, n).toArray();
        CompareUtils.quickSort(indices, (i, j) -> -Long.compare(C[i], C[j]), 0, n);
        Query[] queriesSortByLow = queries.clone();
        Arrays.sort(queriesSortByLow, (x, y) -> -Long.compare(x.low, y.low));
        IntegerRange2DequeAdapter dq = new IntegerRange2DequeAdapter(i -> indices[i], 0, n - 1);
        for (Query query : queriesSortByLow) {
            while (!dq.isEmpty() && C[dq.peekFirst()] >= query.low) {
                int index = dq.removeFirst();
                occurBIT.update(index + 1, 1);
                sumBIT.update(index + 1, C[index]);
            }
            long plus = (query.r - query.l + 1 - occurBIT.query(query.l + 1, query.r + 1)) * query.low
                    + sumBIT.query(query.l + 1, query.r + 1);
            query.sum += plus;
        }

        for(Query query : queries){
            out.println(query.sum);
        }
    }
}

class Query {
    int l;
    int r;
    long low;
    long sum;
}