package SymbolTables;

public class ParameterSymbol extends SymbolTableEntry {

    public ParameterSymbol(String name, Type type, int arraySize) {
        super(name, type, arraySize);
    }

    public ParameterSymbol(String name, Type type) {
        super(name, type);
    }
}
