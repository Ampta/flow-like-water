package daily;

import java.util.*;

public class ArrayDatab {

    public static void main(String[] args) {
//        ----------------------------------------------------------------------------
//        String str = "010111";
//        int result = minFlips(str);
//        System.out.println(result);
//        ----------------------------------------------------------------------------
//        String str  = "baabacaaa";
//        int result = minMoves(str);
//        System.out.println(result);
//        ----------------------------------------------------------------------------
//        int[] arr = {3,3,4,7,8,4};
//        int d=5;
//        int result = findTheTriplets(arr, d);
//        System.out.println(result);
//        ----------------------------------------------------------------------------
//        fizzBuzz(15);
//        ----------------------------------------------------------------------------
//        int[] arr = {3,4,9};
//        int[] result = transform(arr, 3);
//        System.out.println(Arrays.toString(result));
//        ----------------------------------------------------------------------------
//        List<String> input = Arrays.asList("2 2 1", "3 3 3", "3 4 5", "1 1 3", "6 6 6");
//        List<String> output = classifyTrangle(input);
        // Expected Output: [Isosceles, Equilateral, None of these, None of these, Test]
//        System.out.println(output);

//        ----------------------------------------------------------------------------
        // Test Case from the image
        // requests = ["item1", "item2", "item3", "item1", "item3"]
        // k = 3
//        List<String> input = new ArrayList<>();
//        input.add("item1");
//        input.add("item2");
//        input.add("item3");
//        input.add("item1");
//        input.add("item3");
//
//        int k = 3;
//        List<String> output = getLatestKRequest(input, k);
//        System.out.println("Result: " + output);
//        ----------------------------------------------------------------------------
//        String json1 = "{\"hello\":\"world\", \"hi\":\"hello\", \"you\":\"me\"}";
//        String json2 = "{\"hello\":\"world\", \"hi\":\"helloo\", \"you\":\"me\"}";
//
//        List<String> result = getJsonDiff(json1, json2);

        // Expected Logic:
        // "hello": "world" vs "world" (Same -> Ignore)
        // "hi": "hello" vs "helloo" (Different -> Add "hi")
        // "you": "me" vs "me" (Same -> Ignore)
//        System.out.println("Result: " + result);
        // Output should be ["hi"]
//        ----------------------------------------------------------------------------

        String[] products = {"milk", "eggs", "cheese"};
        double[] productPrices = {2.89, 3,29, 5,79};
        String[] productSold = {"eggs", "eggs", "cheese", "milk"};
        double[] soldPrices = {2.89, 3.29, 7.79, 2.89};

        int result = errorSize(products, productPrices, productSold, soldPrices);
        System.out.println(result);

    }

    public static int errorSize(String[] products, double[] productPrices, String[] productSold, double[] soldPrices) {
        HashMap<String, Double> pMap = new HashMap<>();
        for (int i = 0; i < products.length; i++) {
            pMap.put(products[i], productPrices[i]);
        }

        int error = 0;

        for (int i = 0; i < productSold.length; i++) {
            if(pMap.containsKey(productSold[i])){}
        }


        return error;
    }

    public static List<String> getJsonDiff(String json1,  String json2) {
        Map<String, String> map1 = parseJson(json1);
        Map<String, String> map2 = parseJson(json2);

        List<String> diffKeys = new ArrayList<>();

        // Step 2: Iterate through keys of the first map
        for (String key : map1.keySet()) {
            // Check if the key exists in the second map
            if (map2.containsKey(key)) {
                // Check if the values are DIFFERENT
                // We use !equals() for string comparison
                if (!map1.get(key).equals(map2.get(key))) {
                    diffKeys.add(key);
                }
            }
        }

        // Step 3: Sort the result alphabetically (lexicographically)
        Collections.sort(diffKeys);

        return diffKeys;
    }

    private static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();

        String content = json.substring(1, json.length() - 1);
        String[] pairs = content.split(",");
        for (String pair : pairs) {
            if(pair.trim().isEmpty()) continue;
            String[] entry = pair.split(":");
            String key = entry[0].trim();
            String value = entry[1].trim();
            map.put(key, value);
        }

        return map;
    }


    public static List<String> getLatestKRequest(List<String> request, int k) {
        Set<String> seen =  new HashSet<>();
        List<String> result = new ArrayList<>();

        for(int i = request.size()-1; i >= 0; i--){
            String currentRequest = request.get(i);
            if(!seen.contains(currentRequest)){
                seen.add(currentRequest);
                result.add(currentRequest);

                if(result.size() == k){
                    break;
                }
            }
        }

        return result;
    }


    public static List<String> classifyTrangle(List<String> triangleToy){
        List<String> result = new ArrayList<>();

        for(String toy : triangleToy){
            String[] toyArr = toy.split(" ");
            int[] sides = new int[3];
            sides[0] = Integer.parseInt(toyArr[0]);
            sides[1] = Integer.parseInt(toyArr[1]);
            sides[2] = Integer.parseInt(toyArr[2]);

            Arrays.sort(sides);

            // Step 3: Check Validity (Triangle Inequality Theorem)
            // The sum of the two smaller sides must be greater than the largest side
            if (sides[0] + sides[1] <= sides[2]) {
                result.add("None of these");
            }
            // Step 4: Check Equilateral (All sides equal)
            // If smallest == largest, then all are equal
            else if (sides[0] == sides[2]) {
                result.add("Equilateral");
            }
            // Step 5: Check Isosceles (Any 2 sides equal)
            else if (sides[0] == sides[1] || sides[1] == sides[2]) {
                result.add("Isosceles");
            }
            // Step 6: Valid but Scalene (All sides different)
            else {
                result.add("None of these");
            }
        }

        return result;
    }

    public static int[] transform(int[] arr, int n){
        for(int i=0;i<n;i++){
            for(int j=0; j<arr.length;j++){
                if(arr[j]%2 == 0){
                    arr[j] -=3;
                }else {
                    arr[j]+=3;
                }
            }
        }
        return arr;
    }

    public static void fizzBuzz(int n){
        for(int i=1;i<=n;i++){
            if(i%3==0 && i%5==0){
                System.out.println("FizzBuzz");
            }
            else if(i%3==0 && i%5!=0){
                System.out.println("Fizz");
            }
            else if(i%5==0 && i%3!=0){
                System.out.println("Buzz");
            }
            else{
                System.out.println(i);
            }
        }
    }


    public static int findTheTriplets(int[] arr, int d){
        int count = 0;
        int n = arr.length;
        for(int i=0; i<n-2; i++){
            for(int j=i+1; j<n-1; j++){
                for(int k=j+1; k<n; k++){
                    int sum = arr[i]+arr[j]+arr[k];
                    if(sum%d == 0){
                        count++;
                    }
                }
            }
        }
        return count;
    }


    // string baabacaa = minMoves pick any char and delete if similar neighbour char
    public static int minMoves(String str){
        int[] counts  = new int[26];
        for(char c : str.toCharArray()){
            counts[c-'a']++;
        }

        int totalMoves = 0;
        for(int count : counts){
            totalMoves += count/2;
        }
        return totalMoves;
    }



    // string 101011 = 2 flips to 111111
    public static int minFlips(String str){
        int count = 0;
        for(int i = 0; i<str.length(); i+=2){
            if(str.charAt(i) != str.charAt(i+1)){
                count++;
            }
        }
        return count;
    }

}
