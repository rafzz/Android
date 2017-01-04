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
    private String equation = "";
    protected String hist = "";

    private final int signLength = 3;

    private Sign sign;
    private boolean signFlag = false;  //true if sign was written
    private boolean dotflag = false;
    private final int textSize = 35;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTextSize(textSize);

        Layout lay1 = new PatternLayout("[%p] %c - %m - Data wpisu: %d %n Wątek: %t - Metoda: %M - Linia: %L - %x");
        Appender app1 = new ConsoleAppender(lay1);
        BasicConfigurator.configure(app1);
        Logger logger = Logger.getRootLogger();
        logger.debug("LOG");


    }

    public boolean ifNoSignWrittenAndWritingSign(String butText, boolean signFlag){

        return signFlag == false &&
                (butText.equals(sign.ADD.toSign()) ||
                        butText.equals(sign.SUBSTRACT.toSign()) ||
                        butText.equals(sign.MULTIPLY.toSign()) ||
                        butText.equals(sign.DIVIDE.toSign()) ||
                        butText.equals(sign.POW.toSign()));
    }

    public boolean dotPressed(String butText){

        return butText.equals(sign.DOT.toString());
    }

    public boolean ifEquationIsNotEmptyAndDotWasntWritten(boolean dotflag,String equation){
        return (equation.length() > 0 && dotflag == false && equation.charAt(equation.length() - 1) != ' ');
    }

    




    public void write(View view) {

        TextView textView = (TextView) findViewById(R.id.textView);
        Button button = (Button) view;
        String butText = button.getText().toString();


        if (dotPressed(butText)) {
            if (ifEquationIsNotEmptyAndDotWasntWritten(dotflag,equation)) {
                dotflag = true;
                textView.setText(textView.getText().toString() + button.getText().toString());
                equation += button.getText().toString();
                return;
            } else {
                return;
            }
        }

        if (ifNoSignWrittenAndWritingSign(butText,signFlag)) {
            signFlag = true;
            dotflag = false;
            equation += button.getText().toString();
            textView.setText(textView.getText().toString() + button.getText().toString());

        } else if (signFlag == true && (butText.equals(sign.ADD.toSign()) || butText.equals(sign.SUBSTRACT.toSign()) ||
                butText.equals(sign.MULTIPLY.toSign()) || butText.equals(sign.DIVIDE.toSign()) || butText.equals(sign.POW.toSign()))) {
            equation += "";
            textView.setText(textView.getText().toString() + "");

        } else if (signFlag == false && (!butText.equals(sign.ADD.toSign()) || !butText.equals(sign.SUBSTRACT.toSign()) ||
                !butText.equals(sign.MULTIPLY.toSign()) || !butText.equals(sign.DIVIDE.toSign()) || !butText.equals(sign.POW.toSign()))) {
            equation += button.getText().toString();
            textView.setText(textView.getText().toString() + button.getText().toString());

        } else if (signFlag == true && (!butText.equals(sign.ADD.toSign()) || !butText.equals(sign.SUBSTRACT.toSign()) ||
                !butText.equals(sign.MULTIPLY.toSign()) || !butText.equals(sign.DIVIDE.toSign()) || !butText.equals(sign.POW.toSign()))) {
            signFlag = false;
            equation += button.getText().toString();
            textView.setText(textView.getText().toString() + button.getText().toString());
        }


    }

    //validation
    public boolean ifEquationEqualsEmpty(String equation) {
        return equation.equals("");
    }


    public void result(View view) {

        TextView textView = (TextView) findViewById(R.id.textView);

        if (ifEquationEqualsEmpty(equation)) {
            return;
        }

        if (signFlag || equation.charAt(1) == sign.ADD.toChar() || equation.charAt(1) == sign.SUBSTRACT.toChar() ||
                equation.charAt(1) == sign.MULTIPLY.toChar() || equation.charAt(1) == sign.DIVIDE.toChar() ||
                equation.charAt(1) == sign.POW.toChar()) {
            return;
        }

        if (!equation.contains(sign.ADD.toString()) && !equation.contains(sign.SUBSTRACT.toString()) && !equation.contains(sign.MULTIPLY.toString()) &&
                !equation.contains(sign.POW.toString()) && !equation.contains(sign.DIVIDE.toString())) {
            return;
        }

        if (equation.length() > signLength && equation.charAt(1) == sign.ADD.toChar()) {
            return;
        } else if (equation.length() > signLength && equation.charAt(1) == sign.SUBSTRACT.toChar()) {
            return;
        } else if (equation.length() > signLength && equation.charAt(1) == sign.MULTIPLY.toChar()) {
            return;
        } else if (equation.length() > signLength && equation.charAt(1) == sign.DIVIDE.toChar()) {
            return;
        } else if (equation.length() > signLength && equation.charAt(1) == sign.POW.toChar()) {
            return;
        }


        String wyr = equation;
        String wyrTab[] = wyr.split(" ");
        Stack oper = new Stack();
        Dictionary dict = new Hashtable();
        dict.put(sign.POW.toString(), 4);
        dict.put(sign.MULTIPLY.toString(), 3);
        dict.put(sign.DIVIDE.toString(), 3);
        dict.put(sign.ADD.toString(), 2);
        dict.put(sign.SUBSTRACT.toString(), 2);
        String output = "";

        for (String c : wyrTab) {

            if (oper.size() == 0) {
                try {
                    output += Double.parseDouble(c);
                } catch (NumberFormatException e) {
                    oper.push(c);
                }
            } else {
                try {
                    output += " " + Double.parseDouble(c);
                } catch (NumberFormatException e) {
                    while (!oper.isEmpty() && (int) dict.get(c) <= (int) dict.get(oper.peek())) {
                        output += " " + oper.pop();
                    }
                    oper.push(c);

                    if ((int) dict.get(c) > (int) dict.get(oper.peek())) {
                        oper.push(c);
                    }
                }
            }
        }
        for (int i = 0; i <= oper.size(); i++) {
            output += " " + oper.pop();
        }
        Stack stack = new Stack();

        String outTab[] = output.split(" ");

        for (String s : outTab) {

            try {
                stack.push(Double.parseDouble(s));
            } catch (NumberFormatException e) {
                Double a = (Double) stack.pop();
                Double b = (Double) stack.pop();

                if (s.equals(sign.ADD.toString())) {
                    stack.push(b + a);
                } else if (s.equals(sign.SUBSTRACT.toString())) {
                    stack.push(b - a);
                } else if (s.equals(sign.MULTIPLY.toString())) {
                    stack.push(b * a);
                } else if (s.equals(sign.DIVIDE.toString())) {
                    stack.push(b / a);
                } else if (s.equals(sign.POW.toString())) {
                    stack.push(Math.pow(b, a));
                }
            }
        }
        hist += equation + sign.EQUAL.toString() + stack.peek().toString() + "\n";

        equation = "";

        textView.setText(stack.peek().toString());
        equation = textView.getText().toString();
        try {
            Double.parseDouble(equation);
            dotflag = true;
        } catch (NumberFormatException e) {
        }


    }

    public void openHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra(EXTRA_MESSAGE, hist);
        startActivity(intent);

    }

    public void cls(View view) {
        dotflag = false;
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(null);
        equation = "";


    }

    public void ce(View view) {
        TextView textView = (TextView) findViewById(R.id.textView);

        if (equation == null || equation.length() == 0) {
            return;
        } else {
            if (equation.charAt(equation.length() - 1) == sign.DOT.toChar()) {
                dotflag = false;
            }
            if (equation.charAt(equation.length() - 1) == ' ') {
                equation = equation.substring(0, equation.length() - 3);
                textView.setText(equation);
                return;
            }
            equation = equation.substring(0, equation.length() - 1);
            textView.setText(equation);
        }

    }

    public void clsHistory(View view) {
        hist = "";
    }

}
