package com.example.rafzz.kalkulator;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * Created by rafzz on 04.01.2017.
 */


public class WriteMethodValidation {

    //ifNoSignWrittenAndWritingSign
    @Test
    public void FalseIfSignWrittenAndWritingSign(){
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifNoSignWrittenAndWritingSign(" + ",true));
    }

    @Test
    public void FalseIfSignWrittenAndNotWritingNoSign(){
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifNoSignWrittenAndWritingSign("1",true));
    }

    @Test
    public void TrueIfNoSignWrittenAndWritingSign(){
        MainActivity mv = new MainActivity();
        assertTrue(mv.ifNoSignWrittenAndWritingSign(" + ",false));
    }

    @Test
    public void TrueIfNoSignWrittenAndWritingNoSign(){
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifNoSignWrittenAndWritingSign("1",false));
    }
    //ifNoSignWrittenAndWritingSign


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


}
