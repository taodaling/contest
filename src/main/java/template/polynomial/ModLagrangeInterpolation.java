package template.polynomial;

import template.math.DigitUtils;
import template.math.Power;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModLagrangeInterpolation {
    private Map<Integer, Integer> points = new LinkedHashMap<>();
    private int mod;
    private Power power;

    public ModLagrangeInterpolation(int mod) {
        this.mod = mod;
        this.power = new Power(mod);
    }


    public void addPoint(int x, int y) {
        x = DigitUtils.mod(x, mod);
        y = DigitUtils.mod(y, mod);
        if (points.containsKey(x)) {
            int val = points.get(x);
            if (val != y) {
                throw new IllegalStateException();
            }
            return;
        }
        points.put(x, y);
    }

    public int interpolate(int x) {
        int sum = 0;
        for (Map.Entry<Integer, Integer> point : points.entrySet()) {
            int up = point.getValue();
            int bottom = 1;
            for (Map.Entry<Integer, Integer> otherPoint : points.entrySet()) {
                if (otherPoint.getKey().intValue() == point.getKey()) {
                    continue;
                }
                up = DigitUtils.mod((long) up * (x - otherPoint.getKey()), mod);
                bottom = DigitUtils.mod((long) bottom * (point.getKey() - otherPoint.getKey()), mod);
            }
            int addition = (int) ((long)up * power.inverse(bottom) % mod);
            sum = DigitUtils.modplus(sum, addition, mod);
        }
        return sum;
    }
}