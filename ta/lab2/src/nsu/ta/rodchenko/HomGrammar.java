package nsu.ta.rodchenko;


import java.util.ArrayList;

public class HomGrammar {
    Grammar ks_grammar = new Grammar();
    Grammar hom_grammar = new Grammar();
    private ArrayList<Rule> newRules = new ArrayList<>();

    public HomGrammar(ArrayList<String> str){
        ArrayList<Rule> rules = new ArrayList<>();
        for(String s : str){
            rules.addAll(Parser.parserRules(s));
        }
        ks_grammar = new Grammar(rules);
        //ks_grammar.setRules();
    }


    public void ks2hom(){
        System.out.println("********************************");

        System.out.println("Your rules:");
        ks_grammar.printRules();
        System.out.println("********************************");

        System.out.println("alphabet:");
        if(ks_grammar.isEmptyLanguage())
            System.out.print("language is empty");
        ks_grammar.printAlphabet();
        System.out.println("********************************");

        fistStep();
        secondStep();

        System.out.println("New rules:");
        hom_grammar.printRules();
    }

    private void fistStep(){

        ArrayList<Rule> rules = ks_grammar.getRules();
        ArrayList<Rule> forgot = new ArrayList<>();

        for(Rule r : rules){
            switch (r.getRightSize()){
                case 0:
                    hom_grammar.addRule(r);
                    forgot.add(r);
                    break;
                case 1:
                    if(r.getRight().get(0) instanceof Term)
                        hom_grammar.addRule(r);
                    forgot.add(r);

                    break;
                case 2:
                    if(r.getRight().get(0) instanceof NotTerm && r.getRight().get(1) instanceof NotTerm) {
                        hom_grammar.addRule(r);
                        forgot.add(r);
                    }
                    break;
            }
        }
        for(Rule r: forgot){
            rules.remove(r);
        }
    }




    private Rule findByRight(ArrayList<Literal> tmp){
        ArrayList<Rule> rules = hom_grammar.getRules();
        for(Rule r: rules)
            if(r.isRightEquals(tmp)) return r;
        return null;
    }


    private Rule newAnonymousRule(ArrayList<Literal> literals){
        int i = newRules.size();
        Rule rule = new Rule(Parser.parsLiterals("N" + i), literals);
        newRules.add(rule);

        return rule;
    }

    private Rule getRuleWithRight(ArrayList<Literal> literals){

        Rule rule = findByRight(literals);
        if( rule != null) return rule;

        rule =  newAnonymousRule(literals);
        hom_grammar.addRule(rule);

        return rule;
    }

    private void secondStep(){
        ArrayList<Rule> rules = ks_grammar.getRules();
        ArrayList<Rule> forgot = new ArrayList<>();
        ArrayList<Rule> splited = new ArrayList<>();
        for(Rule r : rules){
            switch (r.getRightSize()){
                case 1:
                    break;
                case 2: {

                    ArrayList<Literal> right = r.getRight();
                    Rule newRule;

                    Literal notTerm = right.remove((right.get(0) instanceof Term) ?  1: 0);
                    newRule = new Rule (r.getLeft(), getRuleWithRight(right).getLeft());
                    if(right.get(0) instanceof Term)
                        newRule.addLiteralToEndRight(notTerm);
                    else
                        newRule.addLiteralToTopRight(notTerm);

                    splited.add(newRule);
                    forgot.add(r);
                }
                break;
                default:
                    splited.add(split(r));

            }
        }

        for(Rule r: forgot){
            rules.remove(r);
        }


        if(splited.size() > 0 )
            ks_grammar.getRules().addAll(splited);
        fistStep();
        if(splited.size() > 0 )
            secondStep();

    }

    private Rule split(Rule r) {
        ArrayList<Literal> rightForNewRule = r.getRight();
        ArrayList<Literal> rightForOldRule = new ArrayList<>();
        rightForOldRule.add(rightForNewRule.remove(0));
        Rule newRule = newAnonymousRule(rightForNewRule);
        r.setRight(rightForOldRule);
        r.addLiteralToEndRight(newRule.getLeft());
        return newRule;
    }


}
