package SymbolTables;
import 
public class Variable extends SymbolTableEntry {

    public Variable(String name, ArrayType type) {
        super(name, type);
    }

    public Variable(String name, Type type) { // non-array types
        super(name, type);
    }
}
