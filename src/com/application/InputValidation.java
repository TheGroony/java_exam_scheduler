package com.application;


public class InputValidation {

    // Aby uživatel nezadal žádnou třídu s divným názvem, která by dělala problémy
    static boolean className(String name) {
        if (name.matches("[a-z1-9A-Zěščřžýáíé_]+")) {
            return true;
        } else return false;
    }


    //Validace inputu času (př. 9:15) ne 28:775
    static boolean timeInput(String time) {

        for (int i = 0; i < time.length(); i++) {
            String znak = String.valueOf(time.charAt(i));
            switch (i) {
                case 0:
                    String nextChar = String.valueOf(time.charAt(i + 1));
                    if (nextChar.equals(":")) {
                        if (znak.matches("[1-9]")) {
                            break;
                        }
                    } else if (znak.matches("[1-2]")) {
                        break;
                    } else return false;
                case 1:
                    String prevChar1 = String.valueOf(time.charAt(i - 1));
                    if (znak.matches(":")) {
                        break;
                    }
                    if (prevChar1.equals("1")) {
                        if (znak.matches("[1-90]")) {
                            break;
                        } else return false;
                    } else if (prevChar1.equals("2")) {
                        if (znak.matches("[1-30]")) {
                            break;
                        } else return false;
                    }

                case 2:
                    if (znak.matches(":")) {
                        break;
                    } else if (znak.matches("[1-50]")) {
                        break;
                    } else return false;
                case 3:
                    String prevChar2 = String.valueOf(time.charAt(i - 1));
                    if (prevChar2.matches(":")) {
                        if (znak.matches("[1-50]")) {
                            break;
                        } else return false;
                    } else if (znak.matches("[1-90]")) {
                        break;
                    } else return false;
                case 4:
                    if (znak.matches("[1-90]")) {
                        break;
                    } else return false;
            }
        }
        return true;
    }
}
