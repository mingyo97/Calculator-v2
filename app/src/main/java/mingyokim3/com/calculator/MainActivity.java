package mingyokim3.com.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

//2016-09-06
//Author: Min Gyo Kim
//Project: Calculator - pretask for LaunchPad application
//Assumptions: integer is the only type of input
//             user only inputs two operands at a time with a single operator in between
//             user presses C after each computation
//Comments: user can have a maximum of 5 digits per operand and get a maximum result of 1 billion

public class MainActivity extends AppCompatActivity {
    private Integer[] numberBtn={R.id.zero,R.id.one,R.id.two,R.id.three,R.id.four,R.id.five,R.id.six,R.id.seven,R.id.eight,R.id.nine};
    private Integer[] opBtn={R.id.add,R.id.sub,R.id.mul,R.id.div};

    public static int addition=0;
    public static String add_sym="+";

    public static int substraction=1;
    public static String sub_sym="-";

    public static int multiplication=2;
    public static String mul_sym="*";

    public static int division=3;
    public static String div_sym="/";

    public static int max_digit=5;
    public static int max_result=1000000000;

    public String next_number_string;
    //initially display value and op2(second operand) strings are empty
    public String display_value="";
    public String op2_string="";

    public int btnID;
    public int opID, opIndex;
    public int number_int;
    public int op1, op2;
    public int op1_digit_count=0;
    public int op2_digit_count=0;

    //0 if flag down, 1 if flag up
    public int operation_flag=0;

    public double result_db;
    public int result_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //activated by number buttons from 0 to 9
    public void inputNum (View view){
        TextView display = (TextView) findViewById(R.id.disp);

        //get ID of the button that is pressed
        btnID=view.getId();
        //get the integer corresponding to the button that is pressed
        number_int=Arrays.asList(numberBtn).indexOf(btnID);

        //check if the integer is found in the array; it will return -1 if not found
        if(number_int!=-1) {
            //convert integer to string for display
            next_number_string = Integer.toString(number_int);

            //combine the new integer with previous integer(s) if any, for display
            display_value=display_value + next_number_string;
            //display the combined integers
            display.setText(display_value);

            //if operation_flag is set up in "inputOp", start storing the integer to the second operand as a string
            if (operation_flag==1) {
                op2_string=op2_string + next_number_string;
                //count the number of digits of the second operand to check if it exceeds max capacity in "enter"
                op2_digit_count++;
            }
            else {
                //count the number of digits of the first operand to check if it exceeds max capacity in "opInput"
                op1_digit_count++;
            }
        }
        else {
            //integer not found in array
            display.setText(R.string.notFound);
        }
    }

    //activated by the operator buttons: "+", "-", "*", or "/"
    public void inputOp (View view) {
        TextView display = (TextView) findViewById(R.id.disp);

        //check that the first operand was entered, and that the user has not already entered an operator
        if(op1_digit_count==0||operation_flag==1){
            display.setText(R.string.error);
        }
        //check that first operand does not exceed max capacity
        else if(op1_digit_count<=max_digit) {
            //store the number produced by the combination of integers typed by the user as the first operand...
            //before the user entered an operator
            op1 = Integer.parseInt(display_value);

            //set up operation_flag so that the next integers typed by the user get stored as the second operand
            operation_flag = 1;

            //get id of the operator to find its corresponding position in the opBtn array
            opID = view.getId();
            opIndex = Arrays.asList(opBtn).indexOf(opID);

            //using its position in the array, identify the operator and add the operator to the display
            if (opIndex == addition) {
                display_value = display_value + add_sym;
            } else if (opIndex == substraction) {
                display_value = display_value + sub_sym;
            } else if (opIndex == multiplication) {
                display_value = display_value + mul_sym;
            } else {
                display_value = display_value + div_sym;
            }

            //display first operand AND operator
            display.setText(display_value);
        }
        else {
            //first operand exceeds max capacity; error message
            display.setText(R.string.error);
        }
    }

    //activated by "=" button
    public void enter (View view) {
        TextView display = (TextView) findViewById(R.id.disp);

        //check that when "=" is pressed, there are two operands
        if(op1_digit_count==0||op2_digit_count==0){
            //there is at least one operand missing; error message
            display.setText(R.string.empty);
        }
        //Proceed if second operand does not exceed max capacity
        else if(op2_digit_count<=max_digit) {
            //Convert the second operand from string to int, in order to perform computations
            op2 = Integer.parseInt(op2_string);

            //Identify the operator with its position in the array and then perform the corresponding computation
            //The result is an integer for every operator except division
            if (opIndex == addition) {
                result_int = op1 + op2;
                display_value=Integer.toString(result_int);
            } else if (opIndex == substraction) {
                result_int = op1 - op2;
                display_value=Integer.toString(result_int);
            } else if (opIndex == multiplication) {
                result_int = op1 * op2;
                display_value=Integer.toString(result_int);
            } else {
                //First perform the division as double, given that we are not dividing by zero
                if (op2 != 0) {
                    result_db = (double) op1 / op2;
                    //If result%1 is 0, result is a whole number... If result%1 is NOT 0, result is a decimal number
                    //Check if the result is a decimal and that it does not exceed maximum capacity
                    if (result_db % 1 != 0) {
                        //Since result is a decimal number, result should be type double... Display the result on screen
                        display_value = Double.toString(result_db);
                    } else {
                        //Since result is a whole number, result is type integer
                        result_int = op1 / op2;
                        display_value=Integer.toString(result_int);
                    }
                } else {
                    //Cannot divide by 0; error message
                    display_value=getString(R.string.divisionError);
                }
            }

            //check that the result does not exceed one billion
            if(result_int<=max_result && result_db<=max_result) {
                display.setText(display_value);
            }else {
                //integer display exceeds maximum capacity
                display.setText(R.string.error);
            }
        }
        else {
            //Second operand exceeds max capacity
            display.setText(R.string.error);
        }
    }

    //activated by "C" button
    public void resetAll (View view) {
        TextView display = (TextView) findViewById(R.id.disp);

        //initialize all the parameters
        op2_string="";
        operation_flag=0;

        op1_digit_count=0;
        op2_digit_count=0;

        //Show empty display on screen
        display_value="";
        display.setText(display_value);
    }
}
