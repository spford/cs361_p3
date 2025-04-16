import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TMSimulator {

    protected int numberOfStates;
    protected int startState = 0;
    protected int haltingState;
    protected int numbSymbols;

    public TMSimulator() {

    }

    private void fileParser(String fileName) {
        try {
            int currentLine = 0;
            Scanner input = new Scanner(new FileInputStream(fileName));
            while (input.hasNextLine()) {
                if(currentLine == 0) {
                    setStatesByNumber(Integer.parseInt(input.nextLine()));
                } else if(currentLine == 1) {
                    setSymbolsByNumber(Integer.parseInt(input.nextLine()));
                } else {

                }
                currentLine++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setStatesByNumber(int numbStates) {
        numberOfStates = numbStates;
        haltingState = numbStates - 1;
    }

    private void setSymbolsByNumber(int numbSymbols) {
        if (numbSymbols < 10) {
            this.numbSymbols = numbSymbols;
        } else {
            //error
        }
    }

    public static void main(String[] args) {

        TMSimulator simulator = new TMSimulator();
        simulator.fileParser(args[0]);
        System.out.println("Number of states: " + simulator.numberOfStates);
        System.out.println("Number of symbols: " + simulator.numbSymbols);
        System.out.println("Halting state: " + simulator.haltingState);
        System.out.println("Starting state: " + simulator.startState);

    }
}
