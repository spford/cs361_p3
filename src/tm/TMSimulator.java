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
    protected Map<Integer, Integer> tape;
    protected LinkedList<Integer> sigma;
    protected Map<Integer, Map<Integer, Transition>> states;

    public TMSimulator() {
        tape = new HashMap<>();
        sigma = new LinkedList<>();
        states = new HashMap<>();
        pointer = 0;
    }

    private void runSimulation() {
        int currentState = 0;
        while (currentState < numbStates-1) { //Try to get to haltingState (may add iterations/time limits)
            int currentTapeValue = tape.getOrDefault(pointer, 0);
            currentState = getTransition(currentState, currentTapeValue);
        }
        printResults();
    }

    private int getTransition(int currentState, int currentTapeValue) {
        Transition nextTransition = states.get(currentState).get(currentTapeValue);
        tape.put(pointer, nextTransition.write_value);
        if (nextTransition.isForward()) { pointer++; }
        else { pointer--; }
        return  nextTransition.next_state;
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
                    addTransition(tokens, currentState, i);
                    currentPosition++;
                }
                currentState++;
            }
            if(input.hasNextLine()) {
                String newLine = input.nextLine();
                for (int i = 0; i < newLine.length(); i++) {
                    tape.put(i, Integer.parseInt(String.valueOf(newLine.charAt(i))));
                }
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addTransition(String[] tokens, int currentState, int onSymbol) {
        boolean forward = false;
        int next_state = Integer.parseInt(tokens[0]);
        int write_sym = Integer.parseInt(tokens[1]);
        String move_sym = String.valueOf(tokens[2]);
        if (move_sym.equals("R")) {
            forward = true;
        }
        Transition newTransition = new Transition(next_state,write_sym,forward);
        states.putIfAbsent(currentState, new HashMap<>());
        states.get(currentState).put(onSymbol, newTransition);
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
        int output_Length = 0;
        int sum_of_symbols = 0;
        int min = tape.keySet().stream().min(Integer::compareTo).orElse(0);
        int max = tape.keySet().stream().max(Integer::compareTo).orElse(0);

        System.out.println("output:");

        for (int i = min; i <= max; i++) {
            int val = tape.getOrDefault(i, 0);
            System.out.print(val);
            sum_of_symbols += val;
            output_Length++;
        }
        System.out.println();
        System.out.println("output length: " + output_Length);
        System.out.println("sum of symbols: " + sum_of_symbols);
    }

    public static void main(String[] args) {

        TMSimulator simulator = new TMSimulator();
        simulator.fileParser(args[0]);
        simulator.runSimulation();

    }
}
