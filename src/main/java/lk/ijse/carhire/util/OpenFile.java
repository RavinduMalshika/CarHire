package lk.ijse.carhire.util;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OpenFile {
    public static void openFile(String location) {
        try {
            File file = new File(location);

            if(Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if(file.exists()) {
                    desktop.open(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
