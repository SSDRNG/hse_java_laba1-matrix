import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Здравствуйте! Это мой калькулятор матриц с комплексными числами.");
        System.out.println("Доступные команды: сложить, умножить, поделить, транспонировать, определитель");
        System.out.println("Если хотите выйти - напечатайте слово выход");

        while (true) {

            System.out.println("Введите команду или выход");
            String task = sc.next().toLowerCase();

            if (task.contains("выход")) {
                break;
            }

            try {
                if (task.contains("слож")) {
                    Matrix a = Matrix.readFrom(sc, "A");
                    Matrix b = Matrix.readFrom(sc, "B");
                    System.out.println("A + B =");
                    System.out.println(a.add(b));
                } else if (task.contains("умнож") || task.contains("перемнож")) {
                    Matrix a = Matrix.readFrom(sc, "A");
                    Matrix b = Matrix.readFrom(sc, "B");
                    System.out.println("A × B =");
                    System.out.println(a.mul(b));
                } else if (task.contains("подел") || task.contains("раздел")) {
                    Matrix a = Matrix.readFrom(sc, "A");
                    Matrix b = Matrix.readFrom(sc, "B");
                    System.out.println("A / B =");
                    System.out.println(a.div(b));
                } else if (task.contains("транспон")) {
                    Matrix a = Matrix.readFrom(sc, "A");
                    System.out.println("Aᵀ =");
                    System.out.println(a.transpose());
                } else if (task.contains("определ") || task.contains("детерминант")) {
                    Matrix a = Matrix.readFrom(sc, "A");
                    System.out.println("det(A) = " + a.determinant());
                } else {
                    System.out.println("Не понял команду: " + task);
                }
            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
            System.out.println();
        }

        sc.close();
    }
}