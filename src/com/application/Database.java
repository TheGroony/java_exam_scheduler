package com.application;

import java.sql.*;

public class Database {

    //Uloží třídu
    static void saveClass(String className) {
        String sql = "CREATE TABLE " + className + " (id_zaka INT NOT NULL AUTO_INCREMENT, jmeno_zaka VARCHAR(45) NOT NULL, prijmeni_zaka VARCHAR(45) NOT NULL, cas_zkouseni VARCHAR(45) NULL, PRIMARY KEY (id_zaka))";
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
                Statement statement = con.createStatement();

        ) {
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }

    }

    //Uloží záznam studenta (pouze jméno a příjmení)
    static void saveStudentRow(String className, String jmeno_zaka, String prijmeni_zaka) {
        String sql = "INSERT INTO " + className + " (jmeno_zaka, prijmeni_zaka) VALUES (?,?)"; // bug fix
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
                PreparedStatement statement = con.prepareStatement(sql);

        ) {
            statement.setString(1, jmeno_zaka);
            statement.setString(2, prijmeni_zaka);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }
    }

    // bool - jestli db obsahuje třídu
    static boolean findClass(String className) {
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
        ) {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet result = meta.getTables(null, null, className, null);
            if (result.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }
        return false;
    }

    // počet studentů ve třídě
    static int countStudents(String className) {
        String sql = "SELECT COUNT(id_zaka) FROM " + className;
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
                Statement statement = con.createStatement();

        ) {
            ResultSet rows = statement.executeQuery(sql);
            while (rows.next()) {
                return rows.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }
        return 0;
    }

    //Jestli je k danému studentu již napsaný čas zkoušení
    static boolean isScheduleNull(int student_id, String className) {
        String sql = "SELECT cas_zkouseni FROM " + className + " WHERE id_zaka = " + student_id;
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
                Statement statement = con.createStatement();

        ) {
            ResultSet rows = statement.executeQuery(sql);
            while (rows.next()) {
                String row = rows.getString("cas_zkouseni");
                if (row == null) {
                    return true;
                } else return false;
            }

        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }
        return false;
    }

    //Nastaví čas zkoušení pro jednoho studenta
    static void setSchedule(String className, int student_id, String hour, String minute) {
        String sql = "UPDATE " + className + " SET cas_zkouseni = ? WHERE id_zaka = ?";
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
                PreparedStatement statement = con.prepareStatement(sql);

        ) {
            String time = hour + ":" + minute;
            statement.setString(1, time);
            statement.setString(2, String.valueOf(student_id));
            statement.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }
    }

    //Vrátí jméno specifického studenta
    static String fetchName(String className, int student_id) {
        String sql = "SELECT jmeno_zaka FROM " + className + " WHERE id_zaka = " + student_id;
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
                Statement statement = con.createStatement();

        ) {
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String jmeno = result.getString("jmeno_zaka");
                return jmeno;
            }

        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }
        return "chyba";
    }

    //Vrátí příjmení specifického studenta
    static String fetchSurname(String className, int student_id) {
        String sql = "SELECT prijmeni_zaka FROM " + className + " WHERE id_zaka = " + student_id;
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
                Statement statement = con.createStatement();

        ) {
            ResultSet result = statement.executeQuery(sql);
            if (result.next()) {
                String prijmeni = result.getString("prijmeni_zaka");
                return prijmeni;
            }

        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }
        return "chyba";
    }

    //Vrátí čas zkoušky specifického studenta
    static String fetchScheduleRow(String className, int student_id) {
        String sql = "SELECT cas_zkouseni FROM " + className + " WHERE id_zaka = " + student_id;
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
                Statement statement = con.createStatement();

        ) {
            ResultSet result = statement.executeQuery(sql);
            if (result.next()) {
                String prijmeni = result.getString("cas_zkouseni");
                return prijmeni;
            }

        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }
        return "chyba";
    }

    // Vypíše celý seznam do konzone
    static void fetchDoneSchedule(String className) {
        String sql = "SELECT * FROM " + className;
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zkouseni", "root", "root");
                Statement statement = con.createStatement();

        ) {
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String jmeno = result.getString("jmeno_zaka");
                String prijmeni = result.getString("prijmeni_zaka");
                String zkouseni = result.getString("cas_zkouseni");
                System.out.println(jmeno + " " + prijmeni + ": " + zkouseni);
            }
            System.out.printf("Vracím se do hlavního menu...");
            Menu.startMenu();

        } catch (SQLException e) {
            System.out.println("Nastala chyba.");
            e.printStackTrace();
        }
    }
}
