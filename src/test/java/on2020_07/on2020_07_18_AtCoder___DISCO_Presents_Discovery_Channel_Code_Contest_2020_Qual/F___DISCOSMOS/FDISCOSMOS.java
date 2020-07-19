package on2020_07.on2020_07_18_AtCoder___DISCO_Presents_Discovery_Channel_Code_Contest_2020_Qual.F___DISCOSMOS;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.Modular;
import template.math.Power;

public class FDISCOSMOS {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();
        int t = in.readInt();

        int hh = h / GCDs.gcd(h, t);
        int ww = w / GCDs.gcd(w, t);


        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);
        int way = 1;
        way = mod.plus(way, mod.subtract(pow.pow(2, hh), 2));
        way = mod.plus(way, mod.subtract(pow.pow(2, ww), 2));
        way = mod.plus(way, pow.pow(2, GCDs.gcd(hh, ww)));


        Modular powMod = mod.getModularForPowerComputation();
        int p = powMod.mul(h / hh, w / ww);
        way = pow.pow(way, p);
        out.println(way);
    }
}
