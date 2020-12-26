public class Iter {

    private double l , h,  eps, x0;

    private double calc(double x){
        return l* Math.sin(x) / h;
    }

    private int how_many_X(){
        double len = l/h;
        int count = (int) (len / (Math.PI / 2));
        if (len < Math.PI / 2) return 0;
        count = (count + 4) /4;
        count = count * 2 - 1;
        return count;
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

        int count = how_many_X();
        System.out.println("count: " + (count * 2 + 1));

        start();
        System.out.println("x = " + this.x0);

        for( int i = 0; i < count; i++) {
        this.x0 = Math.PI/2 + (Math.PI * 2 * (i +1));
        //цthis.x0 = 2 + i;
        start() ;
        System.out.println("x = ±" + this.x0);
        }


        /*int i = start();

        System.out.println("__________________________");
        System.out.println("iters = " + i);
        System.out.println("x = " + this.x0);

        */

    }


    private void show(int i, double x){
        System.out.println("iter = " + i + " : " + x);
    }

    private int start(){
        double fx0 = calc(x0);
        int iter = 0;
        while (Math.abs(fx0 - x0) > eps){
            iter++;
            //show(iter, fx0);
            x0 = fx0;
            fx0 = calc(x0);
        }
        x0 = fx0;

        return iter;
    }


}
