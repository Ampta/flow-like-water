package ArrayProblem;

// ---------- first case
//class MyIntro{
//    String name;
//    int age;
//    int salary;
//
//    MyIntro(String name, int age, int salary){
//        this.name = name;
//        this.age = age;
//        this.salary = salary;
//    }
//
//    public void myIntro(){
//        System.out.println("My name is: " + name + ", age is: " + age + " and my salary is: " + salary);
//    }
//}
//
//public class GenericEx {
//    public static void main(String[] args) {
//        MyIntro obj = new MyIntro("shivam", 20, 20000);
//        obj.myIntro();
//    }
//}


// --------------second case
class MyIntro<N, A, S>{
    N name;
    A age;
    S salary;

    MyIntro(N name, A age, S salary){
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public void myIntro(){
        System.out.println("My name is: " + name + ", age is: " + age + " and my salary is: " + salary);
    }
}

public class GenericEx {
    public static void main(String[] args) {
        MyIntro<String, Integer, String> obj = new MyIntro<>("shivam", 20, "20000");
        MyIntro<String, String, String> obj2 = new MyIntro<>("shivam", "24",  "10000");
        obj.myIntro();
        obj2.myIntro();
    }
}