package src.tm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class TMSimulator {

    protected int numbStates;
    protected int haltingState;
    protected int numbSymbols;
    protected int pointer;
    protected LinkedList<Integer> tape;
    protected LinkedList<Integer> sigma;
    protected Map<Integer, LinkedList<Transition>> states;

    public TMSimulator() {
        tape = new LinkedList<>();
        sigma = new LinkedList<>();
        states = new HashMap<>();
        pointer = 0;
    }

    private void runSimulation() {
        int currentState = 0;
        int iterations = 0;
        while (currentState < numbStates-1) { //Try to get to haltingState (may add iterations/time limits)
            int currentTapeValue = tape.get(pointer);
            currentState = getTransition(currentState, currentTapeValue);
            iterations++;
        }
        printResults();
    }

    private int getTransition(int currentState, int currentTapeValue) {
        Transition nextTransition = states.get(currentState).get(currentTapeValue);
        tape.set(pointer, nextTransition.write_value);
        if (nextTransition.isForward()) { pointer++; }
        else { pointer--; }
        tapeResize();
        return  nextTransition.next_state;
    }

    private void tapeResize() {
        if (pointer >= tape.size() ) { tape.addLast(0); }
        else if (pointer < 0) { tape.addFirst(0); pointer = 0; }
    }

    private void fileParser(String fileName) {
        int currentState = 0;
        int currentPosition = 0;
        try {
            Scanner input = new Scanner(new FileInputStream(fileName));
                setStatesByNumber(Integer.parseInt(input.nextLine()));  //read number of states 0 - (n-1)
                setSymbolsByNumber(Integer.parseInt(input.nextLine())); //read number of symbols + 0
                while (currentPosition < (numbSymbols+1) * (numbStates-1)) {    //total number of transitions
                    for (int i = 0; i <= numbSymbols; i++) { //Every state has number of Symbols + '0' transitions
                        String newLine = input.nextLine();
                        String[] tokens = newLine.split(",");
                        addTransition(tokens, currentState);
                        currentPosition++;
                    }
                    currentState++;
                }
                if(input.hasNextLine()) {
                    String newLine = input.nextLine();
                    for(char c : newLine.toCharArray()) {
                        tape.add(Integer.parseInt(String.valueOf(c)));
                    }
                } else {tape.add(0);}
                input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addTransition(String[] tokens, int currentState) {
        boolean forward = false;
        int next_state = Integer.parseInt(tokens[0]);
        int write_sym = Integer.parseInt(tokens[1]);
        String move_sym = String.valueOf(tokens[2]);
        if (move_sym.equals("R")) {
            forward = true;
        }
        Transition newTransition = new Transition(next_state,write_sym,forward);
        states.putIfAbsent(currentState, new LinkedList<>());
        states.get(currentState).add(newTransition);
    }

    private void setStatesByNumber(int numberOfStates) {
        numbStates = numberOfStates;
        haltingState = numbStates - 1;
    }

    private void setSymbolsByNumber(int numbSymbols) {
        if (numbSymbols < 10 && numbSymbols > 0) {
            this.numbSymbols = numbSymbols;
            for (int i = 1; i <= numbSymbols; i++) {
                sigma.add(i);
            }
        } else {
            System.err.println("Number of input symbols must be less than 10");
        }
    }

    private void printResults() {
        for (Integer integer : tape) {
            System.out.print(integer);
        }
        System.out.println();
    }

    public static void main(String[] args) {

        TMSimulator simulator = new TMSimulator();
        simulator.fileParser(args[0]);
        simulator.runSimulation();

    }
}
