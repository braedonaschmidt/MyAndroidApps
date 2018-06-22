package com.braedonaschmidt.chemistrycalculator;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
/*
 * Implement:
 * user variables
 * storing values with sharedPreferences?
 * option for not scientific notation
 * decimal places?
 * df for initials
 * put E where cursor is if in focus
 */

public class MainActivity extends AppCompatActivity {
    private EditText acid_I_EditText, h_I_EditText, a_I_EditText, k_A;
    private Button calculate_button, e1_button, e2_button, e3_button, e4_button;
    private TextView acid_I_TextView, acid_C, acid_E, h_I, h_C, h_E, a_I, a_C, a_E, sigFigsDisplay, rxnDirectionDisplay;
    private Switch pKa_switch, pH_switch, pA_switch, pAcid_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        acid_I_EditText = findViewById(R.id.acid_I_EditText);
        h_I_EditText = findViewById(R.id.h_I_EditText);
        a_I_EditText = findViewById(R.id.a_I_EditText);
        k_A = findViewById(R.id.k_A);

        pKa_switch = (Switch) findViewById(R.id.pKa_switch);
        pH_switch = (Switch) findViewById(R.id.pH_switch);
        pA_switch = (Switch) findViewById(R.id.pA_switch);
        pAcid_switch = (Switch) findViewById(R.id.pAcid_switch);

        e1_button = (Button) findViewById(R.id.e1_button);
        e2_button = (Button) findViewById(R.id.e2_button);
        e3_button = (Button) findViewById(R.id.e3_button);
        e4_button = (Button) findViewById(R.id.e4_button);

        calculate_button = findViewById(R.id.calculate_button);
        sigFigsDisplay = findViewById(R.id.sigFigsDisplay);
        rxnDirectionDisplay = findViewById(R.id.rxnDirectionDisplay);

        acid_I_TextView = findViewById(R.id.acid_I_TextView);
        acid_C = findViewById(R.id.acid_C);
        acid_E = findViewById(R.id.acid_E);
        h_I = findViewById(R.id.h_I);
        h_C = findViewById(R.id.h_C);
        h_E= findViewById(R.id.h_E);
        a_I = findViewById(R.id.a_I);
        a_C = findViewById(R.id.a_C);
        a_E = findViewById(R.id.a_E);

