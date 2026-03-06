package ArrayProblem.compair;

import java.util.ArrayList;
import java.util.List;

public class compair {
    public static void main(String[] args) {
        Employee e1= new Employee(1, "shivam", 23, new Address("la, 23", 44002));
        Employee e2 = new Employee(1, "vivek", 25, new Address("la, 23", 333686));
        Employee e3 = new Employee(1, "brijesh", 17, new Address("la, 23", 566));

        List<Employee> employees = new ArrayList<Employee>();
        employees.add(e1);
        employees.add(e2);
        employees.add(e3);

        System.out.println(employees);
    }
}
