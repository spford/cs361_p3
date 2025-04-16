import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TMSimulator {

    protected static int numberOfStates;
    protected static int startState = 0;
    protected static int haltingState;
    protected static int numbSymbols;

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

    public void main(String[] args) {

        try {
            int currentLine = 0;
            Scanner input = new Scanner(new FileInputStream(args[0]));
            while (input.hasNextLine()) {
                if(currentLine == 0) {
                   setStatesByNumber(Integer.parseInt(input.nextLine()));
                } else if(currentLine == 1) {
                    setSymbolsByNumber(Integer.parseInt(input.nextLine()));
                }
                currentLine++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
