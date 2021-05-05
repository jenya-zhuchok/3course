package nsu.ta.rodchenko;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scaner = new Scanner(System.in);
        System.out.println("Enter a regular expression:");

        //FiniteAutomaton fa = new FiniteAutomaton(scaner.nextLine());
        Automat a = new  Automat(scaner.nextLine());
        Utilit.printResult(a);

    }
}
