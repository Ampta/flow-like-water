package ArrayProblem.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Basic_math {

    public static Integer reverse(int number){
        Integer rev = 0;
        Integer rem = 0;

        while(number>0){
            rem = number%10;
            rev = rev*10 + rem;
            number = number/10;
        }
        return rev;
    }

    // number of digits
    public static Integer counter(int number){
        Integer counter = 0;
        while(number>0){
            number = number/10;
            counter = counter+1;

        }

        return counter;
    }


    /// 121 == 121
    public static boolean paledrome(int number){
        int revNum = 0;
        int dump = number;
        while(number>0){
            int rem = number%10;
            revNum = revNum*10 + rem;
            number = number/10;
        }
        System.out.println("Reversed number is "+revNum);
        if(revNum==dump){
            return true;
        }
        else {
            return false;
        }
    }


    // 371 == 3*3*3 + 7*7*7 + 1*1*1
    public static boolean armstrong(int number){
        int sum = 0;
        int dump = number;
        while(number>0){
            int rem = number%10;
            sum = sum + (rem*rem*rem);
            number = number/10;
        }
        if(sum==dump){
            return true;
        }else  {
            return false;
        }
    }

    public static List<Integer> divisions(int num){

        List<Integer> list = new ArrayList<>();

        for(int i = 1; i<=num; i++){
            if(num%i==0){
                list.add(i);
            }
        }
        return list;
    }

    public static List<Integer> divisionsNew(int num){

        List<Integer> list = new ArrayList<>();

        for(int i = 1; i*i<=num; i++){
            if(num%i==0){
                list.add(i);
                if((num/i) != i){
                    list.add(num/i);
                }
            }
        }
        return list;
    }


    public static void main(String[] args) {
        System.out.println(reverse(2345));
        System.out.println(counter(2345));
        System.out.println(paledrome(1211));
        System.out.println(armstrong(371));
        System.out.println(divisions(18));
        System.out.println(divisionsNew(18));


    }
}
