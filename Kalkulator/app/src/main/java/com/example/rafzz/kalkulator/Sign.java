package com.example.rafzz.kalkulator;

/**
 * Created by rafzz on 30.10.2016.
 */

public enum Sign {
    ADD,
    SUBSTRACT,
    MULTIPLY,
    DIVIDE,
    EQUAL,
    POW,
    DOT;


    @Override
    public String toString(){

        switch (this) {
            case ADD:
                return "+";
            case SUBSTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            case EQUAL:
                return "=";
            case POW:
                return "^";
            case DOT:
                return ".";
        }
        return "";

    }

    public char toChar(){

        switch (this) {
            case ADD:
                return '+';
            case SUBSTRACT:
                return '-';
            case MULTIPLY:
                return '*';
            case DIVIDE:
                return '/';
            case EQUAL:
                return '=';
            case POW:
                return '^';
            case DOT:
                return '.';
        }
        return '1';

    }



    public String toSign(){

        switch (this) {
            case ADD:
                return " + ";
            case SUBSTRACT:
                return " - ";
            case MULTIPLY:
                return " * ";
            case DIVIDE:
                return " / ";
            case EQUAL:
                return " = ";
            case POW:
                return " ^ ";
            case DOT:
                return " . ";
        }
        return "";

    }





}