package Parser;

import Scanner.Token;

public abstract class Leaf extends Node {

    Token token;
    public Leaf(Token token) {
        this.token = token;
    }
}
