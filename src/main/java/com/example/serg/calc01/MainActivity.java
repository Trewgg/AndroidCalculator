package com.example.serg.calc01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.serg.calc01.enums.OperationType;
import com.example.serg.calc01.enums.Symbol;

import java.util.EnumMap;

public class MainActivity extends AppCompatActivity {

    public static final String ZERO_TEXT = "0";
    private EditText txtResult;
    private Button btnDivide;
    private Button btnMultiply;
    private Button btnMinus;
    private Button btnPlus;
    private OperationType operationType;

    //хранит введенные данные
    private EnumMap<Symbol, Object> commands = new EnumMap<>(Symbol.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = (EditText) findViewById(R.id.txtResult);
        btnDivide = (Button) findViewById(R.id.btnDivide);
        btnMultiply = (Button) findViewById(R.id.btnMultiply);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnPlus = (Button) findViewById(R.id.btnPlus);

        //каждой кнопке добавить тип операции
        btnDivide.setTag(OperationType.DIVIDE);
        btnMultiply.setTag(OperationType.MULTIPLY);
        btnMinus.setTag(OperationType.MINUS);
        btnPlus.setTag(OperationType.PLUS);
    }

    public void buttonOperationClick(View view) {

        operationType = (OperationType) view.getTag();

        if (!commands.containsKey(Symbol.OPERATION)) {
            if (!commands.containsKey(Symbol.FIRST_DIGIT)) {
                commands.put(Symbol.FIRST_DIGIT, txtResult.getText());
            }
            commands.put(Symbol.OPERATION, operationType);
        } else if (!commands.containsKey(Symbol.SECOND_DIGIT)) {
            commands.put(Symbol.SECOND_DIGIT, txtResult.getText());
        }

        txtResult.setText("");

    }

    public void buttonDigitClick(View view) {

        txtResult.setText(txtResult.getText() + view.getContentDescription().toString());

    }

    public void buttonResultClick(View view) {

        if (commands.containsKey(Symbol.FIRST_DIGIT) && commands.containsKey(Symbol.OPERATION)) {
            commands.put(Symbol.SECOND_DIGIT, txtResult.getText());
            doCalc();
            commands.clear();
        }

    }

    public void buttonClearClick(View view) {
        txtResult.setText("");
        commands.clear();
    }

    private void doCalc() {

        OperationType operTypeTmp = (OperationType) commands.get(Symbol.OPERATION);

        double result;

        try {
            result = calc(operTypeTmp,
                    getDouble(commands.get(Symbol.FIRST_DIGIT)),
                    getDouble(commands.get(Symbol.SECOND_DIGIT)));

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (result % 1 == 0) {
            txtResult.setText(String.valueOf((int) result));
        } else {
            txtResult.setText(String.valueOf(result));
        }

    }

    private double getDouble(Object value) {
        double result;
        try {
            result = Double.valueOf(value.toString().replace(',', '.'));
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }

        return result;
    }

    private Double calc(OperationType operType, double a, double b) {
        switch (operType) {
            case PLUS: {
                return CalcOperations.add(a, b);
            }
            case DIVIDE: {
                return CalcOperations.divide(a, b);
            }
            case MULTIPLY: {
                return CalcOperations.multiply(a, b);
            }
            case MINUS: {
                return CalcOperations.subtract(a, b);
            }
        }

        return null;
    }
}
