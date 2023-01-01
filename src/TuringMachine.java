/*-----------------------------------------------------------------------------------------------------------------------------------
- Software Name: Turing Machine
- Version: 1.0
- Language: Java
- Developer: Emilio Rivera Macías
- Date: 10/08/2022
- Contact: emilioriveramacias@gmail.com
-----------------------------------------------------------------------------------------------------------------------------------*/

// <TURING MACHINE FILE FORMAT------------------------------------------------------------------------------------------------------>
/*-----------------------------------------------------------------------------------------------------------------------------------
- TuringMachine.txt Format:
(I/O stream is written in a single line with or wihtout spaces)
ex.
aaabbaba bbbabaaaa abbbababa
(initial state)
ex.
q0
(final states separated by a space and in a single line)
ex.
q3 q5
(each rule goes in a single line and is of the form <current state> <input symbol> | <output symbol> <direction I/O header moves> <next state>)
(*white space symbol " " can be specified with "%" symbol)
ex.
q0 a | a > q1
q1 b | a < q0
-----------------------------------------------------------------------------------------------------------------------------------*/

// necessary includes ---------------------------->
package turing_machine;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.ArrayList;

// Turing Machine class ---------------------------->
// This Turing Machine supports white spaces and uses them at the beginning and end of the IO Stream
public class TuringMachine {
    // declaration of fields ---------------------------->
    // IO Stream of the Turing Machine
    private StringBuilder fsb_IO_STREAM;
    // current state of the Turing Machine, starts being the initial state
    private String fs_CURRENT_STATE;
    // ArrayList containing the final states of the Turing Machine
    private ArrayList<String> fal_FINAL_STATES;
    // HashMap to store the transition rules of the Turing Machine
    private HashMap<String,Transition> fhm_TURING_MACHINE;
    
    // declaration and definition of methods ---------------------------->
    /*-----------------------------------------------------------------------------------------------------------------------------------
    - Functionality: Empty constructor for TuringMachine class
    -----------------------------------------------------------------------------------------------------------------------------------*/
    public TuringMachine(){
        // field initialization ---------------------------->
        this.fhm_TURING_MACHINE = new HashMap<>();
        this.fal_FINAL_STATES = new ArrayList<>();
    }
    
    /*-----------------------------------------------------------------------------------------------------------------------------------
    - Functionality: Constructor that reads from the sepcified file and initializes the TuringMachine object
    -----------------------------------------------------------------------------------------------------------------------------------*/
    public TuringMachine(String s_fileName){
        // field and variable declaration and initialization ---------------------------->
        this.fhm_TURING_MACHINE = new HashMap<>();
        this.fal_FINAL_STATES = new ArrayList<>();
        String s_currentState, s_nextState;
        char c_inputSymbol, c_outputSymbol, c_Direction;
        StringTokenizer st_tokens;
        // procedure ---------------------------->
        //try block to catch FileNotFoundException or other Exceptions
        try{
            // open file with Scanner to read from it
            Scanner scanner_fileInput = new Scanner(new File(s_fileName));
            // get the IOStream of the Turing Machine and append white spaces at the beginning and end of the stream
            this.fsb_IO_STREAM = new StringBuilder(' ' + scanner_fileInput.nextLine() + ' ');
            // get the initial state
            this.fs_CURRENT_STATE = scanner_fileInput.nextLine();
            // get the final states (next line and tokenize them to store each one
            // in the fal_FINAL_STATES field
            st_tokens = new StringTokenizer(scanner_fileInput.nextLine());
            while (st_tokens.hasMoreElements()){
                this.fal_FINAL_STATES.add(st_tokens.nextToken());
            }
            // get the following lines to store (put) the rules from the Turing Machine file
            while (scanner_fileInput.hasNextLine()){
                // tokenize line
                st_tokens = new StringTokenizer(scanner_fileInput.nextLine());
                // get current state of the transition rule
                // set the delimiter to " |" so no extra tokens are retrieved from the line
                s_currentState = st_tokens.nextToken(" |");
                // get input symbol from the transition rule
                c_inputSymbol = st_tokens.nextToken().charAt(0);
                // case if the input symbol is a white space
                if (c_inputSymbol == '%')
                    c_inputSymbol = ' ';
                // get the output symbol from the transition rule
                c_outputSymbol = st_tokens.nextToken().charAt(0);
                // case if the output symbol is a white space
                if (c_outputSymbol == '%')
                    c_outputSymbol = ' ';
                c_Direction = st_tokens.nextToken().charAt(0);
                // get the next state from the transition rule
                s_nextState = st_tokens.nextToken();
                // put the retrieved data into the fhm_TURING_MACHINE to successfully store the transition rule
                fhm_TURING_MACHINE.put((s_currentState + c_inputSymbol), new Transition(c_outputSymbol, s_nextState, c_Direction));
            }
        }
        // catch blocks for FileNotFoundException and other Exceptions
        catch (FileNotFoundException e){
            System.out.println("\n\t--->" + e.getMessage() + " <---");
        }
        catch (Exception e){
            System.out.println("\n\t--->" + e.getMessage() + " <---");
        }
    }
    
