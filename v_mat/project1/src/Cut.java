public  class Cut{
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

