import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReverseIndexClass {
    public static void demo() throws FileNotFoundException {
        //System.out.println("Введите путь к директории проекта Java:");
        File projectDir = new File("/home/nikita/projects/spring-framework");
        List<File> files = FileSearcher.searchTypeRecursive(projectDir, ".java");
        Map<String, List<String>> inheritMap = new HashMap<>();

        Pattern classPattern = Pattern.compile(
                //"(?<=class |interface )([a-zA-Z]+)( extends | implements )*([a-zA-Z]*)"
                "(?<=class |interface )([a-zA-Z]+)(( extends | implements )([a-zA-Z ,])*)?"
        );
        Pattern wordPattern = Pattern.compile(
                //"(?<=class |interface )([a-zA-Z]+)( extends | implements )*([a-zA-Z]*)"
                "(?<=[ ,.?!])*[a-zA-Zа-яА-Я]+(?=[ ,.?!]*)"
        );

        files.stream().
                map((f) -> {
                    try {
                        return new BufferedReader(new FileReader(f));
                    } catch (FileNotFoundException e) {
                        System.out.println("Ошибка при чтении файлов дирекории");
                        return null;
                    }
                }).flatMap(br -> br.lines().filter(classPattern.asPredicate())).
                        map(line -> classPattern.matcher(line)).
                        forEach(matcher -> {
                            while(matcher.find()) {
                                String[] classNameGroups = matcher.group().split(" extends | implements ");
                                for (int i = 1; i < classNameGroups.length; i++) {
                                    Matcher wordMatcher = wordPattern.matcher(classNameGroups[i]);
                                    while(wordMatcher.find()) {
                                        if (inheritMap.get(wordMatcher.group()) == null) {
                                            inheritMap.put(wordMatcher.group(), new ArrayList<>());
                                        }
                                        inheritMap.get(wordMatcher.group()).add(classNameGroups[0]);
                                    }

                                    //System.out.println(classNames[1]);


                                }
                                //inheritMap.get(classNames.length > 1 ? classNames[2] : null).add(classNames[0]);
                            }
                        });


        for (String k : inheritMap.keySet()) {
            System.out.println((k != null ? k : "roots" ) + " : " + inheritMap.get(k));
        }

    }
}
