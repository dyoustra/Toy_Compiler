public class WhileStatement extends Node {

        public Node condition;
        public Node body;

        public WhileStatement(Node condition, Node body) {
            this.condition = condition;
            this.body = body;
        }
}
