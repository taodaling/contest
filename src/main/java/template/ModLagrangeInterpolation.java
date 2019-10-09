package template;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModLagrangeInterpolation {
    private Map<Integer, Integer> points = new LinkedHashMap<>();
    private NumberTheory.Modular modular;
    private NumberTheory.Power power;

    public ModLagrangeInterpolation(NumberTheory.Modular modular) {
        this.modular = modular;
        this.power = new NumberTheory.Power(modular);
    }


    public void addPoint(int x, int y) {
        x = modular.valueOf(x);
        y = modular.valueOf(y);
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
                up = modular.mul(up, modular.plus(x, -otherPoint.getKey()));
                bottom = modular.mul(bottom, modular.plus(point.getKey(), -otherPoint.getKey()));
            }
            int addition = modular.mul(up, power.inverse(bottom));
            sum = modular.plus(sum, addition);
        }
        return sum;
    }
}