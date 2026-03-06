package ArrayProblem.practice;

import java.util.HashSet;

public class Diptesh_practice {

    // reverse a string
    public static String reverse(String str) {
        String rev = "";
        for(int i = str.length() -1; i>=0; i--){
            rev = rev + str.charAt(i);
        }
        return rev;
    }

    // reverse a number
    public static int reverse(int num){
        int rev = 0;
        int rem = 0;
        while(num > 0){
            rem = num % 10;
            rev = rev*10 + rem;
            num = num/10;
        }
        return rev;
    }

    // check prime number divisible by 1 and itself
    public static boolean checkPrime(int num){
        boolean flag = true;
        if(num <= 1){
            return false;
        }else {
            for(int i =2; i<= Math.sqrt(num); i++  ){
                if(num % i == 0){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    // find duplicate values in array
    public static void duplicate(int arr[]){
        HashSet<Integer> seen = new HashSet<>();
        HashSet<Integer> duplicate = new HashSet<>();

        for(int num : arr){
            if(!seen.add(num)){
                duplicate.add(num);
            }
        }
        System.out.println("duplicate: " + duplicate);

    }


    public static void main(String[] args) {
        System.out.println(reverse("tell"));
        System.out.println(reverse(85241));
        System.out.println(checkPrime(4));
        int[] a = {1,2,3,4,5,3,2,2};
        duplicate(a);

    }





}
