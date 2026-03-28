import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


class Student{
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String toString() {
        return "Student{name='" + name + "', age=" + age + "}";
    }
}

public class TypeCollection{
    public static void main(String[] args) {
        System.out.println("------------------");
        Set<Integer> nums = new TreeSet<>();
        nums.add(16);
        nums.add(12);
        nums.add(93);
        nums.add(29);

        Iterator<Integer> val = nums.iterator();
        while(val.hasNext()){
            System.out.println(val.next());
        }

        System.out.println("------------------");
        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 30);   
        map.put("Bob", 25);
        map.put("Charlie", 35);

        for(String key : map.keySet()){
            System.out.println(key + ": " + map.get(key));
        }

        System.out.println("------------------");
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(7);
        list.add(190);
        list.add(18);

        Collections.sort(list);
        for(Integer num : list){
            System.out.println(num);
        }

        System.out.println("------------------");

        Comparator<Student> com = new Comparator<Student>() {
            public int compare(Student s1, Student s2) {
                return s1.getAge() - s2.getAge();
            }
        };

        List<Student> students = new ArrayList<>();
        students.add(new Student("Alice", 20));
        students.add(new Student("Bob", 22));
        students.add(new Student("Charlie", 19));

        // Collections.sort(students, (s1, s2) -> s1.getAge() - s2.getAge());

        Collections.sort(students, com);

        for(Student student : students){
            System.out.println(student);
        }
    }

}






// Collection API, Collection, Collections, Comparator, Comparable, Arrays, ArrayList, LinkedList, HashSet, TreeSet, HashMap, TreeMap

// 1. Collection API: A framework that provides a set of interfaces and classes for working with groups of objects. It includes interfaces like Collection, List, Set, and Map, and classes like ArrayList, LinkedList, HashSet, TreeSet, HashMap, and TreeMap.

// 2. Collection: The root interface in the Collection API that represents a group of objects. It provides basic methods for adding, removing, and querying elements in the collection.

// 3. Collections: A utility class that provides static methods for working with collections, such as sorting, searching, and shuffling.

// Array vs ArrayList: An array is a fixed-size data structure that can hold a specific number of elements, while an ArrayList is a resizable array that can grow or shrink dynamically as elements are added or removed. Arrays are more efficient for storing and accessing primitive types, while ArrayLists are more flexible and easier to use for storing objects.    

// Iterable -> Collection -> Interface List, queue, set, map
// List -> ArrayList, LinkedList
// Set -> HashSet, TreeSet
// Map -> HashMap, TreeMap


// HashMap vs HashTable
// HashMap is not synchronized and allows null keys and values, while HashTable is synchronized and does not allow null keys or values. HashMap is generally faster than HashTable because it does not have the overhead of synchronization. However, if you need thread safety, you can use Collections.synchronizedMap() to create a synchronized version of HashMap.