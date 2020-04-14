package ch.scaly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * Unter Linux muss package entfernt werden.
 * Linux ausfuehren mit den folgenden beiden Befehlen:
 * javac Main.java
 * java Main
 */
public class Main {

    // Unter Linux anderer Pfad verwenden, z.B. './testfile.txt'
    private static String FILE_PATH = "C:\\tmp\\java\\testfile1.txt";

    public static void main(String[] args) {
        File file;

        try {
            // Neue Fileinstanz erstellen. File wird nicht erstellt, wenn noch keines vorhanden ist.
            file = new File(FILE_PATH);

            // Falls noch kein File mit dem Pfad existiert wird hier ein neues erstellt. If ist nur fuer die Printausgabe noetig, ansonsten koennte auch einfach file.createNewFile() aufgerufen werden.
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            // Erstellen von neuem FileWriter aus dem file. Anschliessend wird der Text ins File geschrieben und am schluss der writer weider geschlossen.
            FileWriter myWriter = new FileWriter(file);
            myWriter.write("Files in Java might be tricky, but it is fun enough!");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch (IOException e) {
            System.out.println("An error occurred while file writing.");
            e.printStackTrace();
        }


        /*
            Zwei verschiedene Varianten um Berechtigungen auf ein File zu vergeben. Beide arbeiten mit dem posix System.
            Variante 1: Hier werden die Berechtigungen aus einem String 'rw-r--rw-' vergeben.
            Variante 2: Bei dieser Variante werden die einzelnen Berechtigungen dem Berechtigungsset hinzugefuegt. Dies geschieht ueber ein Enum 'PosixFilePermission.OWNER_WRITE'.
         */
        try {
            // Erstellen des Path fuer beie Varianten benoetigt. Auch  'Path path = file.getPath();' waere moeglich, dann muss der Pfad nicht zwei Orten abgerufen werden.
            Path path = Paths.get(FILE_PATH);

            // Variante 1 ----------------------------------------------------------------------------------------------------

            // Erstellen der Berechtigungen nach dem Posix Schema aus einem String
            Set<PosixFilePermission> ownerWritable = PosixFilePermissions.fromString("rw-r--rw-");
            // Erstelltes Berechtigungsset auf File anwenden
            Files.setPosixFilePermissions(path, ownerWritable);


            // Variante 2 ----------------------------------------------------------------------------------------------------

            // Erstellen des Set aus den aktuellen Berechtigungen des Files.
            Set<PosixFilePermission> perms = Files.readAttributes(path, PosixFileAttributes.class).permissions();

            perms.clear();
            // Nun werden alle Berechtigungen ueberschrieben, bzw. neu hinzugefuegt. Theoretisch koennen hier auch nur die Rechte hinzugefuegt werden, die zuvor nicht existierten.
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            perms.add(PosixFilePermission.GROUP_WRITE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            perms.add(PosixFilePermission.OTHERS_WRITE);
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);

            // Anwenden der Berechtigungena uf das File
            Files.setPosixFilePermissions(path, perms);

        } catch (IOException e) {
            System.out.println("An error occurred while changing file permissions.");
            e.printStackTrace();
        }

    }
}
