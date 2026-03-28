import java.util.List;
import java.util.ArrayList;

class Student  {
    private String name;
    private int age;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student [name=" + name + ", age=" + age + "]";
    }
    
}

public class MethodRef {
    public static void main(String[] args) {
        //java 8

        // Normal Senario
        List<String> names = List.of("shivam", "manish", "Ashshi");
        List<String> uNames = names.stream()
        .map(name -> name.toUpperCase())
        .toList();

        System.out.println(names + " : " + uNames);

        // Method Reference
        List<String> uNames2 = names.stream()
        .map(String::toUpperCase)
        .toList();

        System.out.println(names + " : " + uNames2);

        
        // Student example starts here
        List<Student> students = new ArrayList<>();
        // for(String name : names) {
        //     Student s = new Student(name);
        //     students.add(s);
        // }

        // using stream API
        students = names.stream()
        .map(name -> new Student(name))
        .toList();


        // students = names.stream()
        // .map(Student::new)
        // .toList();



        System.out.println(students);








        
        // Method Reference with Custom Class
        List<Person> people = List.of(new Person("Shivam"), new Person("Manish"), new Person("Ashshi"));
        List<String> pNames = people.stream()
        .map(Person::getName)
        .toList();      

        System.out.println(people + " : " + pNames);

        // Constructor Reference
        List<String> names2 = List.of("Shivam", "Manish", "Ashshi");
        List<Person> people2 = names2.stream()
        .map(Person::new)
        .toList();
        System.out.println(names2 + " : " + people2);

        // Static Method Reference
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        List<Integer> squares = numbers.stream().map(MethodRef::square).toList();
        System.out.println(numbers + " : " + squares);  


    }

    static int square(Integer num) {
        return num * num;
    }

    static class Person{
        String name;
        Person(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return "Person{name='" + name + "'}";
        }
    }
}
