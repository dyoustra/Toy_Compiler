package Parser;

public class CallStatement extends Node {

        public Node function;
        public Arguments arguments;

        public CallStatement(Node function, Arguments arguments) {
            super(Kind.CALL_STATEMENT);
            this.function = function;
            this.arguments = arguments;
        }
}
