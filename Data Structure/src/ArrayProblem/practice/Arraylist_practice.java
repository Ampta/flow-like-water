package ArrayProblem.practice;

import java.util.*;

public class Arraylist_practice {


    public static void main(String[] args) {
        ArrayList<String> secondList = new ArrayList<>();
        ArrayList<Integer> firstList = new ArrayList<>();

        firstList.add(1);
        firstList.add(2);
        firstList.add(3);

        secondList.add("Shivam");
        secondList.add("Vivek");

//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//
//        List<Integer> list2 = new ArrayList<>();
//        list2.add(100);
//        list2.add(200);
//        list2.add(300);
//
//        list.addAll(list2);
//        System.out.println(list);
//
//        System.out.println(list.get(5));
//        list.remove(2);
//        System.out.println(list);
//        System.out.println(list.remove(Integer.valueOf(300)));
//        System.out.println(list);
//        list.set(1, 200);
//        System.out.println(list);
//        System.out.println(list.contains(200));
//
//        for(int j = 0; j<list.size(); j++){
//            System.out.println(list.get(j));
//        }
//
//        for(Integer i : list){
//            System.out.println(i);
//        }

//        Stack
//        Stack<String> stack = new  Stack<>();
//        stack.push("Shivam");
//        stack.push("Vivek");
//        stack.push("Ashvin");
//        stack.push("Bilsan");
//
//        stack.pop();
//        System.out.println(stack.peek());


//        Queue
//        Queue<Integer> queue = new LinkedList<>();
//        queue.offer(1);
//        queue.offer(2);
//        queue.offer(3);
//
//        System.out.println(queue);
//        System.out.println(queue.poll());
//        System.out.println(queue);
//        System.out.println(queue.peek());


//        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
//        pq.add(21);
//        pq.add(9);
//        pq.add(17);
//        pq.add(40);
//
//        System.out.println(pq);
//        System.out.println(pq.poll());
//        System.out.println(pq);


        // stores only unique element and unordered
//        Set<Integer> set = new HashSet<>();
//        set.add(1);
//        set.add(40);
//        set.add(17);
//        set.add(9);
//
//        System.out.println(set);
//        set.remove(17);
//        System.out.println(set);
//        System.out.println(set.contains(40));
//
//        // maintian orders
//        Set<Integer> set2 = new LinkedHashSet<>();
//        set2.add(9);
//        set2.add(2);
//        set2.add(18);
//        System.out.println(set2);

        Map<String, Integer> map = new HashMap<>();
        map.put("Shivam", 1);
        map.put("Vivek", 2);
        map.put("Ashvin", 3);
        System.out.println(map);

        for(Map.Entry<String, Integer> entry : map.entrySet()){
            System.out.println(entry.getKey());
        }

        for(String keys: map.keySet()){
            System.out.println(keys);
        }







    }
}
