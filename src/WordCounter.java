import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class WordCounter {

    public static void demo() throws FileNotFoundException {
        Map<String, Integer> wordCount = new HashMap<String, Integer>();

        new BufferedReader(new FileReader("input.txt")).lines()
                .map(line -> line.split("[ ,.?!]+"))
                .forEach(words -> {
                    for(String w: words) {
                        wordCount.put(w,
                                wordCount.getOrDefault(w, 0) + 1);
                    }
                });

        for (String k : wordCount.keySet()) {
            System.out.println(k + " : " + wordCount.get(k));
        }
    }

}
