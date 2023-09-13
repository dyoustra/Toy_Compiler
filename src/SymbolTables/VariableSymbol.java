package SymbolTables;

public class VariableSymbol extends SymbolTableEntry {

    public VariableSymbol(String name, Type type, int arraySize) {
        super(name, type, arraySize);
    }

    public VariableSymbol(String name, Type type) {
        super(name, type);
    }
}
