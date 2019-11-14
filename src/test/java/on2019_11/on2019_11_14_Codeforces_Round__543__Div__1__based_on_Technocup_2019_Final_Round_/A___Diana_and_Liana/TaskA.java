package on2019_11.on2019_11_14_Codeforces_Round__543__Div__1__based_on_Technocup_2019_Final_Round_.A___Diana_and_Liana;



import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int k = in.readInt();
        int n = in.readInt();
        int s = in.readInt();

        if (s > k) {
            out.println(-1);
            return;
        }

        int[] data = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            data[i] = in.readInt();
        }

        Counter counter = new Counter(1000000);
        for (int i = 0; i < s; i++) {
            counter.addReq(in.readInt());
        }

        int l = -1;
        int r = -1;
        boolean valid = false;
        int tail = 0;
        int prevRemove = -1;
        int middleRemove = -1;
        for (int i = 1; i <= m; i++) {
            if (i != 1) {
                counter.delete(data[i - 1]);
            }
            while (tail + 1 <= m && !counter.enough()) {
                counter.add(data[tail + 1]);
                tail++;
            }
            if (!counter.enough()) {
                break;
            }
            l = i;
            r = tail;
            prevRemove = (l - 1) % k;
            middleRemove = 0;
            if (r - l + 1 >= k) {
                middleRemove += (r - l + 1) - k;
            } else {
                prevRemove -= Math.min((l - 1) % k, k - (r - l + 1));
            }

            if (m - (prevRemove + middleRemove) >= (long) k * n) {
                valid = true;
                break;
            }
        }

        if (!valid) {
            out.println(-1);
            return;
        }

        out.println(prevRemove + middleRemove);
        for(int i = 1; i <= prevRemove; i++){
            out.append(i).append(' ');
        }
        for(int i = l, j = 0; i <= r && j < middleRemove; i++){
            counter.delete(data[i]);
            if(!counter.enough()){
                counter.add(data[i]);
                continue;
            }
            out.append(i).append(' ');
            j++;
        }
    }
}


class Counter {
    int[] req;
    int total = 0;

    public Counter(int c) {
        req = new int[c];
    }

    public void addReq(int x) {
        req[x]++;
        if (req[x] == 1) {
            total++;
        }
    }

    public void add(int x) {
        req[x]--;
        if (req[x] == 0) {
            total--;
        }
    }

    public void delete(int x) {
        addReq(x);
    }

    public boolean enough() {
        return total == 0;
    }
}
