import java.util.function.BiFunction;

import static java.lang.Math.*;

public class Main {
    private static final double M1 = 1.0;
    private static final double M2 = 1.0;
    private static final double G = 9.8;
    private static final double L1 = 1.0;
    private static final double L2 = 1.0;
    private static final double dT = 0.01;
    private static final double ALPHA1 = PI / 2;
    private static final double ALPHA2 = 0;
    private static final double P1 = 0;
    private static final double P2 = 0;
    private static final int SECONDS = 10;
    private static final int N = SECONDS * (int) (100 * dT * 100);

    public static void main(String[] args) {
        double[] tArray = new double[N + 1];

        double[] a1Array = new double[N + 1];
        a1Array[0] = ALPHA1;

        double[] a2Array = new double[N + 1];
        a2Array[0] = ALPHA2;

        double[] p1Array = new double[N + 1];
        p1Array[0] = P1;

        double[] p2Array = new double[N + 1];
        p2Array[0] = P2;

        commonRunge(tArray, dT, a1Array, a2Array, p1Array, p2Array);

        for (int i = 0; i < N + 1; i++) {
            System.out.print(String.format("ALPHA1(t = %.2f) = %.2f   ", tArray[i], a1Array[i]));
            System.out.print(String.format("ALPHA2(t = %.2f) = %.2f   ", tArray[i], a2Array[i]));
            System.out.print(String.format("P1(t = %.2f) = %.2f   ", tArray[i], p1Array[i]));
            System.out.print(String.format("P2(t = %.2f) = %.2f\n", tArray[i], p2Array[i]));
        }

        Drawer alpha1 = new Drawer("alpha1", tArray, a1Array, "t", "alpha1");
        alpha1.draw();

        Drawer alpha2 = new Drawer("alpha2", tArray, a2Array, "t", "alpha2");
        alpha2.draw();

        Drawer p1 = new Drawer("P1", tArray, p1Array, "t", "P1");
        p1.draw();

        Drawer p2 = new Drawer("P2", tArray, p2Array, "t", "P2");
        p2.draw();

        Drawer pa1 = new Drawer("P1 and alpha1", p1Array, a1Array, "P1", "alpha1");
        pa1.draw();

        Drawer pa2 = new Drawer("P2 and alpha2", p2Array, a2Array, "P2", "alpha2");
        pa2.draw();
    }

    private static double A1(double a1, double a2, double p1, double p2) {
        return  p1 * p2 * sin(a1 - a2) / (L1 * L2 * (M1 + M2 * sin(a1 - a2) * sin(a1 - a2)));
    }

    private static double A2(double a1, double a2, double p1, double p2) {
        return (p1 * p1 * M2 * L2 * L2 - 2 * p1 * p2 * M2 * L1 * L2 * cos(a1 - a2) + p2 * p2 * (M1 + M2) * L1 * L1) * sin(2 * a1 - 2 * a2) /
                (2 * L1 * L1 * L2 * L2 * (M1 + M2 * sin(a1 - a2) * sin(a1 - a2)) * (M1 + M2 * sin(a1 - a2) * sin(a1 - a2)));
    }

    private static double a1(double a1, double a2, double p1, double p2) {
        return (p1 * L2 - p2 * L1 * cos(a1 - a2)) / (L1 * L1 * L2 * (M1 + M2 * sin(a1 - a2) * sin(a1 - a2)));
    }

    private static double a2(double a1, double a2, double p1, double p2) {
        return (p2 * (M1 + M2) * L1 - p1 * M2 * L2 * cos(a1 - a2)) / (M2 * L1 * L2 * L2 * (M1 + M2 * sin(a1 - a2) * sin(a1 - a2)));
    }

    private static double p1(double a1, double a2, double p1, double p2) {
        return -(M1 + M2) * G * L1 * sin(a1) - A1(a1, a2, p1, p2) + A2(a1, a2, p1, p2);
    }

    private static double p2(double a1, double a2, double p1, double p2) {
        return  -M2 * G * L2 * sin(a2) + A1(a1, a2, p1, p2) - A2(a1, a2, p1, p2);
    }

