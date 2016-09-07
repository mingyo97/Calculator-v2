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

    public String next_number_string;
    public String display_value="";
    public String op2_string="";

    public int btnID;
    public int opID, opIndex;
    public int number_int;
    public int op1, op2;

    //0 if flag down, 1 if flag up
    public int operation_flag=0;
    public int decimal_flag=0;
    public int divisionError_flag=0;


    public double result_db;
    public int result_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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

            //add the new integer to previous integer(s), if any
            display_value=display_value + next_number_string;
            display.setText(display_value);

            //if operation_flag is set up in inputOp, start storing the integer to the second operand as a string
            if (operation_flag==1) {
                op2_string=op2_string + next_number_string;
            }
        }
        else
            display.setText("error");
    }

    public void inputOp (View view) {
        TextView display = (TextView) findViewById(R.id.disp);

        //store the number produced by the integers typed by the user as the first operand
        op1=Integer.parseInt(display_value);

        //set up operation_flag so that the next integers typed by the user get stored as the second operand
        operation_flag=1;

        //get id of the operator to find its corresponding position in the opBtn array
        opID=view.getId();
        opIndex=Arrays.asList(opBtn).indexOf(opID);

        //using its position in the array, identify the operator and add the operator to the display
        if (opIndex==addition) {
            display_value=display_value+add_sym;
        }else if (opIndex==substraction) {
            display_value=display_value+sub_sym;
        }else if (opIndex==multiplication) {
            display_value=display_value+mul_sym;
        }else {
            display_value=display_value+div_sym;
        }

        display.setText(display_value);
    }

    public void resetAll (View view) {
        TextView display = (TextView) findViewById(R.id.disp);

        //reset op2, operation_flag, modulus_flag, and display
        op2_string="";
        operation_flag=0;
        decimal_flag=0;
        display_value="";

        //Show empty display on screen
        display.setText(display_value);
    }



    public void enter (View view) {
        TextView display = (TextView) findViewById(R.id.disp);

        //Convert the second operand from string to int, in order to perform computations
        op2=Integer.parseInt(op2_string);

        //Identify the operator with its position in the array and then perform the corresponding computation
        //The result is an integer for every operator except division
        if (opIndex==addition) {
            result_int=op1+op2;
        }else if (opIndex==substraction) {
            result_int=op1-op2;
        }else if (opIndex==multiplication) {
            result_int=op1*op2;
        }else {
            //First perform the division as double
            if(op2!=0) {
                result_db = (double) op1 / op2;
                //If result%1 is 0, result is a whole number... If result%1 is NOT 0, result is a decimal number
                if(result_db%1!=0) {
                    //Since result is a decimal number, result should be type double... Display the result on screen
                    display_value = Double.toString(result_db);
                    display.setText(display_value);
                    //If the flag is up, the command to display the integer result later on will be turned off
                    decimal_flag=1;
                }else {
                    //Since result is a whole number, result is type integer
                    result_int=op1/op2;
                }
            }
            else {
                display.setText(R.string.divisionError);
                divisionError_flag=1;
            }
        }

        //Turned off if decimal value is already displayed; Turned on if result is integer
        if(decimal_flag!=1&&divisionError_flag!=1) {
            display_value = Integer.toString(result_int);
            display.setText(display_value);
            op1=result_int;
        }
    }
}