    /*-----------------------------------------------------------------------------------------------------------------------------------
    - Functionality: Turing Machine process that does the respective IO operations basing on the rules of transitions and indicates
        if the process ended in a final state (true) or not (false)
    -----------------------------------------------------------------------------------------------------------------------------------*/
    public boolean TURING_MACHINE(){
        // variable declaration ---------------------------->
        Transition t_temp;
        boolean b_isFinal = false;
        // procedure ---------------------------->
        // iterate through the fsb_IO_STREAM
        // start at index 1 to avoid reading the white space at the beginning of the Stream
        for (int it = 1; it < this.fsb_IO_STREAM.length() && it > -1;){
            // get the transition rule based on the current state and the current symbol readed from the IOStream
            t_temp = this.fhm_TURING_MACHINE.get(this.fs_CURRENT_STATE + this.fsb_IO_STREAM.charAt(it));
            // check if there isn't an existing rule for the current state and input symbol
            if (t_temp == null){
                System.out.println("\n\t---> No existing transition for input character: " + this.fsb_IO_STREAM.charAt(it) + " and current state: " + this.fs_CURRENT_STATE + "<---\n");
                return false;
            }
            // output the transition rule that the Turing Machine will commit
            print_Transition(this.fs_CURRENT_STATE, this.fsb_IO_STREAM.charAt(it), t_temp);
            // change current state
            this.fs_CURRENT_STATE = t_temp.getFs_State();
            // do the respective output operation on the IO Stream
            this.fsb_IO_STREAM.setCharAt(it, t_temp.getFc_Output());
            // move the Turing Machine header depending on the direction indicated by the transition rule
            switch (t_temp.getFb_Direction()){
                case '>' -> it++;
                case '<' -> it--;
                // this case is when the header does not move any more and the current state does not have any transitions
                default -> {
                    // check if the last current state of the Turing Machine iteration is a final state, if it is return true
                    for (int it1 = 0; it1 < this.fal_FINAL_STATES.size(); it1++){
                        if (this.fal_FINAL_STATES.get(it1).equals(this.fs_CURRENT_STATE))
                            b_isFinal = true;
                    }
                    return b_isFinal;
                }
            }
        }
        // check if the last current state of the Turing Machine iteration is a final state, if it is return true
        for (int it2 = 0; it2 < this.fal_FINAL_STATES.size(); it2++)
            if (this.fal_FINAL_STATES.get(it2).equals(this.fs_CURRENT_STATE))
                    return true;
        // if it does not equal to any of the final states, then it returns false since the Turing Machine didn't accept it
        return false;
    }
    
