package Parser;

public class ReturnStatement extends Node {

        public Node value; // expression, can be null for empty return statement

        public ReturnStatement(Node value) {
            this.value = value;
        }
}
