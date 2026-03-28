import java.util.Arrays;
import java.util.List;

public class TypeStream {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(2, 3, 45, 1, 20);

        int result = nums.stream()
        .filter(n -> n%2==0)
        .map(n -> n*2)
        .reduce(0, (c,e) -> c+e);

        System.out.println(result);





         // Stream<Integer> s1 = nums.stream();
        // Stream<Integer> s2 = s1.filter(n -> n%2==0);
        // Stream<Integer> s3 = s2.map(n -> n*2);
        // int result = s3.reduce(0, (c,e) -> c+e);
        // System.out.println(result);

        // s3.forEach(n -> System.out.println(n));

        // Consumer<Integer> con = new Consumer<Integer>() {
        //     public void accept(Integer n){
        //         System.out.println(n*2);
        //     }
        // };

        // Consumer<Integer> con = n -> System.out.println(n*2);

        // nums.forEach(con);




        // example

        // System.out.println("Using for each method");
        // nums.forEach(n -> System.out.println(n));

        // System.out.println("Using Stream API");
        // nums.stream().forEach(System.out::println);


        // System.out.println("Using for loop");
        // for(int i = 0; i < nums.size(); i++){
        //     System.out.println(nums.get(i));
        // }

        // System.out.println("Using for each loop");
        // for(int n: nums){
        //     System.out.println(n);
        // }

        // for(int n: nums){
        //     if(n%2==0){
        //         n = n*2;
        //         sum = sum + n;
        //     }
        // }

        // System.out.println(sum);
    }
}

// java 1.8
// Stream API - > filter, map, reduce, collect, forEach, sorted, distinct, limit, skip, count, anyMatch, allMatch, noneMatch, findFirst, findAny, flatMap, peek, max, min, sum, average, toArray, toList,
// Stream API is a powerful tool for processing collections of data in a functional programming style. It allows you to perform operations on collections such as filtering, mapping, reducing, and collecting results in a concise and readable way. Here are some of the key operations you can perform with Stream API:
// Filter: This operation allows you to filter elements from a stream based on a given condition. For example, you can use the filter method to select only even numbers from a list of integers.
// Map: This operation allows you to transform elements from a stream into another form. For example you can use the map method to convert a list of strings into a list of their lengths.
// Reduce: This operation allows you to combine elements from a stream into a single result. For example, you can use the reduce method to calculate the sum of a list of integers.
// Collect: This operation allows you to collect the results of a stream into a collection such as a List, Set, or Map. For example, you can use the collect method to gather the results of a stream into a List.  
// ForEach: This operation allows you to perform an action for each element in a stream. For example, you can use the forEach method to print each element of a list of strings.
