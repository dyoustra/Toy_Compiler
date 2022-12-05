// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    private static class TokenArrayList {
        private ArrayList<Token> tokens;

        public TokenArrayList(ArrayList<Token> tokens) {
            this.tokens = tokens;
        }

        public Token get(int index) {
            if (index < tokens.size()) {
                return tokens.get(index);
            } else {
                return new Token("NULL", -1, -1);
            }
        }
    }

    private TokenArrayList tokens;
    private int index;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = new TokenArrayList(tokens);
        this.index = 0;
    }

    private Node parseExpression() {
        return expressionPrime(disjunction());
    }

    private Node disjunction() {
        return disjunctionPrime(conjunction());
    }

    private Node expressionPrime(Node left) {
        if (tokens.get(index).getType().equals(ToyScanner.State.EQUAL.name())) { // "="
            return new BinaryOperator(tokens.get(index), left, expressionPrime(disjunction()));
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
        Name name = name();
        if (name != null) return name; // need to add factorSuffix
        Literal literal = literal();
        if (literal() != null) return literal();
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
        else if (tokens.get(index).getType().equals(ToyScanner.State.LPAREN.name())) { // "("
            index++;
            Node expression = parseExpression();
            if (expression != null) {
                if (tokens.get(index).getType().equals(ToyScanner.State.RPAREN.name())) { // ")"
                    index++;
                    expression.parens = true;
                    return expression;
                }
            }
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

    private Node factorSuffix() {
        if (tokens.get(index).getType().equals(ToyScanner.State.LPAREN.name())) { // "("
            index++;
            int indexBefore = index;
            Arguments arguments = arguments();
            if (arguments.children.size() == 0) {
                index = indexBefore;
                // if true, then nothing happens but index is incremented to account for the arguments
            }
            if (tokens.get(index).getType().equals(ToyScanner.State.RPAREN.name())) { // ")"
                index++;
                arguments.parens = true;
                return arguments;
            } else return null;
        }

        else if (tokens.get(index).getType().equals(ToyScanner.State.LBRACKET.name())) { // "["
            index++;
            Node expression = parseExpression();
            if (expression != null) {
                if (tokens.get(index).getType().equals(ToyScanner.State.RBRACKET.name())) { // "]"
                    index++;
                    postfixOp(); // optional
                    expression.brackets = true;
                    return expression;
                }
            }
            errorExpected(']');
            return null;
        }
        return null; // can be null
    }

    private Node variable() {
        Node name = name();
        if (name != null) {
            if (tokens.get(index).getType().equals(ToyScanner.State.LBRACKET.name())) { // "["
                index++;
                Node expression = parseExpression();
                if (expression != null) {
                    if (tokens.get(index).getType().equals(ToyScanner.State.RBRACKET.name())) { // "]"
                        index++;
                        expression.brackets = true;
                        return new Variable(name, expression);
                    }
                }
            } else return new Variable(name);
        }
        return null;
    }

    private Arguments arguments() {
        Arguments args = new Arguments();
        args.children.add(parseExpression()); // argument = expression
        return argumentsPrime(args);
    }

    private Arguments argumentsPrime(Arguments args) {
        if (tokens.get(index).getType().equals(ToyScanner.State.COMMA.name())) { // ","
            index++;
            args.children.add(parseExpression()); // argument = expression
            return argumentsPrime(args);
        }
        return args; // can be null
    }


    // CHECK FOR TERMINAL METHODS -- WILL NOT INCREMENT INDEX ON FALSE

    private BinaryOperator compareOp() {
        String type = tokens.get(index).getType();
        if (type.equals(ToyScanner.State.EQUAL.name()) ||
            type.equals(ToyScanner.State.EXCLAMATION.name()) ||
            type.equals(ToyScanner.State.LESSTHAN.name()) ||
            type.equals(ToyScanner.State.GREATERTHAN.name()) ||
            type.equals(ToyScanner.State.LESSEQUAL.name()) ||
            type.equals(ToyScanner.State.GREATEREQUAL.name())
        ) { // "==", "!=", "<", ">", "<=", ">="
            return new BinaryOperator(tokens.get(index++), null, null);
        } else return null;
    }

    private BinaryOperator assignOp() {
        String type = tokens.get(index).getType();
        if (tokens.get(index).getType().equals(ToyScanner.State.EQUAL.name()) ||
            tokens.get(index).getType().equals(ToyScanner.State.PLUSEQUAL.name()) ||
            tokens.get(index).getType().equals(ToyScanner.State.MINUSEQUAL.name()) ||
            tokens.get(index).getType().equals(ToyScanner.State.STAREQUAL.name()) ||
            tokens.get(index).getType().equals(ToyScanner.State.SLASHEQUAL.name()) ||
            tokens.get(index).getType().equals(ToyScanner.State.PERCENTEQUAL.name()) ||
            tokens.get(index).getType().equals(ToyScanner.State.AMPERSANDEQUAL.name()) ||
            tokens.get(index).getType().equals(ToyScanner.State.CARETEQUAL.name()) ||
            tokens.get(index).getType().equals(ToyScanner.State.PIPEEQUAL.name())
        ) { // "=", "+=", "-=", "*=", "/=", "%=", "&=", "^=", "|="
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private UnaryOperator unaryOp() {
        if (tokens.get(index).getType().equals(ToyScanner.State.EXCLAMATION.name())) { // "!"
            return new UnaryOperator(tokens.get(index++), null);
        }
        else return null;
    }

    private UnaryOperator prefixOp() {
        String type = tokens.get(index).getType();
        if (type.equals(ToyScanner.State.PLUSPLUS.name()) ||
            type.equals(ToyScanner.State.MINUSMINUS.name())
        ) { // "++", "--"
            return new UnaryOperator(tokens.get(index++), null, true);
        }
        else return null;
    }

    private UnaryOperator postfixOp() {
        String type = tokens.get(index).getType();
        if (type.equals(ToyScanner.State.PLUSPLUS.name()) ||
                type.equals(ToyScanner.State.MINUSMINUS.name())
        ) { // "++", "--"
            return new UnaryOperator(tokens.get(index++), null, false);
        }
        else return null;
    }

    private BinaryOperator andOp() {
        String type = tokens.get(index).getType();
        if (type.equals(ToyScanner.State.AMPERSAND.name()) ||
            type.equals(ToyScanner.State.AMPERSANDAMPERSAND.name())
        ) { // "&", "&&"
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private BinaryOperator orOp() {
        String type = tokens.get(index).getType();
        if (type.equals(ToyScanner.State.PIPE.name()) ||
            type.equals(ToyScanner.State.PIPEPIPE.name()) ||
            type.equals(ToyScanner.State.CARET.name())
        ) { // "|", "||", "^"
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private BinaryOperator mulOp() {
        String type = tokens.get(index).getType();
        if (type.equals(ToyScanner.State.STAR.name()) ||
            type.equals(ToyScanner.State.SLASH.name()) ||
            type.equals(ToyScanner.State.PERCENT.name())
        ) { // "*", "/", "%"
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private BinaryOperator addOp() {
        String type = tokens.get(index).getType();
        if (type.equals(ToyScanner.State.PLUS.name()) ||
            type.equals(ToyScanner.State.MINUS.name())
        ) { // "+", "-"
            return new BinaryOperator(tokens.get(index++), null, null);
        }
        else return null;
    }

    private UnaryOperator sign() {
        String type = tokens.get(index).getType();
        if (type.equals(ToyScanner.State.PLUS.name()) ||
                type.equals(ToyScanner.State.MINUS.name())
        ) { // "+", "-"
            return new UnaryOperator(tokens.get(index++), null);
        }
        else return null;
    }

    private Literal number() {
        if (tokens.get(index).getType().equals(Token.TokenType.NUMBER.name())) { // number
            return new Literal(tokens.get(index++));
        }
        return null;
    }

    private Literal literal() {
        Literal number = number();
        if (number != null) return number;
        String type = tokens.get(index).getType();
        if (type.equals(Token.TokenType.STRING.name()) ||
            type.equals(Token.TokenType.CHARACTER.name())
        ) { // string, character
            return new Literal(tokens.get(index++));
        }
        else if (type.equals(Token.TokenType.IDENTIFIER.name())) { // check if it's a true | false keyword
            if (((Identifier) tokens.get(index)).getValue().equals("true") ||
                ((Identifier) tokens.get(index)).getValue().equals("false")) {
                return new Literal(tokens.get(index++));
            }
        }
        return null;
    }

    private Node name() {
        if (tokens.get(index).getType().equals(Token.TokenType.IDENTIFIER.name())) { // identifier
            Name name = new Name(((Identifier) tokens.get(index++)).getValue());
            name.value += namePrime().value;
            return name;
        }
        return null;
    }

    private Node namePrime(Node left) {
        if (tokens.get(index).getType().equals(ToyScanner.State.DOT.name())) { // "."
            index++;
            Dot dot = new Dot();
            dot.left = left;
            dot.right = namePrime();
        }
        return new Name("");


        if (mulOp != null) {
            mulOp.left = left;
            mulOp.right = termPrime(factor());
            return mulOp;
        }
        return left; // can be null
    }

    // EXCEPTION METHODS

    private void errorExpected(char c) {
        throw new RuntimeException("Expected '" + c + "' at line " + (tokens.get(index).getRow() + 1) + ", column " + (tokens.get(index).getCol() + 1));
    }

    // MAIN

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter expression: ");
        if (scanner.hasNextLine()) System.out.println();
        Token token = null;
        ArrayList<Token> tokens = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String current = scanner.nextLine();
            while (ToyScanner.startCol < current.length()) {
                token = ToyScanner.getNextToken(current.substring(ToyScanner.startCol));
                if (token != null) tokens.add(token);
            }
            Parser parser = new Parser(tokens);
            if (parser.parseExpression() != null) {
                System.out.println("OK");
            } else {
                System.out.println("ERROR" + " at row:" + ToyScanner.startRow + " col:" + parser.index);
            }
            ToyScanner.startCol = 0;
            ToyScanner.startRow++;
            tokens.clear();
        }
    }
}
