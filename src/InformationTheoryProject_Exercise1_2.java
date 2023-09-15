import java.util.Scanner;

public class InformationTheoryProject_Exercise1_2 {
    public static double log2(double digit) {
        return Math.log(digit) / Math.log(2);
    }

    public static double calculateHX(double[][] matrix) {
        int rows = matrix.length;
        int col = matrix[0].length;
        double entropyX = 0;
        for (int j = 0; j < col; j++) {
            double colSum = 0;
            for (int i = 0; i < rows; i++) {
                colSum += matrix[i][j];
            }
            entropyX += colSum * log2(colSum);
        }
        return entropyX * (-1);
    }

    public static double calculateHY(double[][] matrix) { //HY thì thuận for i j ->> matrix[i][j] HX thì for [i][j] matrix [j][i]
        int rows = matrix.length;
        int col = matrix[0].length;
        double entropyY = 0;
        for (int i = 0; i < rows; i++) {
            double rowSum = 0;
            for (int j = 0; j < col; j++) {
                rowSum += matrix[i][j];
            }
            entropyY += rowSum * log2(rowSum);
        }
        return entropyY * (-1);
    }

    public static double calculateX_Given_Y(double[][] matrix) {
        int rows = matrix.length;
        int col = matrix[0].length;
        double H_XGivenY = 0;
        for (int i = 0; i < rows; i++) {
            double rowSum = 0;
            for (int j = 0; j < col; j++) {
                rowSum += matrix[i][j];
            }
            //Dùng công thức P(X,Y) = P(Y) * P(X|Y)
            double temp = 0;
            double ProbabilityOfXGivenY;
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] > 0) {
                    ProbabilityOfXGivenY = matrix[i][j] / rowSum; //P(X|Y) = P(X,Y) / P(Y) (PY = rowSum)
                    temp -= (ProbabilityOfXGivenY) * log2(ProbabilityOfXGivenY);
                    //Cả biểu thức này bằng: temp = P(X|Y) * log2(P_(X|Y))
                    //Suy ra để tính H(X|Y) cần nhân thêm P(Y) = rowSum
                }
            }
            H_XGivenY += rowSum * temp; //H(X|Y) = P(Y) * P(X|Y) * log2(P_(X|Y)) = P(X,Y) * log2(P_(X|Y))
        }
        return H_XGivenY;
    }

    public static double calculateY_Given_X(double[][] matrix) {
        int rows = matrix.length;
        int col = matrix[0].length;
        double H_YGivenX = 0;
        for (int j = 0; j < col; j++) {
            double colSum = 0;
            for (int i = 0; i < rows; i++) {
                colSum += matrix[i][j];
            }
            //Dùng công thức P(X,Y) = P(Y) * P(X|Y)
            double temp = 0;
            double ProbabilityOfYGivenX;
            for (int i = 0; i < rows; i++) {
                if (matrix[i][j] > 0) {
                    ProbabilityOfYGivenX = matrix[i][j] / colSum;
                    //P(Y|X) = P(X,Y) / P(X) (PX = colSum), P(X,Y) xét theo cột xuống px1y1 xuống px1y2
                    temp -= (ProbabilityOfYGivenX) * log2(ProbabilityOfYGivenX);
                    //Cả biểu thức này bằng: temp = P(Y|X) * log2(P_(Y|X))
                    //Suy ra để tính H(Y|X) cần nhân thêm P(X) = colSum
                }
            }
            H_YGivenX += colSum * temp; //H(Y|X) = P(X) * P(Y|X) * log2(P_(Y|X)) = P(X,Y) * log2(P_(Y|X))
        }
        return H_YGivenX;
    }

    public static double calculateY_Given_X2(double[][] matrix) {
        return calculateHY(matrix) - calculateHX(matrix) + calculateX_Given_Y(matrix);
        //H(Y|X) = H(Y) - H(X) + H(X|Y)
    }

    public static double calculateJointEntroPy(double[][] matrix) {
        int rows = matrix.length;
        int col = matrix[0].length;
        double jointEntropy = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] > 0) {
                    jointEntropy += matrix[i][j] * log2(matrix[i][j]);
                }
            }
        }
        return jointEntropy * -1;
    }

    public static double calculateHY_Minus_HYGivienX(double[][] matrix) {
        return calculateHY(matrix) - calculateY_Given_X(matrix);
    }

    public static double calculateMutualInformation(double[][] matrix) {
        //I(X,Y) = H(Y) - H(Y|X) = H(X) - H(X|Y)
//        return calculateHY(matrix) - calculateY_Given_X(matrix);
        return calculateHX(matrix) - calculateX_Given_Y(matrix);
    }

    //Câu C

    public static double calculateD_PX_PY(double[][] matrix) {
        int rows = matrix.length;
        int col = matrix[0].length;
        double relativeEntropyDXY = 0;
//        D(Px/Py) = Px * Log2(Px/Py)
        for (int i = 0; i < rows; i++) {
            double rowSum = 0;
            double colSum = 0;
            for (int j = 0; j < col; j++) {
                rowSum += matrix[i][j]; // Tính P(Y)
                colSum += matrix[j][i]; // Tính P(X)
            }
            relativeEntropyDXY += colSum * log2(colSum / rowSum);
        }
        return relativeEntropyDXY;
    }

    public static double calculateD_PY_PX(double[][] matrix) {
        int rows = matrix.length;
        int col = matrix[0].length;
        double relativeEntropyDYX = 0;
//        D(Py/Px) = Py * Log2(Py/Px)
        for (int i = 0; i < rows; i++) {
            double rowSum = 0;
            double colSum = 0;
            for (int j = 0; j < col; j++) {
                rowSum += matrix[i][j]; // Tính P(Y)
                colSum += matrix[j][i]; // Tính P(X)
            }
            relativeEntropyDYX += rowSum * log2(rowSum / colSum);
        }
        return relativeEntropyDYX;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Nhập kích thước ma trận
        System.out.print("Nhập số hàng M: ");
        int M = Integer.parseInt(scanner.nextLine());
        System.out.print("Nhập số cột N: ");
        int N = Integer.parseInt(scanner.nextLine());
        // Tạo ma trận xác suất
        double[][] matrix = new double[M][N];
        // Nhập ma trận xác suất từ bàn phím
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                do {
                    System.out.printf("Mời nhập giá trị P[%d][%d]: ", i + 1, j + 1);
                    String inputString = scanner.nextLine();
                    if (inputString.contains("/")) {
                        String[] arr = inputString.split("/");
                        Double tuSo = Double.parseDouble(arr[0]);
                        Double mauSo = Double.parseDouble(arr[1]);
                        matrix[i][j] = tuSo / mauSo;
                    } else {
                        matrix[i][j] = Double.parseDouble(inputString);
                    }
                } while (matrix[i][j] < 0);
            }
        }

        //Vẽ ma trận
        System.out.println("Ma trận đã nhập:");
        System.out.printf("p(X,Y)\t  ");
        for (int i = 0; i < M; i++) {
            System.out.printf("X%d\t  ", (i + 1));
        }
        System.out.println();
        for (int i = 0; i < M; i++) {
            System.out.print("  Y" + (i + 1));
            System.out.print("\t");
            for (int j = 0; j < N; j++) {
                System.out.printf("%.4f", matrix[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Câu B:");
        System.out.println("H(X): " + calculateHX(matrix));
        System.out.println("H(Y): " + calculateHY(matrix));
        System.out.println("H(X|Y): " + calculateX_Given_Y(matrix));
        System.out.println("H(Y|X): " + calculateY_Given_X(matrix));
        System.out.println("H(Y|X) solution 2: " + calculateY_Given_X2(matrix));
        System.out.println("H(X,Y): " + calculateJointEntroPy(matrix));
        System.out.println("H(Y) - H(Y|X): " + calculateHY_Minus_HYGivienX(matrix));
        System.out.println("I(X,Y): " + calculateMutualInformation(matrix));
        System.out.println();
        System.out.println("Câu C:");
        System.out.println("D(Px || Py): " + calculateD_PX_PY(matrix));
        System.out.println("D(Py || Px): " + calculateD_PY_PX(matrix));
    }
}