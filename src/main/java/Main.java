import task1_fact_getter.FactGetter;
import task2_nasa_photo_getter.NasaPhotoGetter;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
//        Переопределение кодировки консольного вывода (в Maven не требуется)
        System.setOut(new PrintStream(
                new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        FactGetter.main();
        NasaPhotoGetter.main();
    }
}
