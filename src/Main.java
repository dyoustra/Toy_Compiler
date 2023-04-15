import Scanner.*;
import Parser.*;
import Semantics.Traverser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("Parser Test Programs\\echo.toy");
        Scanner scanner = new Scanner(file);
        Token token;
        ArrayList<Token> tokens = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String current = scanner.nextLine();
            while (ToyScanner.startCol < current.length()) {
                token = ToyScanner.getNextToken(current.substring(ToyScanner.startCol));
                if (token != null) tokens.add(token);
            }
            ToyScanner.startCol = 0;
            ToyScanner.startRow++;
        }
        System.out.println("done scanning");
        Parser parser = new Parser(tokens);
        Node root = parser.parse();
        if (root != null) {
            System.out.println("OK");
        } else {
//            System.out.println("ERROR" + " at row:" + Scanner.ToyScanner.startRow + " col:" + parser.size);
            System.out.println("NO");
        }

//        Traverser traverser = new Traverser(root, parser.getSymbolTable());
//        traverser.traverseAll();
//        System.out.println("bruh");
    }
}
