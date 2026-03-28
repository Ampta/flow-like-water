

@FunctionalInterface
interface A{
    void show();
}

// class B implements A{
//     public void show(){
//         System.out.println("Hello World");
//     }
// }



public class TypeInterface{
    public static void main(String[] args) {
        // one way to implement the interface is to create a class that implements the interface and override the abstract method.
        // A a = new A() {
        //     @Override
        //     public void show() {
        //         System.out.println("Hello World");
        //     }
        // };

        // another way to implement the interface is to use a lambda expression. A lambda expression is a concise way to represent an anonymous function. It can be used to implement a functional interface, which is an interface that has only one abstract method.
        A a = () -> System.out.println("Hello World");


        a.show();
    }
}




















// Types of interface 
// 1. Normal interface
// 2. Functional interface
// 3. Marker interface

// 1. Normal interface: A normal interface is an interface that can have multiple abstract methods. It can also have default and static methods. It is used to define a contract for classes that implement it.

// 2. Functional interface: A functional interface is an interface that has only one abstract method. It can have default and static methods as well. It is used to define a contract for lambda expressions and method references.

// 3. Marker interface: A marker interface is an interface that has no methods. It is used to indicate that a class has a certain property or behavior. For example, the Serializable interface is a marker interface that indicates that a class can be serialized.  

// what is serializable interface in java?The Serializable interface in Java is a marker interface that indicates that a class can be serialized. Serialization is the process of converting an object into a byte stream, which can then be saved to a file or transmitted over a network. The Serializable interface does not contain any methods; it simply serves as a marker to indicate that a class can be serialized. To make a class serializable, you need to implement the Serializable interface and ensure that all of its fields are also serializable. This allows you to easily save and restore the state of an object, or send it over a network. Here is an example of a class that implements the Serializable interface: 