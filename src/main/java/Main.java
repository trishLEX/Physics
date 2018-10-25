import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.lang.Math.*;

public class Main {
    private static final double m1 = 0.1;
    private static final double m2 = 0.1;
    private static final double g = 9.8;
    private static final double l1 = 0.2;
    private static final double l2 = 0.2;
    private static final double dt = 0.01;
    private static final double a1 = PI / 2;
    private static final double a2 = PI / 2;
    private static final double p1 = 0;
    private static final double p2 = 0;

    public static void main(String[] args) {
        double[] tArray = new double[101];

        double[] a1Array = new double[101];
        a1Array[0] = a1;

        double[] a2Array = new double[101];
        a2Array[0] = a2;

        double[] p1Array = new double[101];
        p1Array[0] = p1;

        double[] p2Array = new double[101];
        p2Array[0] = p2;

        //runge((t, y) -> t * sqrt(y), tArray, yArray, dt);
        BiFunction<Double, Double, Double> a1Func = (t, a) -> (p1 * l2 - p2 * l1 * cos(a - a2)) / (l1 * l1 * l2 * (m1 + m2 * sin(a - a2) * sin(a - a2)));
        BiFunction<Double, Double, Double> a2Func = (t, a) -> (p2 * (m1 + m2) * l1 - p1 * m2 * l2 * cos(a1 - a)) / (m2 * l1 * l2 * l2 * (m1 + m2 * sin(a1 - a) * sin(a1 - a)));
        BiFunction<Double, Double, Double> p1Func = (t, p) -> -(m1 + m2) * g * l1 * sin(a1) - A1(a1, a2, p, p2) + A2(a1, a2, p, p2);
        BiFunction<Double, Double, Double> p2Func = (t, p) -> - m2 * g * l2 * sin(a2) + A1(a1, a2, p1, p) - A2(a1, a2, p1, p);
//        runge(a1Func, tArray, a1Array, dt);
//        runge(a2Func, tArray, a2Array, dt);
//        runge(p1Func, tArray, p1Array, dt);
//        runge(p2Func, tArray, p2Array, dt);
        commonRunge(tArray, dt, a1Array, a2Array, p1Array, p2Array);

        for (int i = 0; i < 101; i++) {
            System.out.print(String.format("a1(t = %.2f) = %.2f   ", tArray[i], a1Array[i]));
            System.out.print(String.format("a2(t = %.2f) = %.2f   ", tArray[i], a2Array[i]));
            System.out.print(String.format("p1(t = %.2f) = %.2f   ", tArray[i], p1Array[i]));
            System.out.print(String.format("p2(t = %.2f) = %.2f\n", tArray[i], p2Array[i]));
        }

        Drawer alpha1 = new Drawer("alpha1", tArray, a1Array, "t", "alpha1");
        alpha1.draw();

        Drawer alpha2 = new Drawer("alpha2", tArray, a2Array, "t", "alpha2");
        alpha2.draw();

        Drawer p1 = new Drawer("p1", tArray, p1Array, "t", "p1");
        p1.draw();

        Drawer p2 = new Drawer("p2", tArray, p2Array, "t", "p2");
        p2.draw();

        Drawer pa1 = new Drawer("p1 and alpha1", p1Array, a1Array, "p1", "alpha1");
        pa1.draw();

        Drawer pa2 = new Drawer("p2 and alpha2", p2Array, a2Array, "p2", "alpha2");
        pa2.draw();
    }

    private static double A1(double a1, double a2, double p1, double p2) {
        return  p1 * p2 * sin(a1 - a2) / (l1 * l2 * (m1 + m2 * sin(a1 - a2) * sin(a1 - a2)));
    }

    private static double A2(double a1, double a2, double p1, double p2) {
        return (p1 * p1 * m2 * l2 * l2 - 2 * p1 * p2 * m2 * l1 * l2 * cos(a1 - a2) + p2 * p2 * (m1 + m2) * l1 * l1) * sin(2 * a1 - 2 * a2) /
                (2 * l1 * l1 * l2 * l2 * (m1 + m2 * sin(a1 - a2) * sin(a1 - a2)) * (m1 + m2 * sin(a1 - a2) * sin(a1 - a2)));
    }

