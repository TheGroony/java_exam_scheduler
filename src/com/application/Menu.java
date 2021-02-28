package com.application;

import java.util.Scanner;

public class Menu {

    public void start() {
        // spuštění statického metody menu skrze instanci třídy v Main - Je to zbytečné? Ano. Ale ať není všechno staticky, žejo
        startMenu();
    }

    static void startMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ROZPISY NA ZKOUŠENÍ \n Přejete si: \n 1) Pracovat již s existující třídou \n 2) Vytvořit novou třídu \n 3) Stáhnout hotový rozvrh \n 4) Ukončit aplikaci");
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Práce s již existující třídou...");
                    Class.selectClass();
                    break;
                case 2:
                    System.out.println("Vytvoření nové třídy...");
                    Class.createNew();
                    break;
                case 3:
                   FileManager manager = new FileManager();
                   manager.downloadSchedule();
                case 4:
                    System.out.println("Ukončuji aplikaci...");
                    System.exit(0);
                default:
                    System.out.println("Zadejte prosím výběr pouze 1-4");
                    startMenu();
                    break;
            }
        } else {
            System.out.println("Zadali jste neplatný znak. Opakujte akci.");
            startMenu();
        }

    }

    static void scheduleMenu(String nazevTridy) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Přejete si pro tuto třídu: \n 1) Vygenerovat nový rozpis \n 2) Zobrazit rozpis");
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    Class.generateNewSchedule(nazevTridy);
                    break;
                case 2:
                    Class.showSchedule(nazevTridy);
                    break;
                default:
                    System.out.println("Zadejte prosím výběr pouze 1 nebo 2...");
                    scheduleMenu(nazevTridy);
                    break;
            }
        } else {
            System.out.println("Zadali jste neplatný znak. Vracím se do hlavního menu..");
            startMenu();
        }
    }
}
