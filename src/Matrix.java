import java.util.Scanner;

public class Matrix {
    private Complex[][] elements;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.elements = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.elements[i][j] = new Complex(0, 0);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(elements[i][j]).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static Matrix readFrom(Scanner sc, String name) {
        System.out.println("Введите матрицу " + name + ":");

        System.out.print("Строк: ");
        int r = sc.nextInt();

        System.out.print("Столбцов: ");
        int c = sc.nextInt();

        Matrix m = new Matrix(r, c);

        System.out.println("Введите " + (r * c) + " элементов.");
        System.out.println("Пример: чтобы ввести число 3+4i, введите: 3 4");

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print("[" + i + "][" + j + "]: ");
                double re = sc.nextDouble();
                double im = sc.nextDouble();
                m.elements[i][j] = new Complex(re, im);
            }
        }

        return m;
    }

    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Размеры матриц не совпадают. Сложение невозможно.");
        }

        Matrix result = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.elements[i][j] = this.elements[i][j].add(other.elements[i][j]);
            }
        }

        return result;
    }

    public Matrix sub(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Размеры матриц не совпадают. Вычитание невозможно.");
        }

        Matrix result = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.elements[i][j] = this.elements[i][j].sub(other.elements[i][j]);
            }
        }

        return result;
    }

    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.elements[j][i] = this.elements[i][j];
            }
        }

        return result;
    }

    public Matrix mul(Matrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Ошибка. Нельзя умножить.");
        }

        Matrix result = new Matrix(this.rows, other.cols);

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex sum = new Complex();
                for (int t = 0; t < this.cols; t++) {
                    sum = sum.add(this.elements[i][t].mul(other.elements[t][j]));
                }
                result.elements[i][j] = sum;
            }
        }

        return result;
    }

    public Complex determinant() {
        if (this.rows != this.cols) {
            throw new IllegalArgumentException("Только для квадратных");
        }
        return det(this);
    }

    private static Complex det(Matrix m) {
        int n = m.rows;

        if (n == 1) {
            return m.elements[0][0];
        }
        if (n == 2) {
            return m.elements[0][0].mul(m.elements[1][1])
                    .sub(m.elements[0][1].mul(m.elements[1][0]));
        }

        Complex sum = new Complex(0, 0);
        for (int j = 0; j < n; j++) {
            Matrix minor = minor(m, 0, j);
            Complex term = m.elements[0][j].mul(det(minor));
            if (j % 2 == 0) {
                sum = sum.add(term);
            } else {
                sum = sum.sub(term);
            }
        }
        return sum;
    }

    private static Matrix minor(Matrix m, int row, int col) {
        int n = m.rows;
        Matrix result = new Matrix(n - 1, n - 1);
        int ri = 0;
        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            int rj = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                result.elements[ri][rj] = m.elements[i][j];
                rj++;
            }
            ri++;
        }
        return result;
    }

    public Matrix inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException("Только для квадратных");
        }
        int n = rows;

        if (determinant().abs() < 1e-9) {
            throw new ArithmeticException("Матрица вырождена");
        }

        Matrix aug = new Matrix(n, 2 * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aug.elements[i][j] = elements[i][j];
            }
            aug.elements[i][i + n] = new Complex(1);
        }

        for (int i = 0; i < n; i++) {
            int base = -1;
            for (int k = i; k < n; k++) {
                if (aug.elements[k][i].abs() > 1e-9) {
                    base = k;
                    break;
                }
            }
            if (base == -1) throw new ArithmeticException("Вырождена");

            if (base != i) {
                Complex[] tmp = aug.elements[i];
                aug.elements[i] = aug.elements[base];
                aug.elements[base] = tmp;
            }

            Complex p = aug.elements[i][i];
            for (int j = 0; j < 2 * n; j++) {
                aug.elements[i][j] = aug.elements[i][j].div(p);
            }

            for (int k = 0; k < n; k++) {
                if (k == i) continue;
                Complex factor = aug.elements[k][i];
                for (int j = 0; j < 2 * n; j++) {
                    aug.elements[k][j] = aug.elements[k][j]
                            .sub(factor.mul(aug.elements[i][j]));
                }
            }
        }

        Matrix result = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.elements[i][j] = aug.elements[i][j + n];
            }
        }
        return result;
    }

    public Matrix div(Matrix other) {
        return this.mul(other.inverse());
    }
}
