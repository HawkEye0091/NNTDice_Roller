package diceroller;

import java.util.ArrayList;
import java.util.Comparator;

public class Modifier implements Comparator<Modifier> {
    private int value;
    private String description;
    private boolean hasCritical;
    private ArrayList<Integer> criticalValues = new ArrayList<>();

    public Modifier(int value, String description, boolean hasCritical) {
        this.value = value;
        this.description = description;
        this.hasCritical = hasCritical;
    }

    // multiple constructors? we gettin fancy in here
    public Modifier(int value, String description, boolean hasCritical, ArrayList<Integer> criticalValues) {
        this.value = value;
        this.description = description;
        this.hasCritical = hasCritical;
        this.criticalValues = criticalValues;
    }

    // what do you think these do?
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasCritical() {
        return hasCritical;
    }

    public void setHasCritical(boolean hasCritical) {
        this.hasCritical = hasCritical;
    }

    public int getCriticalValue(int index) {
        return criticalValues.get(index);
    }

    // I made this, then never used it
    public void setCriticalValue(ArrayList<Integer> criticalValues, int index, int newValue) {
        this.criticalValues.remove(index);
        this.criticalValues.add(index, newValue);
    }

    public ArrayList<Integer> getCriticalValues() {
        return criticalValues;
    }

    public void setCriticalValues(ArrayList<Integer> criticalValues) {
        this.criticalValues = criticalValues;
    }

    // makes it so I can print fancy descriptors of modifiers on the fly
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.getDescription() + ": " + this.getValue() + " ");

        if (this.isHasCritical()) {
            for (int i = 0; i < this.criticalValues.size(); i++) {
                stringBuilder.append(this.getCriticalValues().get(i));
                for (int k = 0; k < i + 1; k++) {
                    stringBuilder.append('*');
                }
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    // essential for sorting the modifier ArrayList alphabetically
    @Override
    public int compare(Modifier o1, Modifier o2) {
        return o1.getDescription().compareToIgnoreCase(o2.getDescription());
    }
}
