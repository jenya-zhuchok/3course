package nsu.ta.rodchenko.literals;

import nsu.ta.rodchenko.literals.Literal;

public class Term extends Literal {

    public Term(char c) {
        super(Character.toLowerCase(c));
    }

}
