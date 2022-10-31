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

    private boolean parseExpression() {
        return disjunction() && expressionPrime();
    }

    private boolean disjunction() {
        return conjunction() && disjunctionPrime();
    }

    private boolean expressionPrime() {
        if (tokens.get(index).getType().equals(ToyScanner.State.EQUAL.name())) { // "="
            index++; // optional
        }
        int indexBefore = index;
        if (disjunction()) return expressionPrime();
        index = indexBefore;
        return true; // can be null
    }

    private boolean conjunction() {
        return relation() && conjunctionPrime();
    }

    private boolean disjunctionPrime() {
        if (orOp()) {
            return conjunction() && disjunctionPrime();
        }
        else return true; // can be null
    }

    private boolean relation() {
        if (simpleExpression()) {
            int indexBefore = index;
            if (compareOp() && simpleExpression()) {
                return true;
            }
            index = indexBefore;
            return true;
        }
        return false;
    }

    private boolean conjunctionPrime() {
        if (andOp()) {
            return relation() && conjunctionPrime();
        }
        else return true;
    }

    private boolean simpleExpression() {
        sign(); // optional, can be false but will not increment index
        return term() && simpleExpressionPrime();
    }

    private boolean term() {
        return factor() && termPrime();
    }

    private boolean simpleExpressionPrime() {
        if (addOp()) {
            return term() && simpleExpressionPrime();
        }
        return true; // can be null
    }

    private boolean factor() {
        if (name()) return factorSuffix();
        else if (unaryOp()) return factor();
        else if (prefixOp()) return variable();
        else if (tokens.get(index).getType().equals(ToyScanner.State.LPAREN.name())) { // "("
            index++;
            if (parseExpression()) {
                if (tokens.get(index).getType().equals(ToyScanner.State.RPAREN.name())) { // ")"
                    index++;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean termPrime() {
        if (mulOp()) {
            return factor() && termPrime();
        }
        return true; // can be null
    }

    private boolean factorSuffix() {
        if (tokens.get(index).getType().equals(ToyScanner.State.LPAREN.name())) { // "("
            index++;
            int indexBefore = index;
            if (!arguments()) {
                index = indexBefore;
                // if true, then nothing happens but index is incremented to account for the arguments
            }
            if (tokens.get(index).getType().equals(ToyScanner.State.RPAREN.name())) { // ")"
                index++;
                return true;
            } else return false;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.LBRACKET.name())) { // "["
            index++;
            if (parseExpression()) {
                if (tokens.get(index).getType().equals(ToyScanner.State.RBRACKET.name())) { // "]"
                    index++;
                    postfixOp(); // optional
                    return true;
                }
            }
            return false;
        }
        return true; // can be null
    }

    private boolean variable() {
        if (name()) {
            if (tokens.get(index).getType().equals(ToyScanner.State.LBRACKET.name())) { // "["
                index++;
                if (parseExpression()) {
                    if (tokens.get(index).getType().equals(ToyScanner.State.RBRACKET.name())) { // "]"
                        index++;
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean arguments() {
        return parseExpression() && argumentsPrime();
    }

    private boolean argumentsPrime() {
        if (tokens.get(index).getType().equals(ToyScanner.State.COMMA.name())) { // ","
            index++;
            return parseExpression() && argumentsPrime();
        }
        return true; // can be null
    }


    // CHECK FOR TERMINAL METHODS -- WILL NOT INCREMENT INDEX ON FALSE

    private boolean compareOp() {
        if (tokens.get(index).getType().equals(ToyScanner.State.EQUAL.name())) { // "=="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.EXCLAMATION.name())) { // "!="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.LESSTHAN.name())) { // "<"
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.GREATERTHAN.name())) { // ">"
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.LESSEQUAL.name())) { // "<="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.GREATEREQUAL.name())) { // ">="
            index++;
            return true;
        }
        return false;
    }

    private boolean assignOp() {
        if (tokens.get(index).getType().equals(ToyScanner.State.EQUAL.name())) { // "="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.PLUSEQUAL.name())) { // "+="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.MINUSEQUAL.name())) { // "-="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.STAREQUAL.name())) { // "*="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.SLASHEQUAL.name())) { // "/="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.PERCENTEQUAL.name())) { // "%="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.AMPERSANDEQUAL.name())) { // "&="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.CARETEQUAL.name())) { // "^="
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.PIPEEQUAL.name())) { // "|="
            index++;
            return true;
        }
        return false;
    }

    private boolean unaryOp() {
        if (tokens.get(index).getType().equals(ToyScanner.State.EXCLAMATION.name())) { // "!"
            index++;
            return true;
        }
        return false;
    }

    private boolean prefixOp() {
        if (tokens.get(index).getType().equals(ToyScanner.State.PLUSPLUS.name())) { // "++"
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.MINUSMINUS.name())) { // "--"
            index++;
            return true;
        }
        return false;
    }

    private boolean postfixOp() {
        return this.prefixOp();
    }

    private boolean andOp() {
        if (tokens.get(index).getType().equals(ToyScanner.State.AMPERSAND.name())) { // "&"
            index++;
            return true;
        }
        if (tokens.get(index).getType().equals(ToyScanner.State.AMPERSANDAMPERSAND.name())) { // "&&"
            index++;
            return true;
        }
        return false;
    }

    private boolean orOp() {
        if (tokens.get(index).getType().equals(ToyScanner.State.PIPE.name())) { // "|"
            index++;
            return true;
        }
        if (tokens.get(index).getType().equals(ToyScanner.State.PIPEPIPE.name())) { // "||"
            index++;
            return true;
        }
        if (tokens.get(index).getType().equals(ToyScanner.State.CARET.name())) { // "^"
            index++;
            return true;
        }
        return false;
    }

    private boolean mulOp() {
        if (tokens.get(index).getType().equals(ToyScanner.State.STAR.name())) { // "*"
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.SLASH.name())) { // "/"
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.PERCENT.name())) { // "%"
            index++;
            return true;
        }
        return false;
    }

    private boolean addOp() {
        if (tokens.get(index).getType().equals(ToyScanner.State.PLUS.name())) { // "+"
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(ToyScanner.State.MINUS.name())) { // "-"
            index++;
            return true;
        }
        return false;
    }

    private boolean sign() {
        return addOp();
    }

    private boolean number() {
        if (tokens.get(index).getType().equals(Token.TokenType.NUMBER.name())) { // number
            index++;
            return true;
        }
        return false;
    }

    private boolean literal() {
        if (number()) {
            return true;
        }
        else if (tokens.get(index).getType().equals(Token.TokenType.STRING.name())) { // string
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(Token.TokenType.CHARACTER.name())) { // character
            index++;
            return true;
        }
        else if (tokens.get(index).getType().equals(Token.TokenType.IDENTIFIER.name())) { // check if it's a keyword
            if (((Identifier) tokens.get(index)).getValue().equals("true") || ((Identifier) tokens.get(index)).getValue().equals("false")) {
                index++;
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean name() {
        if (tokens.get(index).getType().equals(Token.TokenType.IDENTIFIER.name())) { // identifier
            index++;
            return namePrime(); // always true
        }
        return false;
    }

    private boolean namePrime() {
        if (tokens.get(index).getType().equals(ToyScanner.State.DOT.name())) { // "."
            index++;
            return namePrime();
        }
        return true;
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
            if (parser.parseExpression()) {
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
