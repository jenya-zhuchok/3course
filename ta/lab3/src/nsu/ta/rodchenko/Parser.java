package nsu.ta.rodchenko;

import nsu.ta.rodchenko.literals.Literal;
import nsu.ta.rodchenko.literals.NotTerm;
import nsu.ta.rodchenko.literals.Term;

import java.util.ArrayList;

public class Parser {

    static public boolean isRightRight(String str){
        for(int i = 0; i < str.length(); i++){
            if(!Character.isAlphabetic(str.charAt(i)))
                if(str.charAt(i) != ' ') return false;
        }
        return true;
    }

    static public boolean isRightLeft(String str){
        /*if(!isRightRight(str)) return false;

        int count = 0;
        for(int i = 0; i < str.length(); i++){
            if(Character.isAlphabetic(str.charAt(i))){
                count++;
                if(Character.isLowerCase(str.charAt(i))) return false;
                if(count > 1) return false;
            }
        }

        if(count == 0) return false;*/

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
                    rules.add(new Rule(parsLiterals(left), parsLiterals("")));
                    return rules;
                }

                for(String s : getAllRights(str.substring(i + 2))){
                    if(isRightRight(s))
                        rules.add(new Rule(parsLiterals(left), parsLiterals(s)));
                    else
                        System.out.println("The rule \"" + left +" -> " + s + "\" is not correct. It will be not used");

                }

            }
        return rules;
    }

    static public ArrayList<Literal> parsLiterals(String str){
        ArrayList<Literal> literals = new ArrayList<>();
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == ' ') continue;
            if(Character.isAlphabetic(str.charAt(i)))
                if(Character.isUpperCase(str.charAt(i)))
                    literals.add(new NotTerm(str.charAt(i)));
                else
                    literals.add(new Term(str.charAt(i)));
            if(Character.isDigit(str.charAt(i))) {
                int j;
                for(j = 0; j < str.length(); j++)
                    if(!Character.isDigit(str.charAt(i))) break;
                literals.get(literals.size() - 1).setNum(Integer.parseInt(str.substring(i,j)));
            }
        }
        return literals;
    }
}
