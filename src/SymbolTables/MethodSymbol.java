package SymbolTables;

public class MethodSymbol extends SymbolTableEntry {
    public ParameterSymbol[] params;

    public MethodSymbol(String name, Type type, ParameterSymbol[] params) {
        super(name, type);
        this.params = params;
    }
}
