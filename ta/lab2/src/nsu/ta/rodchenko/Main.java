package nsu.ta.rodchenko;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s;
        ArrayList<String> lines = new ArrayList<>();
        do{
            s = scanner.nextLine();
            lines.add(s);
        }while (!s.isEmpty());

        HomGrammar hg = new HomGrammar(lines);
        hg.ks2hom();

    }
}
