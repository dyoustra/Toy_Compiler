package SymbolTables;

public abstract class SymbolTableEntry {

    public String name;
    public Type type; // either var varType or return varType
    public int arraySize; // either 0 or size of array

    public SymbolTableEntry(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public SymbolTableEntry(String name, Type type, int arraySize) {
        this.name = name;
        this.type = type;
        this.arraySize = arraySize;
    }

    public enum Type {
        INT, CHAR, BOOLEAN, VOID, INT_ARRAY, CHAR_ARRAY, BOOLEAN_ARRAY;

        public boolean isArray() {
            return switch (this) {
                case INT_ARRAY, CHAR_ARRAY, BOOLEAN_ARRAY -> true;
                default -> false;
            };
        }
    }

}
