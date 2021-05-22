package nsu.ta.rodchenko;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println(
                "Enter new rule in new line. After last rule press \"Enter\" twice. \n" +
                        "Rule form : X -> α , where X ∈ N, α ∈ (N⋃Σ)* \n" +
                        "You can use form: X -> α(1)|α(2)|....|α(n), where α(i) ∈ (N⋃Σ)*, i=1..n \n" +
                        "Enter your rules:");

        Scanner scanner = new Scanner(System.in);
        String s;
        ArrayList<String> lines = new ArrayList<>();
        do{
            s = scanner.nextLine();
            lines.add(s);
        }while (!s.isEmpty());

        Builder hg = new Builder(lines);
    }
}
