package nsu.ta.rodchenko;

import java.util.ArrayList;

public class Parser {

    static public boolean isAllowed(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isAlphabetic(str.charAt(i)))
                switch (str.charAt(i)) {
                    case ' ':
                    case '+':
                    case '(':
                    case ')':
                    case '*':
                        break;
                    default:
                        return false;
                }
        }
        return true;
    }

    static public boolean isRightLeft(String str){
        if(!isAllowed(str)) return false;

        int count = 0;
        for(int i = 0; i < str.length(); i++){
            if(Character.isAlphabetic(str.charAt(i))){
                count++;
                if(Character.isLowerCase(str.charAt(i))) return false;
                if(count > 1) return false;
            }
        }

        if(count == 0) return false;

        return true;
    }

    static public ArrayList<String> getAllRights(String str){
        ArrayList<String> rights = new ArrayList<>();
        int lastIndex = 0;
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) != '|') continue;
                if (i == 0) {
                    rights.add("");
                    continue;
                }
                rights.add(str.substring(lastIndex, i));
                i++;
                lastIndex = i;
            }
        rights.add(str.substring(lastIndex));
        return rights;
    }

    static public ArrayList<Rule> parserRules(String str){
        ArrayList<Rule> rules = new ArrayList<>();
            for(int i = 0; i < str.length() - 1; i++){
                if(!(str.charAt(i) == '-' && str.charAt(i+1) == '>')) continue;

                String left = str.substring(0, i);
                if(!isRightLeft(left)){
                    System.out.println("The rule \"" + str + "\" is not correct. It will be not used");
                    return null;
                }

                if(i + 1 > str.length() - 1) {
                    rules.add(new Rule(parsLeft(left), null));
                    return rules;
                }

                for(String s : getAllRights(str.substring(i + 2))){
                    if(isAllowed(s))
                        rules.add(new Rule(parsLeft(left), parsRight(s)));
                    else
                        System.out.println("The rule \"" + left +" -> " + s + "\" is not correct. It will be not used");

                }

            }
        return rules;
    }

    static public Character parsLeft(String str){
        String c = str.trim();
        if (c.length() != 1) return null;
        return c.charAt(0);
    }

    static public ArrayList<Character> parsRight(String str){
        ArrayList<Character> characters = new ArrayList<>();
        String s = str.trim();
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) != ' ')
                characters.add(s.charAt(i));
        }
        return characters;
    }
}
