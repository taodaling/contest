package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P5___AQT_and_Modern_Art;



import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Interactor5 extends AbstractInteractor {
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int n = input.ri();
        Map<Rect, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            Rect r = read(input);
            map.put(r, map.getOrDefault(r, 0) + 1);
        }
        int L = 61000;
        int ask = 0;
        solutionInput.println(n).flush();
        while(true){
            char c = solutionOutput.rc();
            if(c == '?'){
                ask++;
                if(ask > L){
                    return Verdict.WA;
                }
                Rect query = read(solutionOutput);
                int ans = 0;
                for(Rect r : map.keySet()){
                    if(contain(query, r)){
                        ans += map.get(r);
                    }
                }
                solutionInput.println(ans).flush();
            }else{
                Map<Rect, Integer> res = new HashMap<>();
                for(int i = 0; i < n; i++){
                    Rect r = read(solutionOutput);
                    res.put(r, res.getOrDefault(r, 0) + 1);
                }
                for(Rect r : map.keySet()){
                    if(!map.get(r).equals(res.get(r))){
                        return Verdict.WA;
                    }
                }
                return Verdict.OK;
            }
        }
    }

    public Rect read(FastInput in){
        return new Rect(in.ri(), in.ri(), in.ri(), in.ri());
    }

    public boolean contain(Rect a, Rect b){
        return a.l <= b.l && a.r >= b.r && a.b <= b.b && a.t >= b.t;
    }
}

class Rect {
    int l;
    int r;
    int b;
    int t;

    public Rect(int l, int r, int b, int t) {
        this.l = l;
        this.r = r;
        this.b = b;
        this.t = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rect rect = (Rect) o;
        return l == rect.l && r == rect.r && b == rect.b && t == rect.t;
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, r, b, t);
    }
}
