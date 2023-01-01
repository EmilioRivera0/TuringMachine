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

// Transition class ---------------------------->
public class Transition {
    // field declaration ---------------------------->
    // 0 moves the TM I/O header to the left and 1 to the right
    private final char fb_Direction;
    // future state to be moved to after this transition rule
    private final String fs_State;
    // symbol to be outputed by the I/O header
    private final char fc_Output;
    
    // declaration and definition of methods ---------------------------->
    /*-----------------------------------------------------------------------------------------------------------------------------------
    - Functionality: Constructor to create  a Transition object
    -----------------------------------------------------------------------------------------------------------------------------------*/
    public Transition(char output, String state, char direction){
        this.fc_Output = output;
        this.fs_State = state;
        this.fb_Direction = direction;
    }
    
    // toString and getters ---------------------------->
    @Override
    public String toString(){
        return "Output: \"" + this.fc_Output + "\" Next State: \"" + this.fs_State + "\" Move: \"" + this.fb_Direction + "\"";
    }
    
    public char getFb_Direction() {
        return fb_Direction;
    }

    public char getFc_Output() {
        return fc_Output;
    }

    public String getFs_State() {
        return fs_State;
    }    
}