import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReverseIndexClass {
    public static void demo() throws FileNotFoundException {
        //System.out.println("Введите путь к директории проекта Java:");
        File projectDir = new File("C:\\Users\\User\\IdeaProjects\\spring-framework-main");
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

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ArrayList<Future<HashMap<String, List<String>>>> futures = new ArrayList<>();


        files.stream().forEach(f -> {
            futures.add(executorService.submit( () -> {

                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(f));
                } catch (FileNotFoundException e) {
                    System.out.println("Ошибка при чтении файлов дирекории");
                }
                HashMap<String, List<String>> inheritMapFile = new HashMap<>();

                br.lines().
                        map(line -> classPattern.matcher(line)).
                        forEach(matcher -> {
                            while (matcher.find()) {
                                String[] classNameGroups = matcher.group().split(" extends | implements ");
                                for (int i = 1; i < classNameGroups.length; i++) {
                                    Matcher wordMatcher = wordPattern.matcher(classNameGroups[i]);
                                    while (wordMatcher.find()) {
                                        if (inheritMapFile.get(wordMatcher.group()) == null) {
                                            inheritMapFile.put(wordMatcher.group(), new ArrayList<>());
                                        }
                                        inheritMapFile.get(wordMatcher.group()).add(classNameGroups[0]);
                                    }
                                }
                                //System.out.println(classNames[1]);


                            }
                            //inheritMap.get(classNames.length > 1 ? classNames[2] : null).add(classNames[0]);
                        });

                    return inheritMapFile;

            }));
        });
        /*for (FutureTask<HashMap<String, List<String>>> future : futures) {
            executorService.execute(future);
        }*/


        for (Future<HashMap<String, List<String>>> future : futures) {

            try {
                for (String key : future.get().keySet()) {
                    if(inheritMap.get(key) == null) {
                        inheritMap.put(key, new ArrayList<>());
                    }
                    for(String value : future.get().get(key)) {
                        inheritMap.get(key).add(value);
                    }

                }
            } catch (java.lang.InterruptedException e) {
                e.printStackTrace();
            }
            catch (java.util.concurrent.ExecutionException e) {
                e.printStackTrace();
            }
        }
        for(Map.Entry e : inheritMap.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }



    }
}
