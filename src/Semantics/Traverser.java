package Semantics;

import Parser.*;
import Scanner.IdentifierToken;
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
                    traverse(statement, symbolTable);
                }
            }
            case CALL_STATEMENT -> {
                CallStatement callStatement = (CallStatement) current;
                traverse(callStatement.function, symbolTable);
                traverse(callStatement.arguments, symbolTable);
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
                traverse(variableDeclaration.type, symbolTable);
                traverse(variableDeclaration.name, symbolTable);
                traverse(variableDeclaration.value, symbolTable);
            }
            case IDENTIFIER -> {
                Identifier identifier = (Identifier) current;
                SymbolTableEntry entry = symbolTable.get(identifier.token.getValue());
                if (entry == null)
                    throw new RuntimeException("Undefined Error: Identifier " + identifier.token.getValue() + " not found in symbol table.");
                else identifier.symbolTableEntry = entry;
            }
            case RETURN_STATEMENT -> {
                ReturnStatement returnStatement = (ReturnStatement) current;
                traverse(returnStatement.value, symbolTable);
            }
            case TYPE -> {
//                 Type type = (Type) current;
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
}