
public class Main {


    // Распечатывает саму функцию
    private static void print(double[] a)
    {
        for (int i = 0; i < a.length ; i++) {
            if (a[i] == 0) continue;
            if (a[i] > 0)
                if(i != 0)  System.out.print(" + "+a[i]);
                else System.out.print(a[i]);
            else System.out.print(" - "+(-a[i]));
            if(i != a.length - 1) System.out.print(" * x^" + (a.length - i - 1));
        }
        System.out.println(" = 0");
        System.out.println("_________________________");
    }

//
    public static void main(String[] args) {
        double[] a = new double[]{1, -6, 11, -6};
        if (a.length != 4) return;

        print(a);
        Dichotomy method = new Dichotomy(a,0.00001, 1);
        method.print_answers();

    }

}
