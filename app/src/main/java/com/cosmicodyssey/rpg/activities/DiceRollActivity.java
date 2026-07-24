package com.cosmicodyssey.rpg.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.models.Dice;
import com.cosmicodyssey.rpg.views.DiceView;

public class DiceRollActivity extends AppCompatActivity {
    private LinearLayout diceContainer;
    private TextView resultText;
    private TextView totalText;
    private Button rollBtn;
    private Button addDiceBtn;
    private Button clearBtn;

    private java.util.List<DiceView> diceViews;
    private java.util.List<Dice> diceList;
    private int currentDiceType = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_roll);

        diceContainer = findViewById(R.id.diceContainer);
        resultText = findViewById(R.id.resultText);
        totalText = findViewById(R.id.totalText);
        rollBtn = findViewById(R.id.rollBtn);
        addDiceBtn = findViewById(R.id.addDiceBtn);
        clearBtn = findViewById(R.id.clearBtn);

        diceViews = new java.util.ArrayList<>();
        diceList = new java.util.ArrayList<>();

        addDice(Dice.DiceType.D20);
        addDice(Dice.DiceType.D6);

        rollBtn.setOnClickListener(v -> rollAllDice());
        addDiceBtn.setOnClickListener(v -> showAddDiceDialog());
        clearBtn.setOnClickListener(v -> clearDice());
    }

    private void addDice(Dice.DiceType type) {
        Dice dice = new Dice(type);
        diceList.add(dice);

        DiceView diceView = new DiceView(this);
        diceView.setRarityColor(getDiceColor(type));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.setMargins(16, 16, 16, 16);
        diceView.setLayoutParams(params);

        diceView.setOnRollCompleteListener(result -> {
            dice.setResult(result);
            checkAllRolled();
        });

        diceViews.add(diceView);
        diceContainer.addView(diceView);
    }

    private void rollAllDice() {
        resultText.setText("Lancement...");
        totalText.setText("");
        rollBtn.setEnabled(false);

        for (int i = 0; i < diceViews.size(); i++) {
            final int index = i;
            diceViews.get(index).postDelayed(() -> {
                diceViews.get(index).roll(diceList.get(index).getType().getSides());
            }, i * 300);
        }
    }

    private void checkAllRolled() {
        boolean allDone = true;
        int total = 0;
        StringBuilder results = new StringBuilder();

        for (int i = 0; i < diceList.size(); i++) {
            Dice d = diceList.get(i);
            if (d.isRolling()) {
                allDone = false;
                break;
            }
            total += d.getResult();
            results.append("D").append(d.getType().getSides()).append(": ")
                   .append(d.getResult());
            if (d.isCriticalSuccess()) results.append(" ⭐ CRITIQUE!");
            if (d.isCriticalFail()) results.append(" 💀 ÉCHEC!");
            results.append("\n");
        }

        if (allDone) {
            resultText.setText(results.toString().trim());
            totalText.setText("TOTAL: " + total);
            rollBtn.setEnabled(true);

            if (diceList.stream().anyMatch(Dice::isCriticalSuccess)) {
                Toast.makeText(this, "🎉 CRITIQUE !", Toast.LENGTH_SHORT).show();
            }
            if (diceList.stream().anyMatch(Dice::isCriticalFail)) {
                Toast.makeText(this, "💀 Échec critique...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showAddDiceDialog() {
        String[] options = {"D4", "D6", "D8", "D10", "D12", "D20", "D100"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Ajouter un dé")
                .setItems(options, (dialog, which) -> {
                    Dice.DiceType[] types = {
                        Dice.DiceType.D4, Dice.DiceType.D6, Dice.DiceType.D8,
                        Dice.DiceType.D10, Dice.DiceType.D12, Dice.DiceType.D20, Dice.DiceType.D100
                    };
                    addDice(types[which]);
                })
                .show();
    }

    private void clearDice() {
        diceContainer.removeAllViews();
        diceViews.clear();
        diceList.clear();
        resultText.setText("");
        totalText.setText("");
    }

    private int getDiceColor(Dice.DiceType type) {
        switch (type) {
            case D4: return 0xFF4CAF50;
            case D6: return 0xFF2196F3;
            case D8: return 0xFF9C27B0;
            case D10: return 0xFFFF9800;
            case D12: return 0xFFFF1744;
            case D20: return 0xFF00E5FF;
            case D100: return 0xFFFFD700;
            default: return 0xFF00FF88;
        }
    }
}