    /*-----------------------------------------------------------------------------------------------------------------------------------
    - Functionality: read the Turing Machine from the indicated file and initialize the TuringMachine object
    -----------------------------------------------------------------------------------------------------------------------------------*/
    public void read_TM_file(String s_fileName){
        // variable declaration ---------------------------->
        String s_currentState, s_nextState;
        char c_inputSymbol, c_outputSymbol, c_Direction;
        StringTokenizer st_tokens;
        // procedure ---------------------------->
        //try block to catch FileNotFoundException or other Exceptions
        try{
            // open file with Scanner to read from it
            Scanner scanner_fileInput = new Scanner(new File(s_fileName));
            // get the IOStream of the Turing Machine and append white spaces at the beginning and end of the stream
            this.fsb_IO_STREAM = new StringBuilder(' ' + scanner_fileInput.nextLine() + ' ');
            // get the initial state
            this.fs_CURRENT_STATE = scanner_fileInput.nextLine();
            // get the final states (next line and tokenize them to store each one
            // in the fal_FINAL_STATES field
            st_tokens = new StringTokenizer(scanner_fileInput.nextLine());
            while (st_tokens.hasMoreElements()){
                this.fal_FINAL_STATES.add(st_tokens.nextToken());
            }
            // get the following lines to store (put) the rules from the Turing Machine file
            while (scanner_fileInput.hasNextLine()){
                // tokenize line
                st_tokens = new StringTokenizer(scanner_fileInput.nextLine());
                // get current state of the transition rule
                // set the delimiter to " |" so no extra tokens are retrieved from the line
                s_currentState = st_tokens.nextToken(" |");
                // get input symbol from the transition rule
                c_inputSymbol = st_tokens.nextToken().charAt(0);
                // case if the input symbol is a white space
                if (c_inputSymbol == '%')
                    c_inputSymbol = ' ';
                // get the output symbol from the transition rule
                c_outputSymbol = st_tokens.nextToken().charAt(0);
                // case if the output symbol is a white space
                if (c_outputSymbol == '%')
                    c_outputSymbol = ' ';
                c_Direction = st_tokens.nextToken().charAt(0);
                // get the next state from the transition rule
                s_nextState = st_tokens.nextToken();
                // put the retrieved data into the fhm_TURING_MACHINE to successfully store the transition rule
                fhm_TURING_MACHINE.put((s_currentState + c_inputSymbol), new Transition(c_outputSymbol, s_nextState, c_Direction));
            }
        }
        // catch blocks for FileNotFoundException and other Exceptions
        catch (FileNotFoundException e){
            System.out.println("\n\t--->" + e.getMessage() + " <---");
        }
        catch (Exception e){
            System.out.println("\n\t--->" + e.getMessage() + " <---");
        }
    }
    
    /*-----------------------------------------------------------------------------------------------------------------------------------
    - Functionality: output the current transition rule of the Turing Machine
    -----------------------------------------------------------------------------------------------------------------------------------*/
    private void print_Transition(String s_temp, char c_temp, Transition t_temp){
        System.out.println("Current State: \"" + s_temp + "\" Input Symbol: \"" + c_temp + "\" -> " + t_temp);
    }
    
    // toString and getters ---------------------------->
    @Override
    public String toString(){
        // variable declaration and initialization ---------------------------->
        String temp_TM = "Input Stream:\n\"" + this.fsb_IO_STREAM + "\"\nFinal States: " + this.fal_FINAL_STATES + "\nRules:\n";
        // procedure ---------------------------->
        for (Map.Entry<String, Transition> it : fhm_TURING_MACHINE.entrySet()){
            temp_TM += "\"" + it.getKey().substring(0,2) + "\" \"" + it.getKey().charAt(2) + "\" | " + it.getValue() + "\n";
        }
        return temp_TM;
    }
    
    public StringBuilder getIOSTREAM(){
        return this.fsb_IO_STREAM;
    }
}