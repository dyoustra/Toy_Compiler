package Parser;

public class WhileStatement extends Node {

        public Node condition;
        public Node body;

        public WhileStatement(Node condition, Node body) {
            super(Kind.WHILE_STATEMENT);
            this.condition = condition;
            this.body = body;
        }
}
