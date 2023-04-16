// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

package Parser;

import SymbolTables.*;
import Scanner.*;

import java.util.ArrayList;

public class Parser {

    private record TokenArrayList(ArrayList<Token> tokens) {
        public Token get(int index) {
            if (index < tokens.size()) {
                return tokens.get(index);
            } else {
                return new ErrorToken(-1, -1);
            }
        }
    }

    private final TokenArrayList tokens;
    private int index;

    private SymbolTable currentSymbolTable;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = new TokenArrayList(tokens);
        this.index = 0;
        this.currentSymbolTable = SymbolTable.globalSymbolTable();
    }

    public SymbolTable getSymbolTable() {
        return currentSymbolTable;
    }

    public Node parse() {
        return program();
    }

    private Node program() { // good
        expecting(KeywordToken.Keyword.PROGRAM.name());
        Identifier name = new Identifier(expectingIdentifier());
        return block(name.token.getValue());
    }

    private Node block(String name) { // good
        if (optional(ToyScanner.State.LBRACE.name())) {
            // create new symbol table
            if (currentSymbolTable == null) {
                currentSymbolTable = new SymbolTable(name, null);
                System.err.println("Parser: this shouldn't happen");
            } else {
                currentSymbolTable = new SymbolTable(name, currentSymbolTable);
            }
            ArrayList<Node> statements = statements();
            expecting(ToyScanner.State.RBRACE.name());
            currentSymbolTable.print();
            Block block = new Block(statements, currentSymbolTable);
            currentSymbolTable = currentSymbolTable.getParent(); // will return self if null
            return block;
        }
        return null;
    }

    private ArrayList<Node> statements() { // good
        ArrayList<Node> statements = new ArrayList<>();
        statements.add(statement());
        statementsPrime(statements);
        return statements;
    }

    private ArrayList<Node> statementsPrime(ArrayList<Node> statements) { // good
        Node statement = statement();
        while (statement != null) {
            statements.add(statement);
            statement = statement();
        }
        return statements;
    }

    private Node statement() {
        Node declaration = declaration();
        if (declaration != null) {
            return declaration;
        }
        Node assignment = assignment();
        if (assignment != null) {
            return assignment;
        }
        Node ifStatement = ifStatement();
        if (ifStatement != null) {
            return ifStatement;
        }
        Node whileStatement = whileStatement();
        if (whileStatement != null) {
            return whileStatement;
        }
        Node returnStatement = returnStatement();
        if (returnStatement != null) {
            return returnStatement;
        }
        Node callStatement = callStatement();
        if (callStatement != null) {
            return callStatement;
        }
        Node block = block(null);
        if (block != null) {
            return block;
        }
        return null;
    }

    private Node declaration() { // good
        Node type = variableType();
        if (type != null) {
            Identifier name = new Identifier(expectingIdentifier());
            if (optional(ToyScanner.State.LPAREN.name())) { // method declaration
                if (type instanceof ArrayType) {
                    throw new RuntimeException("Cannot declare a method with an array type");
                }
                ArrayList<VariableDeclaration> parameters = parameters(); // can be null
                expecting(ToyScanner.State.RPAREN.name());
                Node body = block(name.token.getValue());
                newMethodSymbol((Type) type, name, parameters);
                return new MethodDeclaration(type, name, parameters, body);
            }
            else { // variable declaration
                if (optional(ToyScanner.State.EQUAL.name())) {
                    Node expression = expression();
                    expecting(ToyScanner.State.SEMICOLON.name());
                    newVariableSymbol(type, name);
                    return new VariableDeclaration(type, name, expression);
                } else {
                    newVariableSymbol(type, name);
                    return new VariableDeclaration(type, name); // variable declared but not initialized
                }
            }
        }
        return null;
    }

    private Node assignment() {
        int indexBefore = index;
        Node variable = variable(); // TODO could increment size then return null... check
        if (variable != null) {
            BinaryOperator assignOp = assignOp();
            if (assignOp != null) {
                assignOp.left = variable;
                assignOp.right = expression();
                expecting(ToyScanner.State.SEMICOLON.name());
                return assignOp;
            }
        }
        index = indexBefore;
        return null;
    }

    private Node ifStatement() { // good
        if (optional(KeywordToken.Keyword.IF.name())) {
            expecting(ToyScanner.State.LPAREN.name());
            Node condition = booleanExpression();
            expecting(ToyScanner.State.RPAREN.name());
            Node statement = statement();
            if (optional(KeywordToken.Keyword.ELSE.name())) {
                Node elseStatement = statement();
                return new IfStatement(condition, statement, elseStatement);
            } else return new IfStatement(condition, statement, null);
        }
        else return null;
    }

    private Node whileStatement() { // good
        if (optional(KeywordToken.Keyword.WHILE.name())) {
            expecting(ToyScanner.State.LPAREN.name());
            Node condition = booleanExpression();
            expecting(ToyScanner.State.RPAREN.name());
            Node body = statement();
            return new WhileStatement(condition, body);
        }
        else return null;
    }

    private Node returnStatement() {
        if (optional(KeywordToken.Keyword.RETURN.name())) {
            Node expression = expression(); // can be null for empty return statement
            expecting(ToyScanner.State.SEMICOLON.name());
            return new ReturnStatement(expression);
        }
        else return null;
    }

    private Node callStatement() { // good
        Node call = call();
        if (call != null) {
            expecting(ToyScanner.State.SEMICOLON.name());
            return call;
        }
        else return null;
    }

    private Node call() { // good
        Node name = name();
        if (name != null) {
            expecting(ToyScanner.State.LPAREN.name());
            Arguments arguments = arguments();
            expecting(ToyScanner.State.RPAREN.name());
            return new CallStatement(name, arguments);
        }
        else return null;
    }

    private ArrayList<VariableDeclaration> parameters() { // there is no parametersPrime
        ArrayList<VariableDeclaration> parameters = new ArrayList<>();
        do {
            parameters.add(parameter());
        } while (optional(ToyScanner.State.COMMA.name()));
        return parameters;
    }

    private VariableDeclaration parameter() {
        Node type = parameterType();
        if (type != null) {
            Identifier name = new Identifier(expectingIdentifier());
            return new VariableDeclaration(type, name);
        }
        return null;
    }

    private Node variableType() {
        Type scalarType = scalarType();
        if (optional(ToyScanner.State.LBRACKET.name())) {
            Node constantExpression = constantExpression();
            expecting(ToyScanner.State.RBRACKET.name());
            return new ArrayType(scalarType, constantExpression);
        }
        return scalarType;
    }

    private Type scalarType() {
        if (optional(KeywordToken.Keyword.INT.name()) ||
            optional(KeywordToken.Keyword.CHAR.name()) ||
            optional(KeywordToken.Keyword.BOOLEAN.name()) ||
            optional(KeywordToken.Keyword.VOID.name())
        ) {
            return new Type(tokens.get(index - 1));
        }
        return null;
    }

    private Node parameterType() {
        Type scalarType = scalarType();
        if (optional(ToyScanner.State.LBRACKET.name())) {
            expecting(ToyScanner.State.RBRACKET.name());
            return new ArrayType(scalarType, null);
        }
        return scalarType;
    }

    private Node booleanExpression() {
        return expression();
    }

    private Node constantExpression() {
        return expression();
    }

    private Node expression() {
        return expressionPrime(disjunction());
    }

    private Node disjunction() {
        return disjunctionPrime(conjunction());
    }

    private Node expressionPrime(Node left) {
        if (tokens.get(index).getValue().equals(ToyScanner.State.EQUAL.name())) { // "="
            return new BinaryOperator(tokens.get(index++), left, expressionPrime(disjunction()));
        }
        else return left; // can be null
    }

    private Node conjunction() {
        return conjunctionPrime(relation());
    }

    private Node disjunctionPrime(Node left) {
        BinaryOperator orOp = orOp();
        if (orOp != null) {
            orOp.left = left;
            orOp.right = disjunctionPrime(conjunction());
            return orOp;
        }
        else return left; // can be null
    }

    private Node relation() {
        return relationPrime(simpleExpression());
    }

    private Node relationPrime(Node left) {
        BinaryOperator compareOp = compareOp();
        if (compareOp != null) {
            compareOp.left = left;
            compareOp.right = simpleExpression();
            return compareOp;
        }
        else return left;
    }
    private Node conjunctionPrime(Node left) {
        BinaryOperator andOp = andOp();
        if (andOp != null) {
            andOp.left = left;
            andOp.right = conjunctionPrime(relation());
            return andOp;
        }
        return left; // can be null
    }

    private Node simpleExpression() {
        UnaryOperator sign = sign();  // sign is optional
        if (sign != null) {
            sign.child = simpleExpressionPrime(term());
            return sign;
        }
        else return simpleExpressionPrime(term());
    }

    private Node term() {
        return termPrime(factor());
    }

    private Node simpleExpressionPrime(Node left) {
        BinaryOperator addOp = addOp();
        if (addOp != null) {
            addOp.left = left;
            addOp.right = simpleExpressionPrime(term());
            return addOp;
        }
        return left; // can be null
    }

    private Node factor() {
//        Parser.Node name = name();
//        if (name != null) return name;
        Node primary = primary(); // checks for (expression)
        if (primary != null){
            return primary;
        }
        UnaryOperator unary = unaryOp();
        if (unary != null) {
            unary.child = factor();
            return unary;
        }
        UnaryOperator prefix = prefixOp();
        if (prefix != null) {
            prefix.child = variable();
            return prefix;
        }
        Node variable = variable();
        if (variable != null) {
            UnaryOperator postfix = postfixOp();
            postfix.child = variable;
            return postfix;
        }
        return null;
    }

    private Node primary() {
        Node literal = literal(); // this must go before callStatement to avoid "true" or "false" being treated as a name (because they are identifiers)
        if (literal != null) return literal;
        int initialIndex = index;
        Node variable = variable();
        if (variable != null) {
            if (optional(ToyScanner.State.LPAREN.name())) { // "("
                index = initialIndex; // needed because both variable and callstatement start with name
            }
            else {
                UnaryOperator postfix = postfixOp();
                if (postfix != null) {
                    postfix.child = variable;
                    return postfix;
                } else return variable;
            }
        }
        Node call = call();
        if (call != null) return call;
        if (optional(ToyScanner.State.LPAREN.name())) { // "("
            Node expression = expression();
            expecting(ToyScanner.State.RPAREN.name()); // ")"
            return expression;
        }
        return null;
    }

    private Node termPrime(Node left) {
        BinaryOperator mulOp = mulOp();
        if (mulOp != null) {
            mulOp.left = left;
            mulOp.right = termPrime(factor());
            return mulOp;
        }
        return left; // can be null
    }

    private Node variable() {
        Node name = name();
        if (name != null) {
            if (optional(ToyScanner.State.LBRACKET.name())) { // "["
                Node expression = expression();
                expecting(ToyScanner.State.RBRACKET.name()); // "]"
                return new Variable(name, expression);
            } else return new Variable(name);
        }
        return null;
    }

    private Arguments arguments() {
        Arguments args = new Arguments();
        args.children.add(expression()); // argument = expression
        return argumentsPrime(args);
    }

    private Arguments argumentsPrime(Arguments args) {
        if (tokens.get(index).getValue().equals(ToyScanner.State.COMMA.name())) { // ","
            index++;
            args.children.add(expression()); // argument = expression
            return argumentsPrime(args);
        }
        return args; // can be null
    }


    // CHECK FOR TERMINAL METHODS -- WILL NOT INCREMENT INDEX ON FALSE

    private BinaryOperator compareOp() {
        String operator = tokens.get(index).getValue();
        if (operator.equals(ToyScanner.State.EQUALEQUAL.name()) ||
            operator.equals(ToyScanner.State.EXCLAMATION.name()) ||
            operator.equals(ToyScanner.State.LESSTHAN.name()) ||
            operator.equals(ToyScanner.State.GREATERTHAN.name()) ||
            operator.equals(ToyScanner.State.LESSEQUAL.name()) ||
            operator.equals(ToyScanner.State.GREATEREQUAL.name())
        ) { // "==", "!=", "<", ">", "<=", ">="
            return new BinaryOperator(tokens.get(index++), null, null);
        } else return null;
    }

    private BinaryOperator assignOp() {
        String operator = tokens.get(index).getValue();
        if (operator.equals(ToyScanner.State.EQUAL.name()) ||
            operator.equals(ToyScanner.State.PLUSEQUAL.name()) ||
            operator.equals(ToyScanner.State.MINUSEQUAL.name()) ||
            operator.equals(ToyScanner.State.STAREQUAL.name()) ||
            operator.equals(ToyScanner.State.SLASHEQUAL.name()) ||
            operator.equals(ToyScanner.State.PERCENTEQUAL.name()) ||
            operator.equals(ToyScanner.State.AMPERSANDEQUAL.name()) ||
            operator.equals(ToyScanner.State.CARETEQUAL.name()) ||
            operator.equals(ToyScanner.State.PIPEEQUAL.name())
        ) { // "=", "+=", "-=", "*=", "/=", "%=", "&=", "^=", "|="
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private UnaryOperator unaryOp() {
        if (tokens.get(index).getValue().equals(ToyScanner.State.EXCLAMATION.name())) { // "!"
            return new UnaryOperator(tokens.get(index++), null);
        }
        else return null;
    }

    private UnaryOperator prefixOp() {
        String type = tokens.get(index).getValue();
        if (type.equals(ToyScanner.State.PLUSPLUS.name()) ||
            type.equals(ToyScanner.State.MINUSMINUS.name())
        ) { // "++", "--"
            return new UnaryOperator(tokens.get(index++), null, true);
        }
        else return null;
    }

    private UnaryOperator postfixOp() {
        String type = tokens.get(index).getValue();
        if (type.equals(ToyScanner.State.PLUSPLUS.name()) ||
            type.equals(ToyScanner.State.MINUSMINUS.name())
        ) { // "++", "--"
            return new UnaryOperator(tokens.get(index++), null, false);
        }
        else return null;
    }

    private BinaryOperator andOp() {
        String type = tokens.get(index).getValue();
        if (type.equals(ToyScanner.State.AMPERSAND.name()) ||
            type.equals(ToyScanner.State.AMPERSANDAMPERSAND.name())
        ) { // "&", "&&"
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private BinaryOperator orOp() {
        String type = tokens.get(index).getValue();
        if (type.equals(ToyScanner.State.PIPE.name()) ||
            type.equals(ToyScanner.State.PIPEPIPE.name()) ||
            type.equals(ToyScanner.State.CARET.name())
        ) { // "|", "||", "^"
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private BinaryOperator mulOp() {
        String type = tokens.get(index).getValue();
        if (type.equals(ToyScanner.State.STAR.name()) ||
            type.equals(ToyScanner.State.SLASH.name()) ||
            type.equals(ToyScanner.State.PERCENT.name())
        ) { // "*", "/", "%"
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private BinaryOperator addOp() {
        String type = tokens.get(index).getValue();
        if (type.equals(ToyScanner.State.PLUS.name()) ||
            type.equals(ToyScanner.State.MINUS.name())
        ) { // "+", "-"
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private UnaryOperator sign() {
        String type = tokens.get(index).getValue();
        if (type.equals(ToyScanner.State.PLUS.name()) ||
            type.equals(ToyScanner.State.MINUS.name())
        ) { // "+", "-"
            return new UnaryOperator(tokens.get(index++), null);
        }
        else return null;
    }

    private Literal number() {
        if (tokens.get(index).getType().equals(Token.TokenType.NUMBER)) { // number
            return new Literal(tokens.get(index++));
        }
        return null;
    }

    private Literal literal() {
        Literal number = number();
        if (number != null) return number;
        Token.TokenType type = tokens.get(index).getType();
        if (type.equals(Token.TokenType.STRING) ||
            type.equals(Token.TokenType.CHARACTER)
        ) { // string, character
            return new Literal(tokens.get(index++));
        }
        else if (type.equals(Token.TokenType.IDENTIFIER)) { // check if it's a true | false keyword
            if (tokens.get(index).getValue().equals("true") ||
                tokens.get(index).getValue().equals("false")) {
                return new Literal(tokens.get(index++));
            }
        }
        return null;
    }

    private Node name() {
        IdentifierToken identifierToken = optionalIdentifier();
        if (identifierToken != null) { // identifier
            return namePrime(new Identifier(identifierToken));
        }
        return null;
    }

    private Node namePrime(Identifier left) {
        if (tokens.get(index).getValue().equals(ToyScanner.State.DOT.name())) { // "."
            index++;
            Dot dot = new Dot();
            dot.left = left;
            dot.right = namePrime(new Identifier(optionalIdentifier()));
            return dot;
        }
        return left;
    }

    // SYMBOL TABLE ENTRY METHODS

    private void newMethodSymbol(Type type, Identifier name, ArrayList<VariableDeclaration> parameters) {
        String stName = name.token.getValue();
        SymbolTableEntry.Type stType = new SymbolTableEntry.Type(SymbolTableEntry.Type.Kind.valueOf(type.token.getValue()));
        ParameterSymbol[] parameterSymbols = new ParameterSymbol[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            VariableDeclaration parameter = parameters.get(i);
            String paramName = parameter.name.token.getValue();
            SymbolTableEntry.Type paramType;
            if (parameter.type instanceof ArrayType arrayType) {
                paramType = new SymbolTableEntry.ArrayType(SymbolTableEntry.Type.Kind.valueOf((arrayType.baseType).token.getValue()), arrayType.evaluateSize());
            } else {
                paramType = new SymbolTableEntry.Type(SymbolTableEntry.Type.Kind.valueOf(((Type) parameter.type).token.getValue()));
            }
            parameterSymbols[i] = new ParameterSymbol(paramName, paramType);
        }
        currentSymbolTable.put(stName, new MethodSymbol(stName, stType, parameterSymbols));
    }

    private void newVariableSymbol(Node type, Identifier name) {
        String stName = name.token.getValue();
        if (type instanceof ArrayType arrayType) {
            SymbolTableEntry.Type stType = new SymbolTableEntry.ArrayType(SymbolTableEntry.Type.Kind.valueOf(arrayType.baseType.token.getValue()), arrayType.evaluateSize());
            currentSymbolTable.put(stName, new VariableSymbol(stName, stType));
        } else if (type instanceof Type newType) {
            SymbolTableEntry.Type stType = new SymbolTableEntry.Type(SymbolTableEntry.Type.Kind.valueOf(newType.token.getValue()));
            currentSymbolTable.put(stName, new VariableSymbol(stName, stType));
        }
    }

    // EXCEPTION METHODS

    private IdentifierToken expectingIdentifier() {
        if (tokens.get(index).getType().equals(Token.TokenType.IDENTIFIER)) {
            return (IdentifierToken) tokens.get(index++);
        } else throw new ErrorExpected(Token.TokenType.IDENTIFIER.name(), tokens.get(index));
    }

    private IdentifierToken optionalIdentifier() {
        if (tokens.get(index).getType().equals(Token.TokenType.IDENTIFIER)) {
            return (IdentifierToken) tokens.get(index++);
        } else return null;
    }

    private Token expecting(String expected) {
        if (tokens.get(index).getValue().equals(expected)) {
            return tokens.get(index++);
        } else throw new ErrorExpected(expected, tokens.get(index));
    }

    private boolean optional(String optional) {
        if (tokens.get(index).getValue().equals(optional)) {
            index++;
            return true;
        }
        return false;
    }

    private static class ErrorExpected extends RuntimeException {
        public ErrorExpected(String expected, Token found) {
            super("Expected \"" + expected + "\" @ line " + (found.getRow() + 1) + ", column " + (found.getCol() + 1) + ", found " + found);
        }
    }
}
