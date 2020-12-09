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

    private double trapeze()
    {
        double h = (a + b) / n;
        double rez = 0;
        double x0 = a;
        double x1= a + h;

        while (x0 < b){
            rez += h * (calc(x0) + calc(x1)) / 2;
            x0 = x1;
            x1 += h;
        }

        return rez;
    }

    Integral(int i, int n){
        if(i == 1){
            this.a = 0;
            this.b = 2;
            f = func.exp;
        }
        else {
            this.a = 0;
            this.b = Math.PI;
            f = func.sin;
        }
        this.n = n;

        System.out.println("Must be: " + calcF());
        System.out.println("Result: " + trapeze());

    }

}
