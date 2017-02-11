package com.example.rafzz.kalkulator;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Stack;

/**
 * Created by rafzz on 07.02.2017.
 */

public class ReversedPolishNotation {

    private static final int WEIGHT4 = 4;
    private static final int WEIGHT2 = 2;
    private static final int WEIGHT3 = 3;
    
    private static Sign sign;

    public static String countInRpn(String equation){

        String componentsTab[] = equation.split(" ");
        Stack stackOfFacorsSignsAndResult = new Stack();
        Dictionary signAndWeightsDict = new Hashtable();

        signAndWeightsDict.put( sign.POW.toString(), WEIGHT4);
        signAndWeightsDict.put( sign.MULTIPLY.toString(), WEIGHT3);
        signAndWeightsDict.put( sign.DIVIDE.toString(), WEIGHT3);
        signAndWeightsDict.put( sign.ADD.toString(), WEIGHT2);
        signAndWeightsDict.put( sign.SUBSTRACT.toString(), WEIGHT2);
        String outputEquation = "";

        for ( String component : componentsTab ) { //component >> single number or sign

            if ( stackOfFacorsSignsAndResult.size() == 0 ) {
                try {
                    outputEquation += Double.parseDouble( component );
                } catch ( NumberFormatException e ) {
                    stackOfFacorsSignsAndResult.push( component );
                }
            } else {
                try {
                    outputEquation += " " + Double.parseDouble( component );
                } catch ( NumberFormatException e ) {
                    while ( !stackOfFacorsSignsAndResult.isEmpty() &&
                            ( int ) signAndWeightsDict.get( component ) <= ( int ) signAndWeightsDict.get(stackOfFacorsSignsAndResult.peek() ) ) {
                        outputEquation += " " + stackOfFacorsSignsAndResult.pop();
                    }
                    stackOfFacorsSignsAndResult.push( component );

                    if ( ( int ) signAndWeightsDict.get( component ) > ( int ) signAndWeightsDict.get( stackOfFacorsSignsAndResult.peek() ) ) {
                        stackOfFacorsSignsAndResult.push( component );
                    }
                }
            }
        }
        for ( int i = 0; i <= stackOfFacorsSignsAndResult.size(); i++ ) {
            outputEquation += " " + stackOfFacorsSignsAndResult.pop();
        }
        stackOfFacorsSignsAndResult = new Stack();

        String outputTab[] = outputEquation.split( " " );

        for ( String component : outputTab ) {

            try {
                stackOfFacorsSignsAndResult.push( Double.parseDouble( component ) );
            } catch ( NumberFormatException exception ) {
                Double factorA = ( Double ) stackOfFacorsSignsAndResult.pop();
                Double factorB = ( Double ) stackOfFacorsSignsAndResult.pop();

                if ( component.equals( sign.ADD.toString() ) ) {
                    stackOfFacorsSignsAndResult.push( factorB + factorA );
                } else if ( component.equals( sign.SUBSTRACT.toString() ) ) {
                    stackOfFacorsSignsAndResult.push( factorB - factorA );
                } else if ( component.equals( sign.MULTIPLY.toString() ) ) {
                    stackOfFacorsSignsAndResult.push( factorB * factorA );
                } else if ( component.equals( sign.DIVIDE.toString() ) ) {
                    stackOfFacorsSignsAndResult.push( factorB / factorA );
                } else if ( component.equals( sign.POW.toString() ) ) {
                    stackOfFacorsSignsAndResult.push(Math.pow( factorB, factorA ) );
                }
            }
        }

        MainActivity.history += equation + sign.EQUAL.toString() + stackOfFacorsSignsAndResult.peek().toString() + "\n";
        return stackOfFacorsSignsAndResult.peek().toString();

    }
}
