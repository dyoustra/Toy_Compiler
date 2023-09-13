// Danny Youstra
// Compilers
// Homework #4
// 10/24/22

package Scanner;

import java.util.Scanner;

public class ToyScanner {

    public enum State {
        START(false, null) {
            @Override
            public State next(char c) {
                if (State.isDigit(c) && c != '0') {
                    return DIGIT;
                }
                if(c >= 'a' && c <= 'z') return IDENTIFIER;
                else if(c >= 'A' && c <= 'Z') return IDENTIFIER;
                switch (c) {
                    case '0': return ZERO;
                    case '\'': return CHAR;
                    case '\"': return STRING;

                    // symbols (not all of them!)
                    case '+': return PLUS;
                    case '-': return MINUS;
                    case '*': return STAR;
                    case '/': return SLASH;
                    case '%': return PERCENT;
                    case '^': return CARET;
                    case '&': return AMPERSAND;
                    case '|': return PIPE;
                    case '~': return TILDE;
                    case '!': return EXCLAMATION;
                    case '=': return EQUAL;
                    case '<': return LESSTHAN;
                    case '>': return GREATERTHAN;
                    case '(': return LPAREN;
                    case ')': return RPAREN;
                    case '[': return LBRACKET;
                    case ']': return RBRACKET;
                    case '{': return LBRACE;
                    case '}': return RBRACE;
                    case ',': return COMMA;
                    case ';': return SEMICOLON;
                    case ':': return COLON;
                    case '.': return DOT;

                    case '\n':
                    case '\r':
                    case '\t':
                    case ' ': return START;
                    default:  return ERROR;
                }
            }
        },

        // INTEGER LITERALS

        ZERO(true, Token.TokenType.INT) {
            @Override
            public State next(char c) {
                if (State.isDigit(c)) return DIGIT;
                switch (c) {
                    case '_': return INTUNDER;
                    case 'x': return HEXSTART;
                    case 'X': return HEXSTART;
                    default:  return ERROR;
                }
            }
        },

        DIGIT(true, Token.TokenType.INT) {
            @Override
            public State next(char c) {
                if (State.isDigit(c)) return DIGIT;
                switch (c) {
                    case '_': return INTUNDER;
                    default :  return ERROR;
                }
            }
        },

        INTUNDER(false, Token.TokenType.INT) {
            @Override
            public State next(char c) {
                if (State.isDigit(c)) return DIGIT;
                return ERROR;
            }
        },

        HEXSTART(false, Token.TokenType.INT) {
            @Override
            public State next(char c) {
                if (State.isHexChar(c)) return HEX;
                return ERROR;
            }
        },

        HEX(true, Token.TokenType.INT) {
            @Override
            public State next (char c) {
                if (State.isHexChar(c)) return HEX;
                switch (c) {
                    case '_': return HEXUNDER;
                    default : return ERROR;
                }
            }
        },

        HEXUNDER(false, Token.TokenType.INT) {
            @Override
            public State next(char c) {
                if (State.isHexChar(c)) return HEX;
                return ERROR;
            }
        },

        // CHAR LITERALS

        CHAR(false, Token.TokenType.CHAR) {
            @Override
            public State next(char c) {
                switch (c) {
                    case '\\': return CHARSPECIAL;
                    case '\'': return CHAREND;
                    default: return ALMOST;
                }
            }
        },
        CHARSPECIAL(false, Token.TokenType.CHAR) {
            @Override
            public State next(char c) {
                if (isSpecialCharacter(c)) return ALMOST;
                else return ERROR;
            }
        },

        ALMOST(false, Token.TokenType.CHAR) {
            @Override
            public State next(char c){
                if(c == '\'') return CHAREND;
                else return ERROR;
            }
        },

        CHAREND(true, Token.TokenType.CHAR) {
            @Override
            public State next(char c){
                return ERROR; // if there is a char after '
            }
        },

        STRINGEND(true, Token.TokenType.STRING) {
            @Override
            public State next(char c){
                return ERROR; // if there is a char after "
            }
        },

        // STRING LITERALS

        STRING(false, Token.TokenType.STRING) {
            @Override
            public State next(char c) {
                switch (c) {
                    case '\\': return STRINGSPECIAL;
                    case '\"': return STRINGEND;
                    default: return STRING;
                }
            }
        },

