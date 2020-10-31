import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearcher {
    private static List<File> searchTypeRecursive(File dir, String type, List<File> filePaths) {
        if (filePaths == null) {
            filePaths = new ArrayList<>();
        }
        File[] nestedFiles = dir.listFiles();
        for (File f : nestedFiles) {
            if(f.isDirectory()) {
                searchTypeRecursive(f, type, filePaths);
            }
            else if (f.isFile()) {
                if(f.getName().endsWith(type)) {
                    filePaths.add(f);
                }
            }
        }
        return filePaths;
    }

    public static List<File> searchTypeRecursive(File dir, String type) {
        return searchTypeRecursive(dir, type, null);
    }
}
