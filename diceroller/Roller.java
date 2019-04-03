package diceroller;

import java.util.ArrayList;
import java.util.Random;

public class Roller {
    public static void roll(int dice, ArrayList<Modifier> startModifiers, Controller controller) {
        ArrayList<Integer> rolls = new ArrayList<>();
        ArrayList<ArrayList<Modifier>> explosionModifiers = new ArrayList<>();
        ArrayList<Integer> rollSums = new ArrayList<>();

        Random random = new Random();
        explosionModifiers.add(startModifiers);
        explosionModifiers.add(new ArrayList<>());

        // Checks the first line of modifiers for critical modifiers. If found, adds all the critical values of that modifier to the appropriate critical lines.
        int criticalSize;
        for (Modifier checkCritical : explosionModifiers.get(0)) {
            if (checkCritical.isHasCritical()) {
                criticalSize = checkCritical.getCriticalValues().size();
                for (int i = 0; i < criticalSize; i++) {
                    if (explosionModifiers.size() == i + 1) {
                        explosionModifiers.get(i + 1).add(new Modifier(checkCritical.getCriticalValue(i), checkCritical.getDescription(), true));
                    } else {
                        explosionModifiers.add(i + 1, new ArrayList<>());
                        explosionModifiers.get(i + 1).add(new Modifier(checkCritical.getCriticalValue(i), checkCritical.getDescription(), true));
                    }
                }
            }
        }

        // rolls dice
        int roll = random.nextInt(dice) + 1;
        rolls.add(roll);

        // if first dice roll is natural 100, adds appropriate modifier.
        if (roll == 100) {
            explosionModifiers.get(0).add(new Modifier(20, "Natural Crit", false));
        }

        // finds first roll sum.
        int rollSum = roll;
        for (Modifier modifier : explosionModifiers.get(0)) {
            rollSum += modifier.getValue();
        }
        rollSums.add(rollSum);

        // repeats previous code if and while the roll sum of the previous line is above 100. Essentially, this is the code that allows for explosive dice.
        int counter = 1;
        while (rollSums.get(counter - 1) >= 100) {
            if (explosionModifiers.get(counter).isEmpty()) {
                explosionModifiers.add(counter, new ArrayList<>());
            }

            if (rollSums.get(counter - 1) > 100) {
                explosionModifiers.get(counter).add(new Modifier(rollSums.get(counter - 1) - 100, "Explosion", false));
            }

            rolls.add(random.nextInt(dice) + 1);
            if (rolls.get(counter) == 100) {
                explosionModifiers.get(counter).add(new Modifier(20, "Natural Crit", false));
            }

            int sum = rolls.get(counter);
            for (Modifier modifier : explosionModifiers.get(counter)) {
                sum += modifier.getValue();
            }
            rollSums.add(sum);

            counter++;
        }

        // human readable output
        StringBuilder appendStars = new StringBuilder();
        for (int i = 0; i < rolls.size(); i++) {
            if (i >= 1) {
                appendStars.append('*');
            }

            if (i == 0) {
                controller.outputArea.appendText("QM Roll 1 = " + rolls.get(i));
                for (Modifier modifier : explosionModifiers.get(i)) {
                    controller.outputArea.appendText("+" + modifier.getValue() + "(" + modifier.getDescription() + ")");
                }
                controller.outputArea.appendText("=" + rollSums.get(i) + "\n");
            } else if (i == 1) {
                controller.outputArea.appendText("Crit Roll = " + rolls.get(i));
                for (Modifier modifier : explosionModifiers.get(i)) {
                    if (modifier.isHasCritical()) {
                        controller.outputArea.appendText("+" + modifier.getValue() + appendStars + "(" + modifier.getDescription() + ")");
                    } else {
                        controller.outputArea.appendText("+" + modifier.getValue() + "(" + modifier.getDescription() + ")");
                    }
                }
                controller.outputArea.appendText("=" + rollSums.get(i) + appendStars + "\n");
            } else {
                controller.outputArea.appendText("Crit Roll " + (i) + " = " + rolls.get(i));
                for (Modifier modifier : explosionModifiers.get(i)) {
                    if (modifier.isHasCritical()) {
                        controller.outputArea.appendText("+" + modifier.getValue() + appendStars + "(" + modifier.getDescription() + ")");
                    } else {
                        controller.outputArea.appendText("+" + modifier.getValue() + "(" + modifier.getDescription() + ")");
                    }
                }
                controller.outputArea.appendText("=" + rollSums.get(i) + appendStars + "\n");
            }
        }

        // finds the total and prints it.
        int totalSum = 0;
        for (int sums : rollSums) {
            totalSum += sums;
        }
        controller.outputArea.appendText("Final =" + totalSum + "\n\n");
    }
}
