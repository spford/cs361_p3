package src.tm;

import java.util.LinkedList;

public class Transition {

    protected int next_state;
    protected int write_value;
    protected boolean forward;

    public Transition(int next_state, int write_value, boolean forward) {
        this.next_state = next_state;
        this.write_value = write_value;
        this.forward = forward;
    }
    public int getNextState() {return next_state;}
    public int getWriteValue() {return write_value;}
    public boolean isForward() {return forward;}
}