        calculate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(k_A.getText().toString()) || TextUtils.isEmpty(acid_I_EditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "You need to put values for both fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    double k_A_val = (pKa_switch.isChecked())? Math.pow(10, -Double.parseDouble(k_A.getText().toString())): Double.parseDouble(k_A.getText().toString());
                    double acid_I_val = (pAcid_switch.isChecked())? Math.pow(10, -Double.parseDouble(acid_I_EditText.getText().toString())): Double.parseDouble(acid_I_EditText.getText().toString());
                    double h_I_val = (TextUtils.isEmpty(h_I_EditText.getText().toString()))? 0: (pH_switch.isChecked())? Math.pow(10, -Double.parseDouble(h_I_EditText.getText().toString())): Double.parseDouble(h_I_EditText.getText().toString());
                    double a_I_val = (TextUtils.isEmpty(a_I_EditText.getText().toString()))? 0: (pA_switch.isChecked())? Math.pow(10, -Double.parseDouble(a_I_EditText.getText().toString())): Double.parseDouble(a_I_EditText.getText().toString());
                    boolean isForwardRxn = calcKa(acid_I_val, h_I_val, a_I_val) < k_A_val;

                    //The *-1 is for reverse reactions
                    double x = polySolv(1, (k_A_val + h_I_val + a_I_val) * (isForwardRxn? 1: -1), (h_I_val * a_I_val) - (k_A_val * acid_I_val));
                    //Setting sigFigs val to smaller #, not setting variables because it's super hard
                    int kA_sigFigs = getSigFigs(k_A.getText().toString(), pKa_switch.isChecked());
                    int acid_sigFigs = getSigFigs(acid_I_EditText.getText().toString(), false);
                    int h_sigFigs = getSigFigs(h_I_EditText.getText().toString(), false);
                    int a_sigFigs = getSigFigs(a_I_EditText.getText().toString(), false);
                    int sigFigs = (kA_sigFigs < acid_sigFigs)? kA_sigFigs: acid_sigFigs;
                    sigFigs = (sigFigs < h_sigFigs || h_sigFigs == 0)? sigFigs: h_sigFigs;
                    sigFigs = (sigFigs < a_sigFigs || a_sigFigs == 0)? sigFigs: a_sigFigs;

                    DecimalFormat df = new DecimalFormat("0.#####E0");

                    acid_I_TextView.setText(String.valueOf(acid_I_val));
                    acid_C.setText((isForwardRxn? "-": "+") + String.valueOf(df.format(x)));
                    acid_E.setText(String.valueOf(df.format(acid_I_val - (isForwardRxn? x: -x))));

                    h_I.setText(String.valueOf(h_I_val));
                    h_C.setText((isForwardRxn? "+": "-") + String.valueOf(df.format(x)));
                    h_E.setText(String.valueOf(df.format(h_I_val + (isForwardRxn? x: -x))));

                    a_I.setText(String.valueOf(a_I_val));
                    a_C.setText((isForwardRxn? "+": "-") + String.valueOf(df.format(x)));
                    a_E.setText(String.valueOf(df.format(a_I_val + (isForwardRxn? x: -x))));

                    sigFigsDisplay.setText("Sig Figs: " + String.valueOf(sigFigs));
                    rxnDirectionDisplay.setText("Rxn Direction: " + ((isForwardRxn)? "Forward": "Reverse"));
                }
            }
        });


        e1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acid_I_EditText.setText(acid_I_EditText.getText().toString() + "E");
                acid_I_EditText.setSelection(acid_I_EditText.getText().length());
            }
        });
        e2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h_I_EditText.setText(h_I_EditText.getText().toString() + "E");
                h_I_EditText.setSelection(h_I_EditText.getText().length());
            }
        });
        e3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a_I_EditText.setText(a_I_EditText.getText().toString() + "E");
                a_I_EditText.setSelection(a_I_EditText.getText().length());
            }
        });
        e4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k_A.setText(k_A.getText().toString() + "E");
                k_A.setSelection(k_A.getText().length());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            acid_I_EditText.setText("");
            h_I_EditText.setText("");
            a_I_EditText.setText("");
            k_A.setText("");

            acid_I_TextView.setText("0");
            acid_C.setText("0");
            acid_E.setText("0");

            h_I.setText("0");
            h_C.setText("0");
            h_E.setText("0");

            a_I.setText("0");
            a_C.setText("0");
            a_E.setText("0");

            sigFigsDisplay.setText("Sig Figs: ");
            rxnDirectionDisplay.setText("Rxn Direction: ");

            return true;
        }

        if (id == R.id.action_crash) {
            return 1/0 == 'c' + 'r' + 'a' + 's' + 'h';
        }

        return super.onOptionsItemSelected(item);
    }

    public double polySolv(double a, double b, double c) {
        double x1 = (-b + Math.sqrt(Math.pow(b, 2) - 4*a*c)) / (2*a);
        double x2 = (-b - Math.sqrt(Math.pow(b, 2) - 4*a*c)) / (2*a);

        if (x1 > 0 && (x2 <= 0 || x2 > x1))
            return x1;
        else if (x2 > 0 && (x1 <= 0 || x1 > x2))
            return x2;
        else
            return 0;
    }

    public int getSigFigs(String num, boolean isPKa) {
        if (isPKa)
            return num.length() - num.indexOf('.') - 1;
        else {
            //Gets rid of 0s in front
            num = num.replaceFirst("^0+(?!$)", "");

            for (int i = 0; i < num.length(); i++) {
                char current = num.charAt(i);

                if (current == 'E') {
                    num = num.substring(0, i);
                    break;
                }
            }

            boolean hasDecimal = false;
            boolean hasE = false; //This is important because it adds 1 extra for some reason when it's true
            boolean pastDec = false;
            int result = 0;
            int lastNon0Index = 0;

            //Check if has a decimal
            for (int i = 0; i < num.length(); i++) {
                int current = num.charAt(i);

                if (current == '.')
                    hasDecimal = true;
                if (current == 'E')
                    hasE = true;
                if (current != 0)
                    lastNon0Index = i;
            }


            for (int i = 0; i < num.length(); i++) {
                char current = num.charAt(i);

                if (pastDec) {
                    if (current != '0' || result > 0)
                        result++;
                } else if (current == '.')
                    pastDec = true;
                else {
                    if (current != '0' || hasDecimal || (i < lastNon0Index && result > 0))
                        result++;
                }
            }

            return result;
        }
    }

    public double calcKa(double acid, double h, double a) {
        if (h == 0 || a == 0)
            return 0;
        if (acid == 0)
            return 2147483647; //largest possible int value, change?
        return (h * a) / acid;
    }
}