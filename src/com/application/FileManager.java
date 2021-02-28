package com.application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {

    // Uloží seznam zkoušení do txt souboru
    public void downloadSchedule() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zadejte název třídy, ke které chcete stáhnout rozvrh:");
        String className = scanner.nextLine();
        if (InputValidation.className(className)) {
            if (Database.findClass(className)) {
                if (!Database.isScheduleNull(1, className)) {
                    System.out.println("Zadejte prosím název TEXTOVÉHO souboru bez přípony a mezer, do kterého chcete rozpis uložit:");
                    String fileName = scanner.next();
                    fileName += ".txt";
                    try {
                        File file = new File(fileName);
                        if (file.createNewFile()) {
                            System.out.println("Soubor " + fileName + " vytvořen.");
                            System.out.println("Zapisuji rozvrh zkoušení do souboru...");
                            FileWriter writer = new FileWriter(fileName);
                            int rows = Database.countStudents(className);
                            for (int i = 1; i <= rows; i++) {

                                String name = Database.fetchName(className, i);
                                String surname = Database.fetchSurname(className, i);
                                String schedule = Database.fetchScheduleRow(className, i);

                                writer.write(name + " " + surname + ": " + schedule + "\n");
                            }

                            writer.close();
                            System.out.println("Rozvrh úspěšně stáhnut. Vracím se do hlavního menu...");
                            Menu.startMenu();

                        } else {
                            System.out.println("Soubor již existuje. Vracím se do hlavního menu...");
                            Menu.startMenu();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Zadaná třída zatím nedisponuje žádním rozvrhem, nejprve ho musíte vytvořit. Vracím se do hlavního menu...");
                    Menu.startMenu();
                }
            } else {
                System.out.println("Zadaná třída nebyla nalezena. Opakujte akci.");
                downloadSchedule();
            }
        } else {
            System.out.println("Zadán neplatný vstup. Opakujte prosím akci.");
            downloadSchedule();
        }
    }
}
