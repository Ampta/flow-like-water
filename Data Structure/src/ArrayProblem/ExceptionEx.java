package ArrayProblem;

class InvalidAge extends Exception{
    InvalidAge(String messgae){
        super(messgae);
    }
}


public class ExceptionEx {

    public static void main(String[] args) throws InvalidAge {
        int age = 20;
        if(age < 20){
            throw new InvalidAge("age should be more than 20");
        }

        if(age >= 20){
            System.out.println("Your age is: " + age);
        }

    }
}
