package Parser;

public class CallStatement extends Node {

        public Node function;
        public Arguments arguments;

        public CallStatement(Node function, Arguments arguments) {
            this.function = function;
            this.arguments = arguments;
        }
}
