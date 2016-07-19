package com.example.dilippashi.sillycalc;

/**
 * Created by Dilippashi on 29-06-2016.
 */
public class Operations {
    // operator types
    public static final String ADD = "+";
    public static final String SUBTRACT = "-";
    public static final String MULTIPLY = "x";
    public static final String DIVIDE = "÷";
    public static final String CLEAR = "C";
    public static final String CLEARMEMORY = "MC";
    public static final String ADDTOMEMORY = "M+";
    public static final String SUBTRACTFROMMEMORY = "M-";
    public static final String RECALLMEMORY = "MR";
    public static final String SQUAREROOT = "√";
    public static final String SQUARED = "x²";
    public static final String INVERT = "1/x";
    public static final String TOGGLESIGN = "+/-";
    public static final String SINE = "sin";
    public static final String COSINE = "cos";
    public static final String TANGENT = "tan";
    // 3 + 6 = 9
    // 3 & 6 are called the operand.
    // The + is called the operator.
    // 9 is the result of the operation.
    public double mOperand;
    private double mWaitingOperand;
    private String mWaitingOperator;
    private double mCalculatorMemory;

    // public static final String EQUALS = "=";

    // constructor
    public Operations() {
        // initialize variables upon start
        mOperand = 0;
        mWaitingOperand = 0;
        mWaitingOperator = "";
        mCalculatorMemory = 0;

    }

    public void setOperand(double operand) {
        mOperand = operand;
    }

    public double getResult() {
        return mOperand;
    }

    // used on screen orientation change
    public double getMemory() {
        return mCalculatorMemory;
    }

    // used on screen orientation change
    public void setMemory(double calculatorMemory) {
        mCalculatorMemory = calculatorMemory;
    }

    public String toString() {
        return Double.toString(mOperand);
    }

    protected double performOperation(String operator) {

        switch (operator) {
            case CLEAR:
                mOperand = 0;
                mWaitingOperator = "";
                mWaitingOperand = 0;
                break;
            case CLEARMEMORY:
                mCalculatorMemory = 0;
                break;
            case ADDTOMEMORY:
                mCalculatorMemory = mCalculatorMemory + mOperand;
                break;
            case SUBTRACTFROMMEMORY:
                mCalculatorMemory = mCalculatorMemory - mOperand;
                break;
            case RECALLMEMORY:
                mOperand = mCalculatorMemory;
                break;
            case SQUAREROOT:
                mOperand = Math.sqrt(mOperand);
                break;
            case SQUARED:
                mOperand = mOperand * mOperand;
                break;
            case INVERT:
                if (mOperand != 0) {
                    mOperand = 1 / mOperand;
                } else {
                    mOperand = 0;
                }
                break;
            case TOGGLESIGN:
                if (mOperand > 0) {
                    mOperand = -mOperand;
                } else if (mOperand < 0) {
                    mOperand *= -1;
                } else
                    mOperand = 0;
                break;
            case SINE:
                mOperand = Math.sin(Math.toRadians(mOperand));
                break;
            case COSINE:
                mOperand = Math.cos(Math.toRadians(mOperand));
                break;
            case TANGENT:
                mOperand = Math.tan(Math.toRadians(mOperand));
                break;
            default:
                performWaitingOperation();
                mWaitingOperand = mOperand;
                mWaitingOperator = operator;
                break;

        }
        return mOperand;
    }

    protected void performWaitingOperation() {

        switch (mWaitingOperator) {
            case ADD:
                mOperand = mWaitingOperand + mOperand;
                break;
            case MULTIPLY:
                mOperand = mWaitingOperand * mOperand;
                break;
            case SUBTRACT:
                mOperand = mWaitingOperand - mOperand;
                break;
            case DIVIDE:
                mOperand = mWaitingOperand / mOperand;
                break;
        }
    }
}