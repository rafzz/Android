package com.example.rafzz.kalkulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import android.widget.TextView;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.*;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    public static String getExtraMessage() {
        return EXTRA_MESSAGE;
    }

    private static final String EXTRA_MESSAGE = "com.example.rafzz.kalkulator";
    private final int SIGN_LENGTH = 3;
    private final int TEXT_SIZE = 35;
    private final int WEIGHT4 = 4;
    private final int WEIGHT2 = 2;
    private final int WEIGHT3 = 3;

    protected String history = "";
    private String equation = "";

    private Sign sign;

    private boolean signFlag = false;  //true if sign was written
    private boolean dotflag = false;
    private boolean secondSignFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTextSize(TEXT_SIZE);

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
                equation.length()> SIGN_LENGTH;
    }
    //WriteValidation

    private TextView textView;
    private Button button;

    public void write( View view ) {

        textView = ( TextView ) findViewById( R.id.textView );
        button = ( Button ) view;
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

            if(ifSecondMinusWasntWritten(secondSignFlag, butText,  equation ) ) {

                equation += sign.SUBSTRACT.toChar();
                textView.setText( textView.getText().toString() + sign.SUBSTRACT.toMinus() );
                secondSignFlag =true;
                dotflag=true;

            }else{
                equation += "";
                textView.setText(textView.getText().toString() + "");
            }

        } else if ( ifNoSignWrittenAndWrittingNoSign( butText, signFlag ) ) {

            equation += button.getText().toString();
            textView.setText(textView.getText().toString() + button.getText().toString());

        } else if ( ifSignWrittenAndWrittingNoSign( butText, signFlag ) ) {

            signFlag = false;
            secondSignFlag =false;
            dotflag=false;
            equation += button.getText().toString();
            textView.setText( textView.getText().toString() + button.getText().toString() );
        }


    }

    //ResultValidation
    public boolean ifEquationEqualsEmpty( String equation ) {
        return equation.equals( "" );
    }

    public boolean ifEquationEqualsSingleSign( String equation, boolean signFlag ){
        return signFlag ||
                equation.charAt( 1 ) == sign.ADD.toChar() && equation.split(" ").length== SIGN_LENGTH ||
                equation.charAt( 1 ) == sign.SUBSTRACT.toChar() && equation.split(" ").length== SIGN_LENGTH ||
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
                equation.length()> SIGN_LENGTH ||
                equation.charAt(1)==sign.SUBSTRACT.toChar() &&
                signFlag==false &&
                equation.length()> SIGN_LENGTH;
    }
    //ResultValidation

    public void result( View view ) {

        textView = ( TextView ) findViewById( R.id.textView );

        if (ifEquationEqualsEmpty(equation) || textView.length()==1 ) { return; }

        if(!equation.contains(" ") && equation.charAt(0)==sign.SUBSTRACT.toChar()){ return; }

        if (ifEquationEqualsSingleSign( equation, signFlag ) ) { return; }

        if(ifSignOnTheBeginingOfEquation( equation,  signFlag)){

           equation =equation.substring(1,2)+equation.substring(3,equation.length());

        }

        if (ifEquationContainsNoSign( equation ) ) { return; }

        if(equation.contains(sign.DOT.toDotSpace())){ return; }


        String componentsTab[] = equation.split(" ");
        Stack stackForFacorsAndSigns = new Stack();
        Dictionary signDict = new Hashtable(); //contains weights of signs

        signDict.put( sign.POW.toString(), WEIGHT4);
        signDict.put( sign.MULTIPLY.toString(), WEIGHT3);
        signDict.put( sign.DIVIDE.toString(), WEIGHT3);
        signDict.put( sign.ADD.toString(), WEIGHT2);
        signDict.put( sign.SUBSTRACT.toString(), WEIGHT2);
        String output = ""; // constins equation formated to RPN

        for ( String component : componentsTab ) { //component >> single number or sign

            if ( stackForFacorsAndSigns.size() == 0 ) {
                try {
                    output += Double.parseDouble( component );
                } catch ( NumberFormatException e ) {
                    stackForFacorsAndSigns.push( component );
                }
            } else {
                try {
                    output += " " + Double.parseDouble( component );
                } catch ( NumberFormatException e ) {
                    while ( !stackForFacorsAndSigns.isEmpty() && ( int ) signDict.get( component ) <= ( int ) signDict.get(stackForFacorsAndSigns.peek() ) ) {
                        output += " " + stackForFacorsAndSigns.pop();
                    }
                    stackForFacorsAndSigns.push( component );

                    if ( ( int ) signDict.get( component ) > ( int ) signDict.get( stackForFacorsAndSigns.peek() ) ) {
                        stackForFacorsAndSigns.push( component );
                    }
                }
            }
        }
        for ( int i = 0; i <= stackForFacorsAndSigns.size(); i++ ) {
            output += " " + stackForFacorsAndSigns.pop();
        }
        stackForFacorsAndSigns = new Stack();

        String outTab[] = output.split( " " );

        for ( String component : outTab ) {

            try {
                stackForFacorsAndSigns.push( Double.parseDouble( component ) );
            } catch ( NumberFormatException exception ) {
                Double factorA = ( Double ) stackForFacorsAndSigns.pop(); // first component of equation
                Double factorB = ( Double ) stackForFacorsAndSigns.pop(); // second component of equation

                if ( component.equals( sign.ADD.toString() ) ) {
                    stackForFacorsAndSigns.push( factorB + factorA );
                } else if ( component.equals( sign.SUBSTRACT.toString() ) ) {
                    stackForFacorsAndSigns.push( factorB - factorA );
                } else if ( component.equals( sign.MULTIPLY.toString() ) ) {
                    stackForFacorsAndSigns.push( factorB * factorA );
                } else if ( component.equals( sign.DIVIDE.toString() ) ) {
                    stackForFacorsAndSigns.push( factorB / factorA );
                } else if ( component.equals( sign.POW.toString() ) ) {
                    stackForFacorsAndSigns.push(Math.pow( factorB, factorA ) );
                }
            }
        }
        history += equation + sign.EQUAL.toString() + stackForFacorsAndSigns.peek().toString() + "\n";

        equation = "";

        textView.setText( stackForFacorsAndSigns.peek().toString() );

        equation = textView.getText().toString();
        try {

            Double.parseDouble( equation );
            dotflag = true;

        } catch (NumberFormatException exception) {}


    }

    public void openHistory( View view ) {
        Intent intent = new Intent( this, HistoryActivity.class );
        intent.putExtra( EXTRA_MESSAGE, history);
        startActivity( intent );

    }

    public void cls( View view ) {
        dotflag = false;
        signFlag=false;
        secondSignFlag =false;
        TextView textView = ( TextView ) findViewById( R.id.textView );
        textView.setText( null );
        equation = "";
    }

    public void clsHistory( View view ) {
        history = "";
    }

}
