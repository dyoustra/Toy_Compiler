package SymbolTables;

public abstract class SymbolTableEntry {

    public String name;
    public Type type; // either var type or return type

    public SymbolTableEntry(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public static class Type {
        public static enum Kind { INT, CHAR, BOOLEAN, VOID }
        Kind kind;

        public Type(Kind kind) {
            this.kind = kind;
        }
    }

    public static class ArrayType extends Type {
        Integer size;

        public ArrayType(Kind kind, Integer size) {
            super(kind);
            this.size = size;
        }
    }

}
