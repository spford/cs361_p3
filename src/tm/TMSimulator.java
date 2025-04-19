package src.tm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * TMSimulator is both an implementation that provides all necessary
 * operations to construct a Turing Machine and simulates running
 * the constructed Turing Machine.
 * @author Spencer Ford
 * @author Luis Acosta
 */

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

    /**
     * This method performs the simulation of the constructed Turing
     * Machine where the tape begins with the string read by the
     * fileParser method.
     */
    private void runSimulation() {
        int currentState = 0;
        while (currentState < numbStates-1) { //Try to get to haltingState (may add iterations/time limits)
            int currentTapeValue = tape.getOrDefault(pointer, 0);
            currentState = getTransition(currentState, currentTapeValue);
        }
        printResults();
    }

    /**
     * This method gets and performs a transition from the currentState
     * where the transition is determined by the currentState * currentTapeValue.
     * After the transition is completed, the read symbol is update along with
     * moving the tape pointer as per the transition.
     * @param currentState - int - Integer representation of current state
     * @param currentTapeValue - int - Integer symbol to determine transition path
     * @return - int - Integer representation of next state
     */
    private int getTransition(int currentState, int currentTapeValue) {
        Transition nextTransition = states.get(currentState).get(currentTapeValue);
        tape.put(pointer, nextTransition.write_value);
        if (nextTransition.isForward()) { pointer++; }
        else { pointer--; }
        return  nextTransition.next_state;
    }

    /**
     * This method reads a file of the following format
     *      # of States
     *      # of Symbols
     *      ((# of States - 1)*(# of States + 1)) Transitions
     *      Input String for Simulation
     * and stores them into the correspondingly named attributes.
     * @param fileName - file to be read
     */
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
                    String[] tokens = newLine.split(","); // Transition are of format: next_state,write_symbol,move
                    addTransition(tokens, currentState, i);
                    currentPosition++;
                }
                currentState++;
            }

            // Store Input String on tape
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

    /**
     * This method adds a transition to the currentState to be executed on
     * onSymbol where the transition is defined by the information read from
     * the transitions portion of the fileParser method.
     * @param tokens - String array - Contains [next_state, write_symbol, move]
     * @param currentState - int - Integer representation of current state
     * @param onSymbol - int - Integer symbol to define transition path
     */
    private void addTransition(String[] tokens, int currentState, int onSymbol) {
        boolean forward = false;    // Move left by default
        int next_state = Integer.parseInt(tokens[0]);
        int write_sym = Integer.parseInt(tokens[1]);
        String move_sym = String.valueOf(tokens[2]);
        if (move_sym.equals("R")) {
            forward = true;         // Overwrite move to go right
        }
        Transition newTransition = new Transition(next_state,write_sym,forward);
        states.putIfAbsent(currentState, new HashMap<>());
        states.get(currentState).put(onSymbol, newTransition);
    }

    /**
     * This method stores the number of states that was read
     * from the "# of States" portion of the fileParser method
     * @param numberOfStates - int - Number of States for Turing Machine
     */
    private void setStatesByNumber(int numberOfStates) {
        numbStates = numberOfStates;
        haltingState = numbStates - 1;
    }

    /**
     * This method stores the number of symbols (Up to 9) that was read
     * from the "# of Symbols" portion of the fileParser method
     * @param numbSymbols - int - Number of Symbols for Turing Machine
     */
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

    /**
     * This method prints a report after running a simulation
     * on the constructed Turing Machine in the following format
     *      "output:"
     *      Values of Tape Squares Visited
     *      "output length: "Length of Output
     *      "sum of symbols: "Total Value of Values of Tape Squares Visited
     */
    private void printResults() {
        int output_Length = 0;
        int sum_of_symbols = 0;

        // Find the first and last square on tape containing values
        int min = tape.keySet().stream().min(Integer::compareTo).orElse(0);
        int max = tape.keySet().stream().max(Integer::compareTo).orElse(0);

        System.out.println("output:");

        // Print out Values of Tape Squares Visited
        for (int i = min; i <= max; i++) {
            int val = tape.getOrDefault(i, 0);
            System.out.print(val);
            sum_of_symbols += val;  // Compute Total Value of Values of Tape Squares Visited
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
