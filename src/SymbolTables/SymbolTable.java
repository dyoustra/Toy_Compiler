package SymbolTables;

import java.util.HashMap;


public class SymbolTable {

    private final String name;
    private SymbolTable parent;
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
        return this.parent;
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
                System.out.printf("\t%s %s: %s%n",  entry.getClass().getName(), entry.name, entry.type);
                for (SymbolTableEntry param : method.params) {
                    System.out.printf("%s %s, ", param.type, param.name);
                }
                System.out.println();
            } else {
                if (entry.type.isArray())
                    System.out.printf("\t%s %s: %s[%d]%n", entry.getClass().getName().split("\\.")[1], entry.name, entry.type, entry.arraySize);
                else {
                    System.out.printf("\t%s %s: %s%n", entry.getClass().getName().split("\\.")[1], entry.name, entry.type);
                }
            }
        }
    }

    // the global symbol table of every program, set as the root in Parser.java
    // Hardcoded, language-defined methods and variables go here
    public static SymbolTable globalSymbolTable() {
         SymbolTable symbolTable = new SymbolTable("global", null);
         // input(char c) method
         ParameterSymbol[] inputParams = { new ParameterSymbol("c", SymbolTableEntry.Type.CHAR) };
         symbolTable.put("input", new MethodSymbol("input", SymbolTableEntry.Type.VOID, inputParams));
         // output(char c) method
        ParameterSymbol[] outputParams = { new ParameterSymbol("c", SymbolTableEntry.Type.CHAR) };
        symbolTable.put("output", new MethodSymbol("output", SymbolTableEntry.Type.VOID, outputParams));
        // true
        symbolTable.put("true", new VariableSymbol("true", SymbolTableEntry.Type.BOOLEAN));
        // false
        symbolTable.put("false", new VariableSymbol("false", SymbolTableEntry.Type.BOOLEAN));

        return symbolTable;
    }
}
