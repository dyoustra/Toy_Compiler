package SymbolTables;

public class Method extends SymbolTableEntry {
    private Parameter[] params;

    public Method(String name, Type type, Parameter[] params) {
        super(name, type);
        this.params = params;
    }
}
