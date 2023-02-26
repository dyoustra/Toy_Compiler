package SymbolTables;

import java.util.HashMap;

public class SymbolTable {

    private String name;
    private SymbolTable parent; // if null, root of program
    private HashMap<String, SymbolTableEntry> symbols;

    public SymbolTable(String name, SymbolTable parent) {
        this.name = name;
        this.parent = parent;
        this.symbols = new HashMap<>();
    }

    public void put(String key, SymbolTableEntry value) {
        symbols.put(key, value);
    }

    public SymbolTableEntry get(String key) {
        SymbolTableEntry value = symbols.get(key);
        if (value == null && parent != null) {
            return parent.get(key);
        }
        return value;
    }

    public String getName() {
        return name;
    }
    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public HashMap<String, SymbolTableEntry> getSymbols() {
        return this.symbols;
    }

    public void setSymbols(HashMap<String, SymbolTableEntry> symbols) {
        this.symbols = symbols;
    }


    public void print() {
        System.out.println("Scope: " + (this.name == null ? "anonymous" : name));
        for (SymbolTableEntry entry : this.symbols.values()) {
            if (entry.params != null) {
                System.out.printf("'%4s' %s: %s",  entry.symbolType, entry.name, entry.type);
                for (SymbolTableEntry param : entry.params) {
                    System.out.printf("%s %s, ", param.type, param.name);
                }
                System.out.println();
            } else {
                System.out.printf("'%4s' %s: %s%n", entry.symbolType, entry.name, entry.type);
            }
        }
    }
}
