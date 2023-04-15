package SymbolTables;

import java.util.HashMap;


public class SymbolTable {

    private final String name;
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
        return parent == null ? this : parent;
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
            if (entry instanceof MethodSymbol method) {
                // methods can't return array types
                System.out.printf("\t%s %s: %s%n",  entry.getClass().getName(), entry.name, entry.type.kind.name());
                for (SymbolTableEntry param : method.params) {
                    System.out.printf("%s %s, ", param.type, param.name);
                }
                System.out.println();
            } else {
                if (entry.type instanceof SymbolTableEntry.ArrayType arrayType)
                    System.out.printf("\t%s %s: %s[%d]%n", entry.getClass().getName().split("\\.")[1], entry.name, arrayType.kind.name(), arrayType.size);
                else {
                    System.out.printf("\t%s %s: %s%n", entry.getClass().getName().split("\\.")[1], entry.name, entry.type.kind.name());
                }
            }
        }
    }
}
