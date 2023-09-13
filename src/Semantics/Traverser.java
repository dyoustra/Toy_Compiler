package Semantics;

import Parser.*;
import Scanner.Token;
import Scanner.ToyScanner;
import SymbolTables.MethodSymbol;
import SymbolTables.ParameterSymbol;
import SymbolTables.SymbolTable;
import SymbolTables.SymbolTableEntry;

public class Traverser {

    private final Node root;
    private final SymbolTable rootSymbolTable;

    public Traverser(Node root, SymbolTable rootSymbolTable) {
        this.root = root;
        this.rootSymbolTable = rootSymbolTable;
    }

    public void traverseAll() {
        traverse(this.root, this.rootSymbolTable);
        checkType(this.root);
    }

    private void traverse(Node current, SymbolTable symbolTable) {
        if (current == null) return;
        switch (current.kind) {
            case ARGUMENTS -> {
                Arguments arguments = (Arguments) current;
                for (Node node : arguments.children) {
                    traverse(node, symbolTable);
                }
            }
            case ARRAY_TYPE -> {
                ArrayType arrayType = (ArrayType) current;
                traverse(arrayType.baseType, symbolTable);
                traverse(arrayType.size, symbolTable);
            }
            case BINARY_OPERATOR -> {
                BinaryOperator binaryOperator = (BinaryOperator) current;
                traverse(binaryOperator.left, symbolTable);
                traverse(binaryOperator.right, symbolTable);
            }
            case BLOCK -> {
                Block block = (Block) current;
                for (Node statement : block.statements) {
                    traverse(statement, block.symbolTable);
                }
            }
            case CALL_STATEMENT -> {
                CallStatement callStatement = (CallStatement) current;
                traverse(callStatement.function, symbolTable);
                traverse(callStatement.arguments, symbolTable);
                while (callStatement.function.kind == Node.Kind.DOT) {
                    Dot dot = (Dot) callStatement.function;
                    callStatement.function = dot.right;
                }

                Identifier identifier = (Identifier) callStatement.function;
                SymbolTableEntry entry = symbolTable.get(((Identifier) callStatement.function).token.getValue());
                if (entry == null) throw new RuntimeException("Undefined Error: Identifier " + ((Identifier) callStatement.function).token.getValue() + " not found in symbol table.");
                else identifier.symbolTableEntry = entry;
            }
            case DOT -> {
                Dot dot = (Dot) current;
                traverse(dot.left, symbolTable);
                traverse(dot.right, symbolTable);
            }
            case IF_STATEMENT -> {
                IfStatement ifStatement = (IfStatement) current;
                traverse(ifStatement.condition, symbolTable);
                traverse(ifStatement.body, symbolTable);
                traverse(ifStatement.elseBody, symbolTable);
            }

            case LITERAL -> {
//                Literal literal = (Literal) current;
            }
            case METHOD_DECLARATION -> {
                MethodDeclaration methodDeclaration = (MethodDeclaration) current;
                traverse(methodDeclaration.returnType, symbolTable);
                traverse(methodDeclaration.name, symbolTable);
                for (Node parameter : methodDeclaration.parameters) {
                    traverse(parameter, symbolTable);
                }
                traverse(methodDeclaration.body, symbolTable);
            }
            case VARIABLE_DECLARATION -> {
                VariableDeclaration variableDeclaration = (VariableDeclaration) current;
                traverse(variableDeclaration.varType, symbolTable);
                traverse(variableDeclaration.name, symbolTable);
                traverse(variableDeclaration.value, symbolTable);
            }
            case IDENTIFIER -> {
                Identifier identifier = (Identifier) current;
                SymbolTableEntry entry = symbolTable.get(identifier.token.getValue());
                if (entry == null) throw new RuntimeException("Undefined Error: Identifier " + identifier.token.getValue() + " not found in symbol table.");
                else identifier.symbolTableEntry = entry;
            }
            case RETURN_STATEMENT -> {
                ReturnStatement returnStatement = (ReturnStatement) current;
                traverse(returnStatement.value, symbolTable);
                returnStatement.enclosingMethod = (MethodSymbol) symbolTable.get(symbolTable.getName());
            }
            case TYPE -> {
//                 Type varType = (Type) current;
            }
            case UNARY_OPERATOR -> {
                UnaryOperator unaryOperator = (UnaryOperator) current;
                traverse(unaryOperator.child, symbolTable);
            }
            case VARIABLE -> {
                Variable variable = (Variable) current;
                traverse(variable.name, symbolTable);
                traverse(variable.index, symbolTable);
            }
            case WHILE_STATEMENT -> {
                WhileStatement whileStatement = (WhileStatement) current;
                traverse(whileStatement.condition, symbolTable);
                traverse(whileStatement.body, symbolTable);
            }
        }
    }

