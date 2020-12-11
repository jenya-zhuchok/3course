public class Integral {

    enum func {sin, exp};

    private double a, b;
    private int n;
    private func f;


    private double calc(double x){
        if(f == func.exp) return Math.exp(x);
        else return Math.sin(x);
    }

    private double calcF(){
        if(f == func.exp) return Math.exp(b) - Math.exp(a);
        else return -(Math.cos(b) - Math.cos(a));
    }


    private double inaccuracy(){
        if(f == func.exp) return Math.exp(b)* Math.pow(b - a, 3) / (12 * Math.pow(n, 2));
        else return Math.abs(Math.sin(Math.PI/2))/ (12 * Math.pow(n, 2));
    }

    private double trapeze()
    {
        double h = (a + b) / (n * 3);
        double res = 0;
        double x0 = a;
        double x1= a + h;

        while (x0 < b){
            res += h * (calc(x0) + calc(x1)) / 2;
            x0 = x1;
            x1 += h;
        }

        return res;
    }

    Integral(func f, int n){
        this.f = f;
        if(f == func.exp){
            this.a = 0;
            this.b = 2;
        }
        else {
            this.a = 0;
            this.b = Math.PI;
        }
        this.n = n;

        double must = calcF();
        double res = trapeze();
        double inac = inaccuracy();
        double diff = Math.abs(res - must);

        double m = must - inac;

        System.out.println("Must be: " + must);
        System.out.println("Result: " + res);
        System.out.println("Inaccuracy: " + inac);
        System.out.println("Difference: " + diff);

        System.out.println("??: " +Math.abs(diff - inac));


    }

}
