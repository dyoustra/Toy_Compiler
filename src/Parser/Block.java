package Parser;

import SymbolTables.SymbolTable;

import java.util.ArrayList;

public class Block extends Node {

    public SymbolTable symbolTable;

    public ArrayList<Node> statements;

    public Block(ArrayList<Node> statements, SymbolTable symbolTable) {
        super(Kind.BLOCK);
        this.statements = statements;
        this.symbolTable = symbolTable;
    }
}
