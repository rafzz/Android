package com.example.rafzz.kalkulator;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by rafzz on 04.01.2017.
 */

public class ResultMethodValidation {

    @Test
    public void equationEmpty(){
        MainActivity mv = new MainActivity();
        assertTrue(mv.ifEquationEqualsEmpty(""));
    }

    @Test
    public void equationIsNotEmpty(){
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationEqualsEmpty("2+2"));
    }
}
