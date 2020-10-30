public class Main {

    private static class Cut{
        private double a;
        private double b;

        Cut(double a, double b){
            this.a = a;
            this.b = b;
        }

        public void setA(double a) { this.a = a; }
        public void setB(double b) { this.b = b; }

        public double getA() { return a; }
        public double getB() { return b; }

        public double getMax() { return Math.max(a, b);}
        public double getMin() { return Math.min(a, b);}

        public double midle(){return (a + b)/2; }
    }



    private static double[] jsa(double[] a){
        double[] tmp;

        if(a.length > 2)  tmp = jsa(dif(a));
        else return new double[]{(-a[1] / a[0])};



        return new double[] {5, 6 , 4};
    }

    private static void print(double[] a)
    {
       // System.out.println("(" + a[0] + "*x^3) + (" + a[1] + "*x^2) + (" + a[2] + "*x) + (" + a[3] + ") = 0");
        for (int i = 0; i < a.length ; i++) {
            if (a[i] == 0) continue;
            if (a[i] > 0)
                if(i != 0)  System.out.print(" + "+a[i]);
                else System.out.print(a[i]);
            else System.out.print(" - "+(-a[i]));
            if(i != a.length - 1) System.out.print(" * x^" + (a.length - i - 1));
        }
        System.out.println(" = 0");
    }

    private static double calc(double[] a, double x){
        double req = 0;

        for(int i = 0; i < a.length; i++){
            double tmp = 1;
            for(int j = a.length - i - 1; j > 0; j--)
                tmp = tmp * x;
            req += tmp * a[i];
        }

        return req;
    }

    private static double D (double a, double b, double c){ return b * b - 4 * a * c; }
    private static double D (double[] a){ return a[1] * a[1] - 4 * a[0] * a[2]; }

    private static double[] dif(double[] a){
        double[] b = new double[a.length - 1];
        for(int i = 0; i < a.length - 1; i++)
            b[i] = a[i] * (a.length - i - 1);
        return  b;
    }


    public static void main(String[] args){
        double[] a = new double[]{ 4, -2, 0, 0};
        Cut cut = new Cut(-1,1);

        jsa(a);

 /*       print(a);

        if(a[0] != 0)
            for(int i = a.length - 1; i >= 0; i--)
                a[i] = a[i]/a[0];

        print(a);

        if(D(a) < 0) {
            System.out.println("Корней нет");
            return;
        }
        if(D(a) == 0)
            System.out.println("Корень 1");

        if(D(a) > 0)
            System.out.println("Корня 2");
*/


        while(true) {
            double tmp = calc(a, cut.midle());
            if (tmp == 0) break;
            if (tmp > 0) {
                cut.setB(cut.getMax());
                cut.setA(cut.midle());
            } else {
                cut.setA(cut.getMin());
                cut.setB(cut.midle());
            }
        }

        System.out.println(cut.midle());



    }

}
