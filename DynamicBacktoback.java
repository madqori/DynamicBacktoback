
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 *
 * @author Muhammad
 */
public class DynamicBacktoback {
    
    public static void main(String[] args) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name = reader.readLine();    
        String[] input = name.split(",");
        System.out.print("{");
        for(String text : doSortingPlaylistDataWithNoAdjacent(input)){
            System.out.print(text + " ");
        }
        System.out.print("}");
    }
    
    private static String[] doSortingPlaylistDataWithNoAdjacent(String[] listData) {
        String[] sortedWithNoAdjacentOrderItem = new String[listData.length];
        String keySource = "abcdefghijklmnopqrstuvwxyz";
        List<String> listKey = new ArrayList<>();
        Map<String, List<String>> data = new HashMap<String, List<String>>();
        for (int j = 0; j < listData.length; j++) {
            String key = listData[j];
            if (data.containsKey(key)) {
                List<String> value = data.get(key);
                value.add(listData[j]);
                data.put(key, value);
            } else {
                listKey.add(key);
                List<String> value = new ArrayList<>();
                value.add(listData[j]);
                data.put(key, value);
            }
        }
        String output = "";
        Map<String, String> keyDelegate = new HashMap<String, String>();
        for (int j = 0; j < listKey.size(); j++) {
            List<String> keyValue = data.get(listKey.get(j));
            for (int k = 0; k < keyValue.size(); k++) {
                output += keySource.charAt(j);
                if (keyDelegate.containsKey(String.valueOf(keySource.charAt(j))) == false) {
                    keyDelegate.put(String.valueOf(keySource.charAt(j)), listKey.get(j));
                }
            }
        }

        String tempOutput = randomPerm(output);
        if (tempOutput != null) {
            while (isPlaylistStillHadTwoConsecutive(tempOutput) || tempOutput.length() != output.length()) {
                tempOutput = randomPerm(output);
            }
            output = tempOutput;
        } else {
            return listData;
        }

        for (int iterate = 0; iterate < output.length(); iterate++) {
            String value = keyDelegate.get(String.valueOf(output.charAt(iterate)));
            List<String> listOrderByKey = data.get(value);
            int indexToReturn = new Random().nextInt(listOrderByKey.size());
            String term = listOrderByKey.get(indexToReturn);

            sortedWithNoAdjacentOrderItem[iterate] = term;
            listOrderByKey.remove(indexToReturn);
            data.replace(value, listOrderByKey);
        }
        return sortedWithNoAdjacentOrderItem;
    }
    
     static boolean isPlaylistStillHadTwoConsecutive(String term) {
        for (int i = 0; i < term.length(); i++) {
            if (i + 1 != term.length()) {
                if (term.charAt(i) == term.charAt(i + 1)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static String randomPerm(String str) {
        Map<Character, Long> counts = str
                .chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return restPerm(null, counts);
    }

    private static final Random RANDOM = new Random();

    public static String restPerm(Character previous, Map<Character, Long> leftover) {
        List<Character> leftKeys = new ArrayList<>(leftover.keySet());
        while (!leftKeys.isEmpty()) {
            Character nextChar = leftKeys.get(RANDOM.nextInt(leftKeys.size()));
            leftKeys.remove(nextChar);
            if (nextChar.equals(previous) || leftover.get(nextChar) == 0) {
                continue; 
            }
            Long count = leftover.compute(nextChar, (ch, co) -> co - 1);
            if (leftover.values().stream().noneMatch(c -> c > 0)) {
                return nextChar.toString(); 
            }
            String rest = restPerm(nextChar, leftover);
            if (rest != null) {
                return nextChar + rest;
            }
            leftover.put(nextChar, count + 1);
        }
        return null;
    }
}
