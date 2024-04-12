package org.example.utils;

import java.util.Scanner;

public class InputUtils {
    public static String inputStringWithRegexCheck(String regex, String promptMessage, String errorMessage) {
        Scanner sc = new Scanner(System.in);
        String str = null;
        while (str == null || str.isEmpty()) {
            System.out.println(promptMessage);
            str = sc.nextLine();
            if (!str.matches(regex)) {
                System.out.println(errorMessage);
                str = null;
            }
        }
        return str;
    }

    public static String inputString(String promptMessage, String errorMessage) {
        Scanner sc = new Scanner(System.in);
        String str = null;
        while (str == null || str.isEmpty()) {
            System.out.println(promptMessage);
            str = sc.nextLine();
            if (str.isEmpty()) {
                System.out.println(errorMessage);
            }
        }
        return str;
    }

    public static Double inputDouble(String promptMessage, String errorMessage) {
        Scanner sc = new Scanner(System.in);
        String doubleStr = null;
        Double doubleValue = null;
        while (doubleStr == null || doubleStr.isEmpty()) {
            System.out.println(promptMessage);
            doubleStr = sc.nextLine();
            if (doubleStr.isEmpty()) {
                System.out.println(errorMessage);
            }
            try {
                doubleValue = Double.parseDouble(doubleStr);
                if (doubleValue <= 0) {
                    doubleStr = null;
                }
            } catch (Exception e) {
                doubleStr = null;
                System.out.println(errorMessage);
            }
        }
        return doubleValue;
    }

    public static Integer inputInteger(String promptMessage, String errorMessage) {
        Scanner sc = new Scanner(System.in);
        String intStr = null;
        Integer intValue = null;
        while (intStr == null || intStr.isEmpty()) {
            System.out.println(promptMessage);
            intStr = sc.nextLine();
            if (intStr.isEmpty()) {
                System.out.println(errorMessage);
            }
            try {
                intValue = Integer.parseInt(intStr);
                if (intValue <= 0) {
                    intStr = null;
                }
            } catch (Exception e) {
                intStr = null;
                System.out.println(errorMessage);
            }
        }
        return intValue;
    }
}
