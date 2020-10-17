import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class WordCounter {

    public static void demo() throws FileNotFoundException {
        File fin = new File("input.txt");
        BufferedReader br = new BufferedReader(new FileReader(fin));

        Map<String, Integer> wordCount = new HashMap<String, Integer>();

        String regexp = "(?<=[ ,.?!])*[a-zA-Zа-яА-Я]+(?=[ ,.?!]*)";
        Pattern pattern = Pattern.compile(regexp);

        br.lines()
                .map(line -> pattern.matcher(line))
                .forEach(matcher -> {
                    while(matcher.find()) {
                        wordCount.put(matcher.group(),
                                wordCount.getOrDefault(matcher.group(), 0) + 1);
                    }
                });

        for (String k : wordCount.keySet()) {
            System.out.println(k + " : " + wordCount.get(k));
        }
    }

}
