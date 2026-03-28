import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StreamEx {
    public static void main(String[] args) {
        int size = 10_000;
        List<Integer> nums = new ArrayList<>(size);

        Random random = new Random();
        for (int i = 0; i < size; i++)
            nums.add(random.nextInt(100));

        long seqStart = System.currentTimeMillis();
        int sum2 = nums.stream()
        .map(i -> {
            try {
                Thread.sleep(1); // Simulate a time-consuming operation
            } catch (Exception e) {

            }
            return i*2;
        })
        .mapToInt(i -> i)
        .sum();
        long seqEnd = System.currentTimeMillis();

        long parStart = System.currentTimeMillis();
        int sum3 = nums.parallelStream()
        .map(i -> {
            try {
                Thread.sleep(1); // Simulate a time-consuming operation
            } catch (Exception e) {

            }
            return i*2;
        })
        .mapToInt(i -> i)
        .sum();
        long parEnd = System.currentTimeMillis();

        System.out.println(sum2 + " " + sum3);
        System.out.println("Sequential time: " + (seqEnd - seqStart) + " ms");
        System.out.println("Parallel time: " + (parEnd - parStart) + " ms");

        // int sum = nums.stream()
        // .map(i -> i*2)
        // .reduce(0, (c,e) -> c+e);
    }
    
} 
