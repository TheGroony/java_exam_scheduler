package com.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Class {

    // vytvoří novou třídu a nahraje do db
    static void createNew() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zadejte prosím název nové třídy (bez mezer):");
        String nazevTridy = scanner.nextLine();
        if (InputValidation.className(nazevTridy)) {
            System.out.println("TIP: každý řádek v načítaném souboru by měl obsahovat pouze jméno a příjmení studenta oddělené čárkou...(viz. přiložený soubor)");
            System.out.println("Nyní prosím zadejte název .csv souboru bez přípony, ze kterého chcete načítat:");
            String nazevSouboru = scanner.nextLine();
            nazevSouboru += ".csv";
            try {
                File soubor = new File(nazevSouboru);
                if (soubor.exists()) {
                    Database.saveClass(nazevTridy);
                    String line = "";
                    BufferedReader br = new BufferedReader(new FileReader(nazevSouboru));
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");
                        String name = values[0];
                        String surname = values[1];
                        Database.saveStudentRow(nazevTridy, name, surname);
                    }
                    System.out.println("Třída úspěšně vytvořena! Vracím se do menu...");
                    Menu.startMenu();
                } else {
                    System.out.println("Soubor nenalezen. Opakujte prosím vytvoření třídy...");
                    createNew();
                }


            } catch (IOException e) {
                System.out.println("Nastala chyba, ukončuji aplikaci...");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.out.println("Zadán neplatný vstup. opakujte prosím akci.");
            createNew();
        }
    }

    // vytvoří nový rozvrh ke tříde, vypíše a nahraje do db
    static void generateNewSchedule(String nazevTridy) {
        // porměnné pro validaci časového inputu
        String startHour = "";
        boolean delimiterStart = false;
        String startMinute = "";
        String endHour = "";
        boolean delimiterEnd = false;
        String endMinute = "";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Zadejte čas v korektním 24hodinovém formátu, kdy chcete začít zkoušet (např. 9:15):");
        String startTime = scanner.next();
        boolean isStartValid = InputValidation.timeInput(startTime);

        //Validace zadaného času
        if (isStartValid) {
            for (int i = 0; i < startTime.length(); i++) {
                String znak = String.valueOf(startTime.charAt(i));
                if (!znak.equals(":") && !delimiterStart) {
                    startHour += znak;
                } else if (znak.equals(":")) {
                    delimiterStart = true;
                } else if (!znak.equals(":") && delimiterStart) {
                    startMinute += znak;
                }
            }

            System.out.println("Nyní zadejte čas, kdy chcete zkoušení ukončit ve stejném formátu:");
            String endTime = scanner.next();
            boolean isEndValid = InputValidation.timeInput(endTime);

            //Validace zadaného času
            if (isEndValid) {
                for (int i = 0; i < endTime.length(); i++) {
                    String znak = String.valueOf(endTime.charAt(i));
                    if (!znak.equals(":") && !delimiterEnd) {
                        endHour += znak;
                    } else if (znak.equals(":")) {
                        delimiterEnd = true;
                    } else if (!znak.equals(":") && delimiterEnd) {
                        endMinute += znak;
                    }
                }

                System.out.println("ZÁKLADNÍ INFO");
                System.out.println("Start: " + startHour + ":" + startMinute);
                System.out.println("Konec: " + endHour + ":" + endMinute);
                int differenceMinutes = TimeCalc.differenceMin(startHour, startMinute, endHour, endMinute);
                System.out.println("Celkem minut: " + differenceMinutes);
                int countStudents = Database.countStudents(nazevTridy);
                System.out.println("Počet žáků: " + countStudents);
                int minutesPerStudent = differenceMinutes / countStudents;
                System.out.println("Počet minut na jednoho žáka: " + minutesPerStudent);
                System.out.println("ROZPIS ZKOUŠENÍ");
                Class trida = new Class();

                // Kompletní výpis, nastavení a uložení zkoušení jednotlivých žáků
                for (int i = 0; i < countStudents; i++) {
                    int random_id_student;

                    do {
                        random_id_student = trida.generateRandom(countStudents);
                    }
                    while (!Database.isScheduleNull(random_id_student, nazevTridy));

                    Calendar calendar;
                    if (i == 0) {
                        int helpMinute = Integer.parseInt(startMinute) - minutesPerStudent;
                        calendar = TimeCalc.studentTime(startHour, String.valueOf(helpMinute), minutesPerStudent);
                    } else {
                        calendar = TimeCalc.studentTime(startHour, startMinute, minutesPerStudent);
                    }
                    String studentHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                    String studentMinute = String.valueOf(calendar.get(Calendar.MINUTE));

                    //bug fix
                    if (studentMinute.length() == 1) {
                        studentMinute = "0" + studentMinute;
                    }

                    startHour = studentHour;
                    startMinute = studentMinute;

                    Database.setSchedule(nazevTridy, random_id_student, startHour, startMinute);
                    String studentName = Database.fetchName(nazevTridy, random_id_student);
                    String studentSurname = Database.fetchSurname(nazevTridy, random_id_student);
                    System.out.println(studentName + " " + studentSurname + ": " + studentHour + ":" + studentMinute);
                }

                System.out.println("Rozpis úspěšně zapsán do databáze. Vracím se do menu...");
                Menu.startMenu();

            } else {
                System.out.println("Zadán špatný formát času! Opakujte akci...");
                startTime = "";
                endTime = "";
                generateNewSchedule(nazevTridy);
            }
        } else {
            System.out.println("Zadán špatný formát času! Opakujte akci...");
            startTime = "";
            generateNewSchedule(nazevTridy);
        }
    }

    //Ukáze hotový rozvrh zkoušení
    static void showSchedule(String className) {
        if (Database.isScheduleNull(1, className)) {
            System.out.println("U této třídy není zatím žádný rozvrh! Přecházím do předešlého menu");
            Menu.scheduleMenu(className);
        }
        System.out.println("Vypisuji rozpis zkoušení z databáze...");
        Database.fetchDoneSchedule(className);
    }

    //Pomocná metoda na vytvoření random čísla v zadaném rozmezí
    public int generateRandom(int countStudents) {
        Random rand = new Random();
        int randomNumber = rand.nextInt(countStudents + 1);
        return randomNumber;
    }

    //Metoda na select třídy, poté uživatel postupuje do dalšího menu
    static void selectClass() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zadejte název třídy, se kterou chcete pracovat");
        String nazevTridy = scanner.nextLine();
        if (InputValidation.className(nazevTridy)) {
            if (Database.findClass(nazevTridy)) {
                Menu.scheduleMenu(nazevTridy);
            } else {
                System.out.println("Zadaná třída nebyla nalezena. Opakujte akci.");
                selectClass();
            }
        } else {
            System.out.println("Zadán neplatný vstup. opakujte prosím akci.");
            selectClass();
        }
    }

}
