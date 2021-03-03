package Ass10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        ContactService cs = new ContactService();
        cs.getAllcontact();
        cs.cl.forEach(System.out::println);
        Scanner scan = new Scanner(System.in);
        cs.addContactNumber(scan.nextInt(), "12345", cs.cl);
        cs.cl.forEach(System.out::println);
    }
}
