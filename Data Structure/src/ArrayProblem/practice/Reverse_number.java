package ArrayProblem.practice;

public class Reverse_number {

    public static int reverse(int x){
        int rev = 0;
        int rem = 0;

        while (x > 0){
            rem = x % 10;
            rev = rev * 10 + rem;
            x /= 10;
        }

        return rev;
    }

    public static void pattern(){
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public static void pattern2(int n){
        for(int i = 0; i<n; i++){
            for(int j = 0; j<=i; j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public static void pattern3(int n){
        for(int i=1; i<=n; i++){
            for(int j=1; j<=i; j++){
                System.out.print(j);
            }
            System.out.println();
        }
    }

    public static void pattern4(int n){
        for(int i = 1; i<=n; i++){
            for(int j = 1; j<=i; j++){
                System.out.print(i);
            }
            System.out.println();
        }
    }

    public static void pattern5(int n){
        for(int i = 1; i<=n; i++){
            for(int j = 1; j<=n-i; j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public static void pattern6(int n){
        for(int i = 1; i<=n; i++){
            for(int j = 1; j<n-i+1; j++){
                System.out.print(j);
            }
            System.out.println();
        }
    }

    public static void pattern7(int n){
        for(int i = 0; i<n; i++){
            for(int j = 1; j<=n-i-1; j++){
                System.out.print(" ");
            }
            for(int j = 1; j<=2*i+1; j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public static void pattern8(int n){
        for(int i = 0; i<n; i++){
            for(int j = 1; j<=i; j++){
                System.out.print(" ");
            }

            for(int j = 1; j<=n*2-(2*i +1); j++){
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println(reverse(65421));
        pattern8(6);
    }
}
