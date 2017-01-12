package com.example.rafzz.kalkulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import android.widget.TextView;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.*;


import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    protected static final String EXTRA_MESSAGE = "com.example.rafzz.kalkulator";
    private final int signLength = 3;
    private final int textSize = 35;
    private final int weight4 = 4;
    private final int weight2 = 2;
    private final int weight3 = 3;
    protected String hist = "";
    private String equation = "";
    private Sign sign;
    private boolean signFlag = false;  //true if sign was written
    private boolean dotflag = false;

    private boolean secondSign = false;

    private int signCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTextSize(textSize);
        signCount=0;

        Layout lay1 = new PatternLayout( "[%p] %c - %m - Data wpisu: %d %n Wątek: %t - Metoda: %M - Linia: %L - %x" );
        Appender app1 = new ConsoleAppender( lay1 );
        BasicConfigurator.configure( app1 );
        Logger logger = Logger.getRootLogger();
        logger.debug( "LOG" );
    }


    //WirteValidation
    public boolean ifNoSignWrittenAndWritingSign(String butText, boolean signFlag){

        return signFlag == false &&
                (butText.equals(sign.ADD.toSign()) ||
                 butText.equals(sign.SUBSTRACT.toSign()) ||
                 butText.equals(sign.MULTIPLY.toSign()) ||
                 butText.equals(sign.DIVIDE.toSign()) ||
                 butText.equals(sign.POW.toSign()));
    }

    public boolean ifSignWrittenAndWrittingSign( String butText, boolean signFlag ){

        return signFlag == true &&
                ( butText.equals( sign.ADD.toSign() ) ||
                butText.equals( sign.SUBSTRACT.toSign() ) ||
                butText.equals( sign.MULTIPLY.toSign() ) ||
                butText.equals( sign.DIVIDE.toSign() ) ||
                butText.equals( sign.POW.toSign() ) );
    }

    public boolean ifNoSignWrittenAndWrittingNoSign( String butText, boolean signFlag ){
        return signFlag == false &&
                ( !butText.equals( sign.ADD.toSign() ) ||
                !butText.equals( sign.SUBSTRACT.toSign() ) ||
                !butText.equals( sign.MULTIPLY.toSign() ) ||
                !butText.equals( sign.DIVIDE.toSign() ) ||
                !butText.equals( sign.POW.toSign() ) );
    }

    public boolean ifSignWrittenAndWrittingNoSign( String butText, boolean signFlag ){
        return signFlag == true &&
                ( !butText.equals( sign.ADD.toSign() ) ||
                        !butText.equals( sign.SUBSTRACT.toSign() ) ||
                        !butText.equals( sign.MULTIPLY.toSign() ) ||
                        !butText.equals( sign.DIVIDE.toSign() ) ||
                        !butText.equals( sign.POW.toSign() ) );
    }

    public boolean dotPressed( String butText ){

        return butText.equals( sign.DOT.toString() );
    }

    public boolean ifEquationIsNotEmptyAndDotWasntWritten( boolean dotflag, String equation ){
        return ( equation.length() > 0 && dotflag == false && equation.charAt( equation.length() - 1 ) != ' ' );
    }

    public boolean ifSecondMinusWasntWritten(boolean secondSign, String butText, String equation){
        return secondSign==false &&
                butText.equals( sign.SUBSTRACT.toSign()) &&
                equation.length()>signLength;
    }
    //WriteValidation


    public void write( View view ) {

        TextView textView = ( TextView ) findViewById( R.id.textView );
        Button button = ( Button ) view;
        String butText = button.getText().toString();


        if ( dotPressed( butText ) ) {
            if (ifEquationIsNotEmptyAndDotWasntWritten( dotflag, equation ) ) {

                dotflag = true;
                textView.setText( textView.getText().toString() + button.getText().toString() );
                equation += button.getText().toString();
                return;

            } else {
                return;
            }
        }

        if ( ifNoSignWrittenAndWritingSign( butText, signFlag ) ) {

            signFlag=true;
            dotflag = false;

            equation += button.getText().toString();
            textView.setText( textView.getText().toString() + button.getText().toString() );

        } else if ( ifSignWrittenAndWrittingSign( butText, signFlag ) ) {

            if(ifSecondMinusWasntWritten( secondSign, butText,  equation ) ) {

                equation += sign.SUBSTRACT.toChar();
                textView.setText( textView.getText().toString() + sign.SUBSTRACT.toMinus() );
                secondSign=true;

            }else{
                equation += "";
                textView.setText(textView.getText().toString() + "");
            }

        } else if ( ifNoSignWrittenAndWrittingNoSign( butText, signFlag ) ) {

            equation += button.getText().toString();
            textView.setText(textView.getText().toString() + button.getText().toString());

        } else if ( ifSignWrittenAndWrittingNoSign( butText, signFlag ) ) {

            signFlag = false;
            secondSign=false;
            equation += button.getText().toString();
            textView.setText( textView.getText().toString() + button.getText().toString() );
        }


    }

    //ResultValidation
    public boolean ifEquationEqualsEmpty( String equation ) {
        return equation.equals( "" );
    }

    public boolean ifEquationEqualsSingleSign( String equation, boolean signFlag ){
        //tab = equation.split(" ");
        return signFlag ||
                equation.charAt( 1 ) == sign.ADD.toChar() && equation.split(" ").length==signLength  ||
                equation.charAt( 1 ) == sign.SUBSTRACT.toChar() && equation.split(" ").length==signLength  ||
                equation.charAt( 1 ) == sign.MULTIPLY.toChar() ||
                equation.charAt( 1 ) == sign.DIVIDE.toChar() ||
                equation.charAt( 1 ) == sign.POW.toChar();
    }

    public boolean ifEquationContainsNoSign( String equation ){
        return !equation.contains( sign.ADD.toString() ) &&
                !equation.contains( sign.SUBSTRACT.toString() ) &&
                !equation.contains( sign.MULTIPLY.toString() ) &&
                !equation.contains( sign.POW.toString() ) &&
                !equation.contains( sign.DIVIDE.toString() );
    }

    public boolean ifSignOnTheBeginingOfEquation(String equation, boolean signFlag){
        return equation.charAt(1)==sign.ADD.toChar() &&
                signFlag==false &&
                equation.length()>signLength ||
                equation.charAt(1)==sign.SUBSTRACT.toChar() &&
                signFlag==false &&
                equation.length()>signLength;
    }
    //ResultValidation

    public void result( View view ) {

        TextView textView = ( TextView ) findViewById( R.id.textView );

        if (ifEquationEqualsEmpty(equation) || textView.length()==1 ) { return; }

        if (ifEquationEqualsSingleSign( equation, signFlag ) ) { return; }

        if(ifSignOnTheBeginingOfEquation( equation,  signFlag)){

           equation =equation.substring(1,2)+equation.substring(3,equation.length());

        }

        if (ifEquationContainsNoSign( equation ) ) { return; }



        String componentsTab[] = equation.split(" ");
        Stack stack = new Stack();
        Dictionary signDict = new Hashtable(); //contains weights of signs

        signDict.put( sign.POW.toString(), weight4 );
        signDict.put( sign.MULTIPLY.toString(), weight3 );
        signDict.put( sign.DIVIDE.toString(), weight3 );
        signDict.put( sign.ADD.toString(), weight2 );
        signDict.put( sign.SUBSTRACT.toString(), weight2 );
        String output = ""; // constins equation formated to RPN

        for ( String component : componentsTab ) { //component >> single number or sign

            if ( stack.size() == 0 ) {
                try {
                    output += Double.parseDouble( component );
                } catch ( NumberFormatException e ) {
                    stack.push( component );
                }
            } else {
                try {
                    output += " " + Double.parseDouble( component );
                } catch ( NumberFormatException e ) {
                    while ( !stack.isEmpty() && ( int ) signDict.get( component ) <= ( int ) signDict.get(stack.peek() ) ) {
                        output += " " + stack.pop();
                    }
                    stack.push( component );

                    if ( ( int ) signDict.get( component ) > ( int ) signDict.get( stack.peek() ) ) {
                        stack.push( component );
                    }
                }
            }
        }
        for ( int i = 0; i <= stack.size(); i++ ) {
            output += " " + stack.pop();
        }
        stack = new Stack();

        String outTab[] = output.split( " " );

        for ( String component : outTab ) {

            try {
                stack.push( Double.parseDouble( component ) );
            } catch ( NumberFormatException exception ) {
                Double a = ( Double ) stack.pop(); // first component of equation
                Double b = ( Double ) stack.pop(); // second component of equation

                if ( component.equals( sign.ADD.toString() ) ) {
                    stack.push( b + a );
                } else if ( component.equals( sign.SUBSTRACT.toString() ) ) {
                    stack.push( b - a );
                } else if ( component.equals( sign.MULTIPLY.toString() ) ) {
                    stack.push( b * a );
                } else if ( component.equals( sign.DIVIDE.toString() ) ) {
                    stack.push( b / a );
                } else if ( component.equals( sign.POW.toString() ) ) {
                    stack.push(Math.pow( b, a ) );
                }
            }
        }
        hist += equation + sign.EQUAL.toString() + stack.peek().toString() + "\n";

        equation = "";

        textView.setText( stack.peek().toString() );

        equation = textView.getText().toString();
        try {

            Double.parseDouble( equation );
            dotflag = true;

        } catch (NumberFormatException exception) {}


    }

    public void openHistory( View view ) {
        Intent intent = new Intent( this, HistoryActivity.class );
        intent.putExtra( EXTRA_MESSAGE, hist );
        startActivity( intent );

    }

    public void cls( View view ) {
        dotflag = false;
        signFlag=false;
        secondSign=false;
        TextView textView = ( TextView ) findViewById( R.id.textView );
        textView.setText( null );
        equation = "";
    }

    //ce validation
    public boolean ifEquationEmpty( String equation ){
        return equation == null || equation.length() == 0;
    }

    public boolean ifEquationLastIsDot( String equation ){
        return equation.charAt(equation.length() - 1) == sign.DOT.toChar();
    }

    public boolean ifEquationLastIsSign( String equation ) {
        return equation.charAt(equation.length() - 1) == ' ';
    }
    //ce validation

    public void ce( View view ) {
        TextView textView = ( TextView ) findViewById( R.id.textView );


        if ( ifEquationEmpty( equation ) ) {
            return;
        } else {
            if ( ifEquationLastIsDot( equation ) ) {
                dotflag = false;
            }
            if ( ifEquationLastIsSign( equation ) ) {

                equation = equation.substring( 0, equation.length() - signLength );
                textView.setText( equation );
                return;
            }
            equation = equation.substring( 0, equation.length() - 1 );
            textView.setText( equation );
        }

    }

    public void clsHistory( View view ) {
        hist = "";
    }

}
