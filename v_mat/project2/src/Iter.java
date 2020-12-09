public class Iter {

    private double l , h,  eps, x0;

    private double calc(double x){
        return l* Math.sin(x) / h;
    }


    Iter(double l , double h, double  eps, double x0){

        this.l =  l;
        this.h =  h;
        this.eps = eps;
        this.x0 =  x0;

        System.out.println("l = " + l + "; h = " + h);
        System.out.println("eps = " + eps);
        System.out.println("x0 = " + x0);
        System.out.println("__________________________");

        int i = start();

        System.out.println("__________________________");
        System.out.println("iter = " + i);
        System.out.println("x = " + this.x0);

    }


    private void show(int i, double x){
        System.out.println("iter = " + i + " : " + x);
    }

    private int start(){
        double x1 = calc(x0);
        int iter = 0;
        while (Math.abs(x1 - x0) > eps){
        //    show(iter, x1);
            iter++;
            x0 = x1;
            x1 = calc(x0);
        }
        x0 = x1;

        return iter;
    }


}