    private void checkType(Node current) {
        if (current == null) return;
        switch (current.kind) {
            case ARGUMENTS -> {
                Arguments arguments = (Arguments) current;
                for (Node node : arguments.children) {
                    checkType(node);
                }
            }
            case ARRAY_TYPE -> {
                ArrayType arrayType = (ArrayType) current;
                checkType(arrayType.baseType);
                checkType(arrayType.size);

                if (arrayType.size.type != SymbolTableEntry.Type.INT) {
                    // No token, so no location info
                    throw new RuntimeException("Type Error: Array size must be of type int.");
                }
            }
            case BINARY_OPERATOR -> {
                BinaryOperator binaryOperator = (BinaryOperator) current;
                checkType(binaryOperator.left);
                checkType(binaryOperator.right);
                checkBinaryType(binaryOperator);
            }
            case BLOCK -> {
                Block block = (Block) current;
                for (Node statement : block.statements) {
                    checkType(statement);
                }
            }
            case CALL_STATEMENT -> {
                CallStatement callStatement = (CallStatement) current;
                checkType(callStatement.function);
                checkType(callStatement.arguments);
                checkCallType(callStatement);
            }
            case DOT -> {
                Dot dot = (Dot) current;
                checkType(dot.left);
                checkType(dot.right);
            }
            case IF_STATEMENT -> {
                IfStatement ifStatement = (IfStatement) current;
                checkType(ifStatement.condition);
                checkType(ifStatement.body);
                checkType(ifStatement.elseBody);
                checkConditionBoolean(ifStatement.condition);
            }

            case LITERAL -> {
                Literal literal = (Literal) current;
                if (literal.token.getType() == Token.TokenType.IDENTIFIER) {
                    // either true or false
                    literal.type = SymbolTableEntry.Type.BOOLEAN;
                }
                // not sure if this will work for all TokenTypes... String?
                // ANSWER: Strings just don't work. Add a String value to SymbolTableEntry.Type if you want to implement them.
                literal.type = SymbolTableEntry.Type.valueOf(literal.token.getType().name());
            }
            case METHOD_DECLARATION -> {
                MethodDeclaration methodDeclaration = (MethodDeclaration) current;
                checkType(methodDeclaration.returnType);
                checkType(methodDeclaration.name);
                for (Node parameter : methodDeclaration.parameters) {
                    checkType(parameter);
                }
                checkType(methodDeclaration.body);
            }
            case VARIABLE_DECLARATION -> {
                VariableDeclaration variableDeclaration = (VariableDeclaration) current;
                checkType(variableDeclaration.name);
                checkType(variableDeclaration.varType);
                checkType(variableDeclaration.value);
                if (variableDeclaration.varType.type != variableDeclaration.value.type) {
                    throw new RuntimeException("Type Error ( " + variableDeclaration.name.token + " ) : Type of variable declaration does not match type of value.");
                }
            }
            case IDENTIFIER -> {
                Identifier identifier = (Identifier) current;
                identifier.type = identifier.symbolTableEntry.type;
            }
            case RETURN_STATEMENT -> {
                ReturnStatement returnStatement = (ReturnStatement) current;
                checkType(returnStatement.value);
                checkReturnType(returnStatement);
            }
            case TYPE -> {
                 Type type = (Type) current;
                 type.type = SymbolTableEntry.Type.valueOf(type.token.getValue());
            }
            case UNARY_OPERATOR -> {
                UnaryOperator unaryOperator = (UnaryOperator) current;
                checkType(unaryOperator.child);
                checkUnaryType(unaryOperator);
            }
            case VARIABLE -> {
                Variable variable = (Variable) current;
                checkType(variable.name);
                checkType(variable.index);
            }
            case WHILE_STATEMENT -> {
                WhileStatement whileStatement = (WhileStatement) current;
                checkType(whileStatement.condition);
                checkType(whileStatement.body);
                checkConditionBoolean(whileStatement.condition);
            }
        }
    }

