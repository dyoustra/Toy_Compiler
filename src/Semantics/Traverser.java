package Semantics;

import Parser.*;
import Scanner.Identifier;
import SymbolTables.SymbolTable;
import SymbolTables.SymbolTableEntry;

public class Traverser {

    private Node root;
    private SymbolTable symbolTable;

    public Traverser(Node root, SymbolTable symbolTable) {
        this.root = root;
        this.symbolTable = symbolTable;
    }

    public void annotateIdentifiers() {
        Node rover = this.root;
        while (rover != null) {
            switch (rover.kind) {
                case ARGUMENTS -> {

                }
                case ARRAY_TYPE -> {

                }
                case BINARY_OPERATOR -> {

                }
                case BLOCK -> {

                }
                case CALL_STATEMENT -> {

                }
                case DOT -> {

                }
                case IF_STATEMENT -> {

                }
                case LITERAL -> {
                    Literal literal = (Literal) rover;
                    Identifier identifier = (Identifier) literal.token;
                    SymbolTableEntry entry = symbolTable.get(identifier.getValue());
                    if (entry == null) throw new RuntimeException("Undefined Error: Identifier " + identifier.getValue() + " not found in symbol table.");
                    else literal.symbolTableEntry = entry;
                }
                case METHOD_DECLARATION -> {

                }
                case VARIABLE_DECLARATION -> {

                }
                case RETURN_STATEMENT -> {

                }
                case TYPE -> {

                }
                case UNARY_OPERATOR -> {

                }
                case VARIABLE -> {

                }
                case WHILE_STATEMENT -> {

                }
            }
        }
    }
}