    private static void runge(BiFunction<Double, Double, Double> function, double[] t,
                      double[] y, double dt)
    {
        for (int n = 0; n < t.length - 1; n++) {
            double dy1 = dt * function.apply(t[n], y[n]);
            double dy2 = dt * function.apply(t[n] + dt / 2.0, y[n] + dy1 / 2.0);
            double dy3 = dt * function.apply(t[n] + dt / 2.0, y[n] + dy2 / 2.0);
            double dy4 = dt * function.apply(t[n] + dt, y[n] + dy3);
            t[n + 1] = t[n] + dt;
            y[n + 1] = y[n] + (dy1 + 2.0 * (dy2 + dy3) + dy4) / 6.0;
        }
    }

    private static void commonRunge(
            double[] t,
            double dt,
            double[] a1Array,
            double[] a2Array,
            double[] p1Array,
            double[] p2Array
    ) {
        for (int i = 1; i < N + 1; i++) {
            t[i] = t[i - 1] + dt;

            double da11 = dt * a1(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1], p2Array[i - 1]);
            double da21 = dt * a2(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1], p2Array[i - 1]);
            double dp11 = dt * p1(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1], p2Array[i - 1]);
            double dp21 = dt * p2(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1], p2Array[i - 1]);

            double da12 = dt * a1(a1Array[i - 1] + da11 / 2.0, a2Array[i - 1] + da21 / 2.0, p1Array[i - 1] + dp11 / 2.0, p2Array[i - 1] + dp21 / 2.0);
            double da22 = dt * a2(a1Array[i - 1] + da11 / 2.0, a2Array[i - 1] + da21 / 2.0, p1Array[i - 1] + dp11 / 2.0, p2Array[i - 1] + dp21 / 2.0);
            double dp12 = dt * p1(a1Array[i - 1] + da11 / 2.0, a2Array[i - 1] + da21 / 2.0, p1Array[i - 1] + dp11 / 2.0, p2Array[i - 1] + dp21 / 2.0);
            double dp22 = dt * p2(a1Array[i - 1] + da21 / 2.0, a2Array[i - 1] + da21 / 2.0, p1Array[i - 1] + dp11 / 2.0, p2Array[i - 1] + dp21 / 2.0);

            double da13 = dt * a1(a1Array[i - 1] + da12 / 2.0, a2Array[i - 1] + da22 / 2.0, p1Array[i - 1] + dp12 / 2.0, p2Array[i - 1] + dp22 / 2.0);
            double da23 = dt * a2(a1Array[i - 1] + da12 / 2.0 ,a2Array[i - 1] + da22 / 2.0, p1Array[i - 1] + dp12 / 2.0, p2Array[i - 1] + dp22 / 2.0);
            double dp13 = dt * p1(a1Array[i - 1] + da12 / 2.0, a2Array[i - 1] + da22 / 2.0, p1Array[i - 1] + dp12 / 2.0, p2Array[i - 1] + dp22 / 2.0);
            double dp23 = dt * p2(a1Array[i - 1] + da12 / 2.0, a2Array[i - 1] + dp22 / 2.0, p1Array[i - 1] + dp12 / 2.0, p2Array[i - 1] + dp22 / 2.0);

            double da14 = dt * a1(a1Array[i - 1] + da13,       a2Array[i - 1] + da23,       p1Array[i - 1] + dp13,       p2Array[i - 1] + dp23);
            double da24 = dt * a2(a1Array[i - 1] + da13,       a2Array[i - 1] + da23,       p1Array[i - 1] + dp13,       p2Array[i - 1] + dp23);
            double dp14 = dt * p1(a1Array[i - 1] + da13,       a2Array[i - 1] + da23,       p1Array[i - 1] + dp13,       p2Array[i - 1] + dp23);
            double dp24 = dt * p2(a1Array[i - 1] + da13,       a2Array[i - 1] + da23,       p1Array[i - 1] + dp13,       p2Array[i - 1] + dp23);

            a1Array[i] = a1Array[i - 1] + (da11 + 2.0 * (da12 + da13) + da14) / 6.0;
            a2Array[i] = a2Array[i - 1] + (da21 + 2.0 * (da22 + da23) + da24) / 6.0;
            p1Array[i] = p1Array[i - 1] + (dp11 + 2.0 * (dp12 + dp13) + dp14) / 6.0;
            p2Array[i] = p2Array[i - 1] + (dp21 + 2.0 * (dp22 + dp23) + dp24) / 6.0;
        }
    }
}
