package daily;

public class Day_one {
    public static void main(String[] args) {
        int num = 8972;
        System.out.println("reverse integer: " + reverseNumber(num));

        String str = "Hello";
        System.out.println("reverse string: " + reverseString(str));

        String sen = "hello this is me where are you now";
        System.out.println("count number of words in sentence: " +wordCount(sen));

        String word = "hello the face";
        System.out.println("count number of alphabet: " + alphabetCount(word));

        int check = 9;
        System.out.println("check prime number: " + isPrime(check));

        int armNum = 370;
        System.out.println("check armstrong number: " + isArms(armNum));

    }

    public static boolean isArms(int num){
        int og = num;
        int sum = 0;
        while(num > 0){
            int rem = num%10;
            sum = sum + (rem * rem * rem);
            num/=10;
        }
        if(sum == og){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isPrime(int num){
        boolean result = true;
        if(num == 1){
            return false;
        }
        for(int i =2; i*i<=num; i++){
            if(num%i == 0){
                return false;
            }
        }
        return result;
    }

    public static int alphabetCount(String str){
        int count = 0;
        for(int i =0; i<str.length(); i++){
            if(str.charAt(i) == ' '){
                count--;
            }
            count++;
        }
        return count;
    }

    public static int wordCount(String str){
        String[] arr = str.split(" ");
        return arr.length;
    }

    public static int reverseNumber(int num) {
        int rev = 0;
        while(num>0){
            int rem = num%10;
            rev= rev*10 + rem;
            num/=10;
        }
        return rev;
    }

    public static String reverseString(String str){
        String rev = "";
        for(int i = str.length()-1; i>=0; i--){
            rev = rev + str.charAt(i);
        }
        return rev;
    }
}
