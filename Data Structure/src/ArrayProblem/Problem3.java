package ArrayProblem;

import javax.sound.midi.spi.SoundbankReader;
import java.util.Collections;

public class Problem3 {

    // Sort an array which contains number only 0s, 1s, 2s
    // array[] = 2,1,2,2,0,1,0,1,1,0,0

    public static void main(String[] args){
        int[] array = {2,1,2,2,0,1,0,1,1,0,0};
        sort(array);
    }

    public static void sort(int[] array){
        int low = 0;
        int mid = 0;
        int high = array.length - 1;

        while(mid <= high){
            if(array[mid] == 0){
                swap(array, low, mid);
                ++low;
                ++mid;
            }
            else if(array[mid] == 2){
                swap(array, mid, high);
                --high;
            }else{
                ++mid;
            }
        }

        System.out.print("Sorted values: ");
        for (int value : array){
            System.out.print(value + ",");
        }
    }

    public static void swap(int [] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
