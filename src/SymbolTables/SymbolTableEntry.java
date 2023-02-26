package SymbolTables;

public abstract class SymbolTableEntry {

    public String name;
    public Type type; // either var type or return type

    public SymbolTableEntry(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public static class Type {
        public static enum Kind { INT, CHAR, BOOL, VOID}
        Kind kind;
    }

    public static class ArrayType extends Type {
        int size;

        public ArrayType(Kind kind, int size) {
            super(kind);
            this.size = size;
        }
    }

}
