package ArrayProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Problem2 {

    // Design data structure that supports insert, delete search and
    // random number in O(1) time

    ArrayList<Integer> arrayList = new ArrayList<>();
    HashMap<Integer, Integer> hashMap = new HashMap<>();

    void insert(int num){
        if(hashMap.get(num) != null){
            return;
        }
        int size = arrayList.size();
        arrayList.add(num);
        hashMap.put(num, size);
    }

    void delete(int num){
        int index = hashMap.get(num);
        hashMap.remove(num);
        int size = arrayList.size();
        int last = arrayList.get(size-1);
        Collections.swap(arrayList, index, size-1);
        arrayList.remove(size-1);
        hashMap.put(last, index);
    }

    int findRandom(){
        int index = (int) (Math.random() * arrayList.size());
        return arrayList.get(index);
    }

    public static void main(String[] args){
        Problem2 problem = new Problem2();
        problem.insert(5);
        problem.insert(9);
        problem.insert(54);
        problem.insert(3);

        System.out.println("Random Value from problem: " + problem.findRandom());

    }


}