    private static double a1(double a1, double a2, double p1, double p2) {
        return (p1 * l2 - p2 * l1 * cos(a1 - a2)) / (l1 * l1 * l2 * (m1 + m2 * sin(a1 - a2) * sin(a1 - a2)));
    }

    private static double a2(double a1, double a2, double p1, double p2) {
        return (p2 * (m1 + m2) * l1 - p1 * m2 * l2 * cos(a1 - a2)) / (m2 * l1 * l2 * l2 * (m1 + m2 * sin(a1 - a2) * sin(a1 - a2)));
    }

    private static double p1(double a1, double a2, double p1, double p2) {
        return -(m1 + m2) * g * l1 * sin(a1) - A1(a1, a2, p1, p2) + A2(a1, a2, p1, p2);
    }

    private static double p2(double a1, double a2, double p1, double p2) {
        return  - m2 * g * l2 * sin(a2) + A1(a1, a2, p1, p2) - A2(a1, a2, p1, p2);
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
        for (int i = 1; i < 101; i++) {
            t[i] = t[i - 1] + dt;

            double da11 = dt * a1(    a1Array[i - 1],              a2Array[i - 1], p1Array[i - 1], p2Array[i - 1]);
            double da12 = dt * a1(a1Array[i - 1] + da11 / 2.0, a2Array[i - 1], p1Array[i - 1], p2Array[i - 1]);
            double da13 = dt * a1(a1Array[i - 1] + da12 / 2.0, a2Array[i - 1], p1Array[i - 1], p2Array[i - 1]);
            double da14 = dt * a1(a1Array[i - 1] + da13,       a2Array[i - 1], p1Array[i - 1], p2Array[i - 1]);
            a1Array[i] = a1Array[i - 1] + (da11 + 2.0 * (da12 + da13) + da14) / 6.0;

            double da21 = dt * a2(a1Array[i - 1],     a2Array[i - 1],              p1Array[i - 1], p2Array[i - 1]);
            double da22 = dt * a2(a1Array[i - 1], a2Array[i - 1] + da21 / 2.0, p1Array[i - 1], p2Array[i - 1]);
            double da23 = dt * a2(a1Array[i - 1], a2Array[i - 1] + da22 / 2.0, p1Array[i - 1], p2Array[i - 1]);
            double da24 = dt * a2(a1Array[i - 1], a2Array[i - 1] + da23,       p1Array[i - 1], p2Array[i - 1]);
            a2Array[i] = a2Array[i - 1] + (da21 + 2.0 * (da22 + da23) + da24) / 6.0;

            double dp11 = dt * p1(a1Array[i - 1], a2Array[i - 1],     p1Array[i - 1],              p2Array[i - 1]);
            double dp12 = dt * p1(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1] + dp11 / 2.0, p2Array[i - 1]);
            double dp13 = dt * p1(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1] + dp12 / 2.0, p2Array[i - 1]);
            double dp14 = dt * p1(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1] + dp13,       p2Array[i - 1]);
            p1Array[i] = p1Array[i - 1] + (dp11 + 2.0 * (dp12 + dp13) + dp14) / 6.0;

            double dp21 = dt * p2(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1],     p2Array[i - 1]);
            double dp22 = dt * p2(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1], p2Array[i - 1] + dp21 / 2.0);
            double dp23 = dt * p2(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1], p2Array[i - 1] + dp22 / 2.0);
            double dp24 = dt * p2(a1Array[i - 1], a2Array[i - 1], p1Array[i - 1], p2Array[i - 1] + dp23);
            p2Array[i] = p2Array[i - 1] + (dp21 + 2.0 * (dp22 + dp23) + dp24) / 6.0;
        }
    }
}
