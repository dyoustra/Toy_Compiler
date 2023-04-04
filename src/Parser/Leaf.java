package Parser;

import Scanner.Token;

public abstract class Leaf extends Node {

    public Token token;
    public Leaf(Token token, Kind kind) {
        super(kind);
        this.token = token;
    }
}
