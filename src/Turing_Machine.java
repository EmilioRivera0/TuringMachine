/*-----------------------------------------------------------------------------------------------------------------------------------
- Software Name: Turing Machine
- Version: 1.0
- Language: Java
- Developer: Emilio Rivera Macías
- Date: 10/08/2022
- Contact: emilioriveramacias@gmail.com
-----------------------------------------------------------------------------------------------------------------------------------*/

// necessary includes ---------------------------->
package turing_machine;

// Turing_Machine class ---------------------------->
public class Turing_Machine {
    // main function ---------------------------->
    public static void main(String[] args) {
        // definition and initialization of the Turing Machine
        TuringMachine tm_TM = new TuringMachine("TuringMachine.txt");
        // print the Turing Machine
        System.out.println(tm_TM);
        // if block to output the user if the IO Stream was accepted or not by the automaton
        if (tm_TM.TURING_MACHINE())
            System.out.println("\nIO Stream accepted by the Turing Machine");
        else
            System.out.println("\nIO Stream not accepted by the Turing Machine");
        // output the IO Stream after the process of the Turing Machine
        System.out.println("\nIO Stream after transitions:");
        System.out.println("\"" + tm_TM.getIOSTREAM() + "\"");
    }
}