        STRINGSPECIAL(false, Token.TokenType.STRING) {
            @Override
            public State next(char c) {
                if(isSpecialCharacter(c)) return STRING;
                else return ERROR;
            }
        },

        // IDENTIFIER

        IDENTIFIER(true, Token.TokenType.IDENTIFIER) {
            @Override
            public State next(char c) {
                if(c == '_') return IDENTIFIERUNDER;
                else if(c >= 'a' && c <= 'z') return IDENTIFIER;
                else if(c >= 'A' && c <= 'Z') return IDENTIFIER;
                else if(c >= '0' && c <= '9') return IDENTIFIER;
                else return ERROR;
            }
        },

        IDENTIFIERUNDER(false, Token.TokenType.IDENTIFIER) {
            @Override
            public State next(char c) {
                if(c >= 'a' && c <= 'z') return IDENTIFIER;
                else if(c >= 'A' && c <= 'Z') return IDENTIFIER;
                else if(State.isDigit(c)) return IDENTIFIER;
                else return ERROR;
            }
        },

        // SYMBOLS

        PLUS(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '+') return PLUSPLUS;
                else if(c == '=') return PLUSEQUAL;
                else return ERROR;
            }
        },

        MINUS(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '-') return MINUSMINUS;
                else if(c == '=') return MINUSEQUAL;
                else return ERROR;
            }
        },

        EXCLAMATION(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '=') return NOTEQUAL;
                else return ERROR;
            }
        },

        TILDE(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        PLUSPLUS(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        MINUSMINUS(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        PLUSEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        MINUSEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        NOTEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        STAR(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '=') return STAREQUAL;
                else return ERROR;
            }
        },

        SLASH(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '=') return SLASHEQUAL;
                else if(c == '/') return COMMENT;
                else return ERROR;
            }
        },

        COMMENT(true, null) {
            @Override
            public State next(char c) {
                switch (c) {
                    case '\r':
                    case '\n':
                        return START;
                    default:
                        return COMMENT;
                }
            }
        },

        PERCENT(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '=') return PERCENTEQUAL;
                else return ERROR;
            }
        },

        AMPERSAND(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '&') return AMPERSANDAMPERSAND;
                else if(c == '=') return AMPERSANDEQUAL;
                else return ERROR;
            }
        },

        PIPE(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '|') return PIPEPIPE;
                else if(c == '=') return PIPEEQUAL;
                else return ERROR;
            }
        },

        CARET(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '=') return CARETEQUAL;
                else return ERROR;
            }
        },

        LESSTHAN(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '<') return LESSLESS;
                else if(c == '=') return LESSEQUAL;
                else return ERROR;
            }
        },

        GREATERTHAN(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '>') return GREATERGREATER;
                else if(c == '=') return GREATEREQUAL;
                else return ERROR;
            }
        },

        EQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '=') return EQUALEQUAL;
                else return ERROR;
            }
        },

        STAREQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        SLASHEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        PERCENTEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        AMPERSANDEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        PIPEEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        CARETEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        LESSLESS(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '=') return LESSLESSEQUAL;
                else return ERROR;
            }
        },

        GREATERGREATER(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                if(c == '=') return GREATERGREATEREQUAL;
                else return ERROR;
            }
        },

        LESSEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        GREATEREQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        EQUALEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        LESSLESSEQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        GREATERGREATEREQUAL(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        AMPERSANDAMPERSAND(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        PIPEPIPE(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        DOT(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        COMMA(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        SEMICOLON(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        COLON(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        LBRACE(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        RBRACE(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        LBRACKET(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        RBRACKET(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        LPAREN(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },

        RPAREN(true, Token.TokenType.SYMBOL) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        },


        // ERROR STATE

        ERROR(false, null) {
            @Override
            public State next(char c) {
                return ERROR;
            }
        };

        private final boolean isFinal;
        private final Token.TokenType type;

        private State(boolean isFinal, Token.TokenType type) {
            this.isFinal = isFinal;
            this.type = type;
        }

        public abstract State next(char c);

        private static boolean isDigit(char c) {
            return c >= '0' && c <= '9';
        }

        private static boolean isHexChar(char c) {
            if (State.isDigit(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) return true;
            return false;
        }

        private static boolean isSpecialCharacter(char c) {
            if (c == '\\' || c == 'n' || c == 'r' || c == 't' || c == '0' || c == '\'' || c == '\"') return true;
            return false;
        }
    }

    public static Token getNextToken(String string) {
        State prevState = null;
        State prevFinalState = null;
        State currentState = State.START;
        // value of the current token
        int numValue = 0;
        StringBuilder strValue = new StringBuilder();
        char charValue = ' ';
        int i = 0;
        int row = startRow;
        int col = startCol;
        int startOfTokenCol = startCol;

        while (currentState != State.ERROR && i < string.length()) {
            char c = string.charAt(i);

            if (currentState == State.START) {
                startOfTokenCol = col;
            }

            if (currentState.isFinal) {
                prevFinalState = currentState;
                startCol = col;
            }

            prevState = currentState;
            currentState = currentState.next(c);
            switch (currentState) {
                case ALMOST:
                    if (prevState == State.CHARSPECIAL) {
                        c = getSpecialChar(c);
                    }
                    charValue = c;
                    break;

                case STRING:
                    if (c == '"') break;
                    else if (prevState == State.STRINGSPECIAL) {
                        c = getSpecialChar(c);
                    }
                    strValue.append(c);
                    break;

                case IDENTIFIER:
                    strValue.append(c);
                    break;

                case DIGIT:
                    numValue = numValue * 10 + (string.charAt(i) - '0');
                    break;

                case HEX:
                    numValue = numValue * 16;
                    if (c >= '0' && c <= '9') numValue += c - '0';
                    else if (c >= 'a' && c <= 'f') numValue += c - 'a' + 10;
                    else if (c >= 'A' && c <= 'F') numValue += c - 'A' + 10;
                    break;
            }

            col++;
            i++;
        }

        if (prevFinalState == null || currentState == State.COMMENT) {
            if (currentState.isFinal) {
                prevFinalState = currentState;
                startCol = col;
            } else return null;
            if (i != string.length()) {
                return new ErrorToken(row, startOfTokenCol);
            }
        }

        if (prevFinalState.type == null) return null;
        return switch (prevFinalState.type) {
            case IDENTIFIER -> switch (strValue.toString()) {
                case "program" -> new KeywordToken(KeywordToken.Keyword.PROGRAM, row, startOfTokenCol);
                case "if" -> new KeywordToken(KeywordToken.Keyword.IF, row, startOfTokenCol);
                case "else" -> new KeywordToken(KeywordToken.Keyword.ELSE, row, startOfTokenCol);
                case "while" -> new KeywordToken(KeywordToken.Keyword.WHILE, row, startOfTokenCol);
                case "return" -> new KeywordToken(KeywordToken.Keyword.RETURN, row, startOfTokenCol);
                case "int" -> new KeywordToken(KeywordToken.Keyword.INT, row, startOfTokenCol);
                case "char" -> new KeywordToken(KeywordToken.Keyword.CHAR, row, startOfTokenCol);
                case "boolean" -> new KeywordToken(KeywordToken.Keyword.BOOLEAN, row, startOfTokenCol);
                case "void" -> new KeywordToken(KeywordToken.Keyword.VOID, row, startOfTokenCol);
                default -> new IdentifierToken(strValue.toString(), row, startOfTokenCol);
            };
            case INT -> new NumericLiteral(numValue, row, startOfTokenCol);
            case SYMBOL -> new SymbolToken(prevFinalState.name(), row, startOfTokenCol);
            case CHAR -> new CharacterLiteral(charValue, row, startOfTokenCol);
            case STRING -> new StringLiteral(strValue.toString(), row, startOfTokenCol);
            default -> throw new IllegalStateException("Unexpected value: " + prevFinalState.type);
        };
    }

    private static char getSpecialChar(char c) {
        return switch (c) {
            case 'n' -> '\n';
            case 'r' -> '\r';
            case 't' -> '\t';
            case '0' -> '\0';
            default -> c;
        };
    }

    public static int startRow = 0;
    public static int startCol = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter tokens: ");
        if (scanner.hasNextLine()) System.out.println();
        Token token = null;
        while (scanner.hasNextLine()) {
            String current = scanner.nextLine();
            while (startCol < current.length()) {
                token = getNextToken(current.substring(startCol));
                if (token != null) System.out.println(token);
            }
            startCol = 0;
            startRow++;
        }
        System.out.println(new EOFToken(startRow, startCol));
    }

}
