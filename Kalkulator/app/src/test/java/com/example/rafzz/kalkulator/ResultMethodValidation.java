package com.example.rafzz.kalkulator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rafzz on 04.01.2017.
 */

public class ResultMethodValidation {

    @Test
    public void equationEmpty() {
        MainActivity mv = new MainActivity();
        assertTrue(mv.ifEquationEqualsEmpty(""));
    }

    @Test
    public void equationIsNotEmpty() {
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationEqualsEmpty("2+2"));
    }

    @Test
    public void equationIsNotSingleSign() {
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationEqualsSingleSign(" 2 ", false));


    }

    @Test
    public void equationIsSingleSign() {
        MainActivity mv = new MainActivity();
        assertTrue(mv.ifEquationEqualsSingleSign(" + ", true));
        assertTrue(mv.ifEquationEqualsSingleSign(" + ", false));
        assertTrue(mv.ifEquationEqualsSingleSign(" 2 ", true));
    }

    //ifEquationContainsNoSign

    @Test
    public void ifEquationContainsNoSign() {
        MainActivity mv = new MainActivity();
        assertTrue(mv.ifEquationContainsNoSign("2323"));

    }

    @Test
    public void ifEquationContainsSign() {
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationContainsNoSign("233 + 23"));

    }

    //ifEquationEmpty

    @Test
    public void ifEquationEmpty() {
        MainActivity mv = new MainActivity();
        assertTrue(mv.ifEquationEmpty(""));
        assertTrue(mv.ifEquationEmpty(new String()));

    }

    @Test
    public void ifEquationIsNotEmpty() {
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationEmpty("dfd"));


    }

    //ifEquationLastSignIsDot

    @Test
    public void ifEquationLastIsDot() {
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationLastIsDot("dfd"));
        assertTrue(mv.ifEquationLastIsDot("dfd."));


    }
    //ifEquationLastIsSign

    @Test
    public void ifEquationLastIsSign() {
        MainActivity mv = new MainActivity();
        assertFalse(mv.ifEquationLastIsSign("dfd"));
        assertTrue(mv.ifEquationLastIsSign("dfd * "));


    }


}
