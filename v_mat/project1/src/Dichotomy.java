import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.NEGATIVE_INFINITY;

public class Dichotomy {

    private double eps = 0.00001;
    private double step = 0.1;

    private ArrayList<Double> answs;
    private ArrayList<Cut> cuts;


    Dichotomy(double[] a, double eps, double step)
    {
        this.eps = eps;
        this.step = step;

        answs = new ArrayList<Double>();
        cuts = new ArrayList<Cut>();

        find_distances(a);
        find_answs(a);
    }

    public void print_answers(){
        for(double ans: answs)
            System.out.println("x = " + ans);
    }

    private void d_less_zero(double[] a)
    {
        double tmp = calc(a, 0);
        if (Math.abs(tmp) < eps) {
            answs.add(tmp);
            return;
        }
        ArrayList<Cut> cutstmp = new ArrayList<Cut>();
        if(tmp > 0)
            cutstmp.add(new Cut(0, POSITIVE_INFINITY));
        else
            cutstmp.add(new Cut(NEGATIVE_INFINITY, 0));

        localdist(cutstmp);

    }

    private void localdist(ArrayList<Cut> infcuts){
        for(Cut c: infcuts){
            double tmp = 0.0;
            if(c.getMax() == POSITIVE_INFINITY)
                for(double i = 0; i < POSITIVE_INFINITY - step; i+=step){
                    cuts.add(new Cut(i, i + step));
                }
            if(c.getMin() == NEGATIVE_INFINITY)
                for(double i = 0; i > NEGATIVE_INFINITY + step; i-=step){
                    cuts.add(new Cut(i, i - step));

                }
        }

    }

    private  void d_more_zero(double d, double[] a, double[] b){
        double sqrt_d = Math.sqrt(d);
        double x1 = (-b[1] - sqrt_d) / (2 * a[0]);
        double x2 = (-b[1] + sqrt_d) / (2 * a[0]);

        double tmp1 = calc(a, x1);
        double tmp2 = calc(a, x2);

        ArrayList<Cut> cutstmp = new ArrayList<Cut>();

        //if(Math.abs(x1) < eps) answs.add(x1);
        //if(Math.abs(x2) < eps) answs.add(x2);

        if (tmp1 < -eps && tmp2 < -eps) // & or && ?
            cutstmp.add(new Cut(x2, POSITIVE_INFINITY));

        if (Math.abs(x1) < eps && x2 < -eps){
            answs.add(x1);
            cutstmp.add(new Cut(x2, POSITIVE_INFINITY));
        }

        if(tmp1 > eps && tmp2 < -eps){
            cutstmp.add(new Cut(NEGATIVE_INFINITY, x1));
            cuts.add(new Cut(x1, x2));
            cutstmp.add(new Cut(x2, POSITIVE_INFINITY));
        }

        if(tmp1 > eps && eps > Math.abs(tmp2)){
            answs.add(x2);
            cutstmp.add(new Cut(NEGATIVE_INFINITY, x1));
        }

        if(tmp1 > eps && tmp2 > eps)
            cutstmp.add(new Cut (NEGATIVE_INFINITY, x1));

        localdist(cutstmp);
    }

    private void find_distances(double[] a) {
        double[] b = dif(a);
        double d = D(b);
        if(d < 0) d_less_zero(a);
        if(d > 0) d_more_zero(d, a, b);
        if(d == 0)
            if(b[0] == 0.0)
                answs.add(0.0);
            else
                answs.add((-b[1])/(2*b[0]));

    }

    private void find_answs(double[] a){
        for(Cut cut : cuts)
            while(cut.dist() < eps)
            {
                double tmp = calc(a,cut.midle());

                if(Math.abs(tmp) < eps){
                    answs.add(cut.midle());
                    break;
                }

                if(tmp > 0){
                    cut.setB(cut.getMax());
                    cut.setA(cut.midle());
                }

                if(tmp < 0){
                    cut.setA(cut.getMin());
                    cut.setB(cut.midle());
                }

            }
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
