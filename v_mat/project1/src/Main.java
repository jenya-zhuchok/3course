
public class Main {

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


    public static void main(String[] args) {
        double[] a = new double[]{1, 1, 1, 0};
        if (a.length != 4) return;

        print(a);
        Dichotomy method = new Dichotomy(a,0.00001, 20000);
        method.print_answers();

    }

}
