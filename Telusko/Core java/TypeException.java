

class ShivamException extends Exception {
    public ShivamException(String message) {
        super(message);
    }
}

public class TypeException {
    public static void main(String[] args){
        int i = 2;
        // int[] num = new int[2];
        String str = null;
        
        try {
            System.out.println(10/i);
            if(str == null) {
                throw new ShivamException("String cannot be null");
            }
            
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero");
            System.exit(0);
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index out of bounds");
        } catch(Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        finally {
            System.out.println("This block will always execute");   
        }
        System.out.println("End of program");
        
    }
    
}

// hirarchy Object -> throwable -> exception -> runtime exception -> arithmetic exception, array index out of bounds exception, null pointer exception, sql exception, io exception, 


// 1. compile time error: Type mismatch: cannot convert from int to String
// for example, if you try to assign an integer value to a String variable, it will result in a compile-time error because the types are incompatible. The compiler will not allow this assignment and will generate an error message indicating the type mismatch.

// 2. runtime error: java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer
// for example, if you try to cast a String object to an Integer object, it will compile successfully but will throw a ClassCastException at runtime because the types are incompatible.

// 3. logic error: The program compiles and runs without errors, but produces incorrect results due to a type-related issue, such as using the wrong data type for a variable or performing an invalid operation on a data type.
// For example, if you declare a variable as an integer but assign it a string value, the program may compile and run without errors, but it will produce incorrect results when you try to perform arithmetic operations on that variable.


// 4. unchecked exception: java.lang.NumberFormatException: For input string: "abc" when trying to parse a non-numeric string to an integer.
// for example, if you try to convert a string that does not represent a valid number (e.g., "abc") to an integer using Integer.parseInt(), it will throw a NumberFormatException at runtime because the input string cannot be parsed as a valid integer.

// 5. checked exception: java.io.IOException when trying to read from a file that does not exist or is inaccessible.
// for example, if you try to read from a file that does not exist or is inaccessible using FileReader or BufferedReader, it will throw an IOException at runtime because the file cannot be found or accessed. This is a checked exception, which means that it must be either caught or declared in the method signature using the throws keyword.

// 6. null pointer exception: java.lang.NullPointerException when trying to access a method or property of a null object reference.
// for example, if you try to call a method or access a property on an object reference that is null, it will throw a NullPointerException at runtime because you are trying to dereference a null object. This can happen if you forget to initialize an object or if an object reference is set to null due to some logic in your code.

// 7. array index out of bounds exception: java.lang.ArrayIndexOutOfBoundsException when trying to access an array element with an index that is outside the valid range of the array.
// for example, if you try to access an array element with an index that is less than 0 or greater than or equal to the length of the array, it will throw an ArrayIndexOutOfBoundsException at runtime because you are trying to access an invalid index in the array. This can happen if you have a loop that iterates over an array and you accidentally use the wrong index variable or if you try to access an element that does not exist in the array.