    private void checkConditionBoolean(Node condition) {
        if (condition.type != SymbolTableEntry.Type.BOOLEAN) {
            // No token for this error, so no location info
            throw new RuntimeException("Type Error: If statement condition must be of type boolean.");
        }
    }

    private void checkCallType(CallStatement callStatement) {
        while (callStatement.function.kind == Node.Kind.DOT) {
            Dot dot = (Dot) callStatement.function;
            callStatement.function = dot.right;
        }
        SymbolTableEntry stEntry = ((Identifier) callStatement.function).symbolTableEntry;

        int index = 0;
        for (ParameterSymbol param : ((MethodSymbol) stEntry).params) {
            if (param.type != callStatement.arguments.children.get(index).type) {
                throw new TypeError(((Identifier) callStatement.function).token,
                        "Parameter " + param.name + " in method " + stEntry.name + " is of type " + param.type +
                                " but was passed a value of type " + callStatement.arguments.children.get(index).type);
            }
            index++;
        }
        callStatement.type = stEntry.type;
    }

    private void checkUnaryType(UnaryOperator unary) {
        switch(ToyScanner.State.valueOf(unary.token.getValue())) {
            case EXCLAMATION -> {
                if (unary.child.type == SymbolTableEntry.Type.BOOLEAN) unary.type = SymbolTableEntry.Type.BOOLEAN;
                else throw new TypeError(unary.token, "! operator can only be applied to boolean values.");
            }
            case PLUS, MINUS, PLUSPLUS, MINUSMINUS -> {
                if (unary.child.type == SymbolTableEntry.Type.INT) unary.type = SymbolTableEntry.Type.INT;
                else throw new TypeError(unary.token, "+, -, ++, and -- operators can only be applied to integer values.");
            }
            default -> throw new TypeError(unary.token, "Invalid unary operator.");
        }
    }

    private void checkBinaryType(BinaryOperator binary) {
        switch (ToyScanner.State.valueOf(binary.token.getValue())) {
            // ASSIGN
            case EQUAL, PLUSEQUAL, MINUSEQUAL, STAREQUAL, SLASHEQUAL, PERCENTEQUAL, AMPERSANDEQUAL, CARETEQUAL, PIPEEQUAL  -> {
                if (binary.left.type == binary.right.type) binary.type = binary.right.type;
                else throw new TypeError(binary.token, "Cannot perform operation on literals of different types.");
            }
            // COMPARE
            case EQUALEQUAL, NOTEQUAL, LESSTHAN, GREATERTHAN, LESSEQUAL, GREATEREQUAL -> {
                if (binary.left.type == binary.right.type) binary.type = SymbolTableEntry.Type.BOOLEAN;
                else throw new TypeError(binary.token, "Cannot perform operation on literals of different types.");
            }
            // ARITHMETIC
            case PLUS, MINUS, STAR, SLASH, PERCENT -> {
                if (binary.left.type == SymbolTableEntry.Type.INT &&
                    binary.right.type == SymbolTableEntry.Type.INT)
                        binary.type = SymbolTableEntry.Type.INT;
                else throw new TypeError(binary.token, "Cannot perform operation on literals of non-integer types.");
            }
            // BITWISE (might want to implement int)
            case AMPERSAND, AMPERSANDAMPERSAND, PIPE, PIPEPIPE, CARET  -> {
                if (binary.left.type == SymbolTableEntry.Type.BOOLEAN &&
                    binary.right.type == SymbolTableEntry.Type.BOOLEAN)
                        binary.type = SymbolTableEntry.Type.BOOLEAN;
                else throw new TypeError(binary.token, "Cannot perform operation on literals of non-boolean types.");
            }
        }
    }

    private void checkReturnType(ReturnStatement returnStatement) {
        if (returnStatement.value.type != returnStatement.enclosingMethod.type) {
            throw new RuntimeException("Return Type Error for method \"" + returnStatement.enclosingMethod.name + "\": Return type does not match method return type.");
        }
        returnStatement.type = returnStatement.value.type;
    }

    private static class TypeError extends RuntimeException {
        public TypeError(Token token, String message) {
            super("Type Error (" + token + "): " + message);
        }
    }
}