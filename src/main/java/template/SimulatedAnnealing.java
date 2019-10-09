package template;

import java.util.Random;

public abstract class SimulatedAnnealing<S> {
    public SimulatedAnnealing(double threshold, double k, double reduce) {
        this.threshold = threshold;
        this.k = k;
        this.reduce = reduce;
    }

    public abstract S next(S old, double temperature);

    public abstract double eval(S status);

    public void abandon(S old) {
    }

    public void optimize(double temperature, S init) {
        S now = init;
        double weight = eval(now);
        double t = temperature;
        while (t > threshold) {
            S next = next(now, t);
            double nextWeight = eval(next);
            if (nextWeight > weight || random.nextDouble() < Math.exp((nextWeight - weight) / (k * t))) {
                abandon(now);
                now = next;
                weight = nextWeight;
            }
            t *= reduce;
        }

        if (best == null || bestWeight < weight) {
            best = now;
            bestWeight = weight;
        }
    }

    protected int nextInt(int l, int r) {
        return random.nextInt(r - l + 1) + l;
    }

    protected double nextDouble(double l, double r) {
        return (r - l) * random.nextDouble() + l;
    }

    public S getBest() {
        return best;
    }

    private S best;
    private double bestWeight;
    private Random random = new Random(12345678);
    private double threshold;
    /**
     * The larger k is, the more possible to challenge
     */
    private double k;
    /**
     * The smaller reduce is, the fast to reduce temperature
     */
    private double reduce;
}