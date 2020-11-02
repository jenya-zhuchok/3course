import java.util.ArrayList;

import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.NEGATIVE_INFINITY;

public class Dichotomy {

    private double eps = 0.00001;
    private double step = 0.1;

    private ArrayList<Double> answ;
    private ArrayList<Cut> cuts;


    Dichotomy(double[] a, double eps, double step)
    {
        this.eps = eps;
        this.step = step;
        find_distances(a);
    }

    public void print_answers(){

    }

    private void d_less_zero(double[] a)
    {
        double tmp = calc(a, 0);
        if (Math.abs(tmp) < eps) {
            answ.add(tmp);
            return;
        }
        if(tmp > 0)
            cuts.add(new Cut(0, POSITIVE_INFINITY));
        else
            cuts.add(new Cut(NEGATIVE_INFINITY, 0));
    }

    private  void d_more_zero(double d, double[] a, double[] b){
        double sqrt_d = Math.sqrt(d);
        double x1 = (-b[1] - sqrt_d) / (2 * a[0]);
        double x2 = (-b[1] + sqrt_d) / (2 * a[0]);

        double tmp1 = calc(a, x1);
        double tmp2 = calc(a, x2);

        if (tmp1 < -eps & tmp2 < -eps) // & or && ?
            cuts.add(new Cut(x2, POSITIVE_INFINITY));

        if (Math.abs(x1) < eps & x2 < -eps){
            answ.add(x1);
            cuts.add(new Cut(x2, POSITIVE_INFINITY));
        }

        if(tmp1 > eps & tmp2 < -eps){
            cuts.add(new Cut(NEGATIVE_INFINITY, x1));
            cuts.add(new Cut(x1, x2));
            cuts.add(new Cut(x2, POSITIVE_INFINITY));
        }

        if(tmp1 > eps & eps > Math.abs(tmp2)){
            answ.add(x2);
            cuts.add(new Cut(NEGATIVE_INFINITY, x1));
        }

        if(tmp1 > eps & tmp2 > eps)
            cuts.add(new Cut (NEGATIVE_INFINITY, x1));
    }

    private void find_distances(double[] a)
    {
        if(a.length != 4) return;
        double[] b = dif(a);
        double d = D(b);
        if(d < 0) d_less_zero(a);
        if(d > 0) d_more_zero(d, a, b);
        if(d == 0) answ.add((-b[1])/(2*b[0]));

    }


    private double calc(double[] a, double x){
        double req = 0;

        for(int i = 0; i < a.length; i++){
            double tmp = 1;
            for(int j = a.length - i - 1; j > 0; j--)
                tmp = tmp * x;
            req += tmp * a[i];
        }

        return req;
    }

    private double D (double[] a){ return a[1] * a[1] - 4 * a[0] * a[2]; }

    private double[] dif(double[] a){
        double[] b = new double[a.length - 1];
        for(int i = 0; i < a.length - 1; i++)
            b[i] = a[i] * (a.length - i - 1);
        return  b;
    }

}
