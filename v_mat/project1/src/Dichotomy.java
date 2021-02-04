import java.util.ArrayList;

import static java.lang.Double.*;

public class Dichotomy {

    private double eps = 0.00001;
    private double step = 0.1;

    private ArrayList<Double> answs;
    private ArrayList<Cut> cuts;
    private ArrayList<Cut> answ_cuts;


    Dichotomy(double[] a, double eps, double step)
    {
        this.eps = eps;
        this.step = step;

        answs = new ArrayList<Double>();
        cuts = new ArrayList<Cut>();
        answ_cuts = new ArrayList<Cut>();

        find_distances(a);
        find_answs(a);
    }

    public void print_answers(){

        System.out.println("_________________________\nAnswers:");
        for(double ans: answs)
            System.out.println("x = " + ans);
        for(Cut cut: answ_cuts)
            System.out.println("x ∈ [ " + cut.getMin() + " ; " + cut.getMax() + "]");
    }

    private void d_less_zero(double[] a)
    {
        double tmp = calc(a, 0);

        System.out.println("f(0) = " + tmp);

        if (Math.abs(tmp) < eps) {
            answs.add(tmp);
            return;
        }
        if(tmp > 0)
            cuts.add(new Cut(NEGATIVE_INFINITY, 0));
        else
            cuts.add(new Cut(0, POSITIVE_INFINITY));
    }

    private  void d_more_zero(double d, double[] a, double[] b){
        double sqrt_d = Math.sqrt(d);
        double x1 = (-b[1] - sqrt_d) / (2 * b[0]);
        double x2 = (-b[1] + sqrt_d) / (2 * b[0]);

        System.out.println("x1 = " + x1);
        System.out.println("x2 = " + x2);

        double tmp1 = calc(a, x1);
        double tmp2 = calc(a, x2);

        System.out.println("f(x1) = " + tmp1);
        System.out.println("f(x2) = " + tmp2);


        if (tmp1 < -eps && tmp2 < -eps)
            cuts.add(new Cut(x2, POSITIVE_INFINITY));

        if (Math.abs(x1) < eps && x2 < -eps){
            answs.add(x1);
            cuts.add(new Cut(x2, POSITIVE_INFINITY));
        }

        if(tmp1 > eps && tmp2 < -eps){
            cuts.add(new Cut(NEGATIVE_INFINITY, x1));
            cuts.add(new Cut(x1, x2));
            cuts.add(new Cut(x2, POSITIVE_INFINITY));
        }

        if(tmp1 > eps && eps > Math.abs(tmp2)){
            answs.add(x2);
            cuts.add(new Cut(NEGATIVE_INFINITY, x1));
        }

        if(tmp1 > eps && tmp2 > eps)
            cuts.add(new Cut (NEGATIVE_INFINITY, x1));

    }

    // Ищет отрезки
    private void find_distances(double[] a) {
        double[] b = dif(a);
        double d = D(b);
        System.out.println("D = " + d);
        if(d < 0) d_less_zero(a);
        if(d > 0) d_more_zero(d, a, b);
        if(d == 0)
            if(b[0] == 0.0)
                answs.add(0.0);
            else
                answs.add((-b[1])/(2*b[0]));

        System.out.println("_________________________\nCuts:");
        for(Cut cut: cuts)
            System.out.println("x ∈ [ " + cut.getMin() + " ; " + cut.getMax() + "]");
    }

    // Ищет точки в полученных ЛОКАЛЬНЫХ отрезках
    private boolean locale(double[] a, Cut c) {
       // if(calc(a, c.getB()) * calc(a, c.getA()) > 0) return false;
        while (c.dist() > eps) {
            double tmp = calc(a, c.midle());

            if (tmp < 0) {
                c.setB(c.getMax());
                c.setA(c.midle());
            }

            if (tmp > 0) {
                c.setA(c.getMin());
                c.setB(c.midle());
            }

            if (Math.abs(tmp) < eps) {
                answs.add(c.midle());
                return true;
            }
        }
        return false;
    }


    // Ищет точки в полученных отрезках или разбивает на более мелкие отрезки
    private void find_answs(double[] a){
        for (Cut cut : cuts) {
            Cut c;
            if (cut.getMin() == NEGATIVE_INFINITY || cut.getMax() == POSITIVE_INFINITY) {
                int iter = 10000000;
                boolean check = false;
                for (int i = 0; i < iter; i++) {
                    if (cut.getMax() == POSITIVE_INFINITY)
                        c = new Cut(cut.getMin() + step * i, cut.getMin() + step * (i + 1));
                    else
                        c = new Cut(cut.getMax() - step * (i + 1), cut.getMax() - step * i);
                    if (locale(a, c) == true){
                        check = true;
                        break;
                    }
                }
                if (check == false)
                    answ_cuts.add(cut);
            } else {
                if (locale(a, cut) == false)
                    answ_cuts.add(cut);
            }
        }
    }


    // Считает значение функции в точке
    private double calc(double[] a, double x){
        double req = 0;

        for (int i = 0; i < a.length; i++)
            req += a[i] * Math.pow(x, a.length - i -1);

        return req;
    }

    //Дискременант
    private double D (double[] a){ return a[1] * a[1] - 4 * a[0] * a[2]; }

    // Производная
    private double[] dif(double[] a){
        double[] b = new double[a.length - 1];
        for(int i = 0; i < a.length - 1; i++)
            b[i] = a[i] * (a.length - i - 1);
        return  b;
    }

}
