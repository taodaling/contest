package on2019_12.on2019_12_29_Educational_Codeforces_Round_79__Rated_for_Div__2_.B__Verse_For_Santa;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.IntegerList;

public class BVerseForSanta {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int s = in.readInt();
        IntegerList times = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            times.add(in.readInt());
        }

        int max = maxReward(times, s);
        int remove = 0;


        int maxIndex = 0;
        for (int i = 0; i <= max && i < n; i++) {
            if (times.get(i) > times.get(maxIndex)) {
                maxIndex = i;
            }
        }
        IntegerList clone = times.clone();
        clone.remove(maxIndex);
        int r = maxReward(clone, s);
        if (r > max) {
            max = r;
            remove = maxIndex + 1;
        }

        out.println(remove);
    }

    public int maxReward(IntegerList list, int s) {
        int cnt = 0;
        for (IntegerIterator iterator = list.iterator(); iterator.hasNext(); ) {
            int next = iterator.next();
            if (s >= next) {
                s -= next;
                cnt++;
            } else {
                break;
            }
        }
        return cnt;
    }
}
