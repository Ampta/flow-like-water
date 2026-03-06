package ArrayProblem;

public class TimeAndSpaceComplexity
{
    // This method has O(1) space complexity constant
    public static int[] constantSpace(int[] array, int number){
        int i, j=10;
        for(i=0; i<10; i++){
            array[i] = i;
        }
        return array;
    }

    // This method has O(n) space complexity constant
    public static int[] nTimesSpace(int[] array, int number){
        int i;
        int[] result = new int[number];
        for(i=0; i<number; i++){
            result[i] = i;
        }
        return result;
    }

    public static void main(String[] args){
        int[] newArray = new int[10];
        int[] emptyArray = new int[400];
        int[] result1 = constantSpace(newArray, 10);
        int[] result2 = nTimesSpace(newArray, 100);

        for(int value: result2){
            System.out.print(value + ",");
        }
    }

}

