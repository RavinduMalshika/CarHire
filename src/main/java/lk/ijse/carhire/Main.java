package lk.ijse.carhire;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        File reportsDir = new File("Reports");
        if (!reportsDir.exists()){
            reportsDir.mkdirs();
            new File("Reports/Customer Reports").mkdirs();
            new File("Reports/Car Reports").mkdirs();
            new File("Reports/Rent Reports").mkdirs();
        }

        Launcher.main(args);
    }
}
