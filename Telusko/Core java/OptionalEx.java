import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OptionalEx {
    public static void main(String[] args) {
        List<String> names =  Arrays.asList("John", "Jane", "Doe");

        Optional<String> name = names.stream()
                .filter(n -> n.contains("z"))
                .findFirst();

        System.out.println(name.orElse("not found"));

        // String name2 = names.stream()
        //         .filter(n -> n.contains("z"))
        //         .findFirst()
        //         .orElseThrow(() -> new RuntimeException("Name not found"));

        // System.out.println(name2);

        String name3 = names.stream()
                .filter(n -> n.contains("z"))
                .findFirst()
                .orElse("name not found");

        System.out.println(name3);

        
    }
}
