package SymbolTables;

public class MethodSymbol extends SymbolTableEntry {
    public ParameterSymbol[] params;

    // can't return array type
    public MethodSymbol(String name, Type returnType, ParameterSymbol[] params) {
        super(name, returnType);
        this.params = params;
    }
}
