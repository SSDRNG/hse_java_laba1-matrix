public class Complex {
    // x = a + b * i
    private double a;
    private double b;

    public Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public Complex(double a) {
        this(a, 0);
    }

    public Complex() {
        this(0, 0);
    }

    public double getRe() {
        return a;
    }

    public double getIm() {
        return b;
    }

    public double abs() {
        return Math.sqrt(a * a + b * b);
    }

    public Complex add(Complex other) {
        return new Complex(this.a + other.a, this.b + other.b);
    }

    public Complex sub(Complex other) {
        return new Complex(this.a - other.a, this.b - other.b);
    }

    public Complex mul(Complex other) {
        double newA = this.a * other.a - this.b * other.b;
        double newB = this.a * other.b + this.b * other.a;
        return new Complex(newA, newB);
    }

    public Complex div(Complex other) {
        double squaring = other.a * other.a + other.b * other.b;
        if (squaring == 0) {
            throw new ArithmeticException("Деление на ноль");
        }
        double newA = (this.a * other.a + this.b * other.b) / squaring;
        double newB = (this.b * other.a - this.a * other.b) / squaring;
        return new Complex(newA, newB);
    }

    public String toString() {
        if (b == 0) return String.valueOf(a);
        if (a == 0) return b + "i";
        if (b < 0) return a + " - " + (-b) + "i";
        return a + " + " + b + "i";
    }
}
