package com.example.rafzz.kalkulator;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * Created by rafzz on 04.01.2017.
 */


public class WriteMethodTest {

    @Test
    public void IfSignWrittenAndWritingSign() {
        MainActivity mv = new MainActivity();
        assertFalse( mv.ifNoSignWrittenAndWritingSign( " + ", true ) );
        assertTrue( mv.ifSignWrittenAndWrittingSign( " + ", true ) ); //OK
        assertFalse(mv.ifNoSignWrittenAndWrittingNoSign( " + ", true ) );
        assertTrue(mv.ifSignWrittenAndWrittingNoSign( " + ", true)); //OK, number >> sign >> number
    }

    @Test
    public void IfSignWrittenAndNotWritingNoSign(){
        MainActivity mv = new MainActivity();
        assertFalse( mv.ifNoSignWrittenAndWritingSign( "1", true ) );
        assertFalse( mv.ifSignWrittenAndWrittingSign( "1", true ) );
        assertFalse(mv.ifNoSignWrittenAndWrittingNoSign( "1", true ) );
        assertTrue(mv.ifSignWrittenAndWrittingNoSign( "1", true)); // OK
    }

    @Test
    public void IfNoSignWrittenAndWritingSign(){
        MainActivity mv = new MainActivity();
        assertTrue(mv.ifNoSignWrittenAndWritingSign( " + ", false ) ); //OK
        assertFalse( mv.ifSignWrittenAndWrittingSign( " + ", false ) );
        assertTrue(mv.ifNoSignWrittenAndWrittingNoSign( " + ", false ) ); //OK, writing sign >> number >> sign
        assertFalse(mv.ifSignWrittenAndWrittingNoSign( " + ", false));
    }

    @Test
    public void IfNoSignWrittenAndWritingNoSign(){
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifNoSignWrittenAndWritingSign( "1", false ) );
        assertFalse( mv.ifSignWrittenAndWrittingSign( "1", false ) );
        assertTrue(mv.ifNoSignWrittenAndWrittingNoSign( "1", false ) ); //OK
        assertFalse(mv.ifSignWrittenAndWrittingNoSign( "1", false));
    }
    //ifNoSignWrittenAndWritingSign
    //ifSignWrittenAndWrittingSign



    //writeDot
    @Test
    public void TrueIfWriteDot(){
        MainActivity mv = new MainActivity();
        assertTrue(mv.dotPressed("."));
    }
    @Test
    public void FalseIfWriteDot(){
        MainActivity mv = new MainActivity();
        assertFalse(mv.dotPressed(","));
    }
    //writeDot


    //ifEquationIsNotEmptyAndDotWasAlreadyWritten
    //(equation.length() > 0 && dotflag == false && equation.charAt(equation.length() - 1) != ' ')
    @Test
    public void FalseIfEquationIsEmpty(){
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationIsNotEmptyAndDotWasntWritten(false,""));
    }
    @Test
    public void FalseIfEquationLastIsSpace(){
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationIsNotEmptyAndDotWasntWritten(false," "));
    }
    @Test
    public void FalseIfDotWritten(){
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationIsNotEmptyAndDotWasntWritten(true," "));
    }
    @Test
    public void TrueIfEquationIsNotEmptyAndDotWasntWritten(){
        MainActivity mv = new MainActivity();
        assertTrue(mv.ifEquationIsNotEmptyAndDotWasntWritten(false,"12 1"));
    }

    //ifSecondMinusWasntWritten

    @Test
    public void ifSecondMinusWasntWritten(){
        MainActivity mv = new MainActivity();
        assertTrue(mv.ifSecondMinusWasntWritten(false," - ","2+2*2"));
        assertFalse(mv.ifSecondMinusWasntWritten(true," - ","2+2*2"));
        assertFalse(mv.ifSecondMinusWasntWritten(false," + ","2+2*2"));
        assertFalse(mv.ifSecondMinusWasntWritten(false," - ","2"));
    }


}
