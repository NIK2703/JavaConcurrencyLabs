import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        File fin = new File("input.txt");
        BufferedReader br = new BufferedReader(new FileReader(fin));

        Map<String, Integer> wordCount = new HashMap<String, Integer>();

        String regexp = "(?<=[ ,.])*[a-zA-Zа-яА-Я]+(?=[ ,.]*)";
        Pattern pattern = Pattern.compile(regexp);

        br.lines()
                .map(line -> pattern.matcher(line))
                .forEach(matcher -> {
                    while(matcher.find()) {
                        wordCount.put(matcher.group(),
                                (wordCount.containsKey(matcher.group()) ? wordCount.get(matcher.group()) : 0) + 1);
                    }
                });

        for()
    }

}
