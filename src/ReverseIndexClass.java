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
        File projectDir = new File("C:\\Users\\Nikita\\IdeaProjects\\spring-framework-main");
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

        ArrayList<Thread> threads = new ArrayList<>();

        files.stream().forEach(f -> {
            Thread t = new Thread(() -> {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(f));
                } catch (FileNotFoundException e) {
                    System.out.println("Ошибка при чтении файлов дирекории");
                }
                br.lines().
                        map(line -> classPattern.matcher(line)).
                        forEach(matcher -> {
                            while (matcher.find()) {
                                String[] classNameGroups = matcher.group().split(" extends | implements ");
                                for (int i = 1; i < classNameGroups.length; i++) {
                                    Matcher wordMatcher = wordPattern.matcher(classNameGroups[i]);
                                    while (wordMatcher.find()) {
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
            });
            t.start();
            threads.add(t);

        });

        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException iex) {
                System.out.println("Thread is interrupted");
            }
        }

            for (String k : inheritMap.keySet()) {
                System.out.println((k != null ? k : "roots") + " : " + inheritMap.get(k));
            }

        }
    }
