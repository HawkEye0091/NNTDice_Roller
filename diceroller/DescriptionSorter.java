package diceroller;

import java.util.Comparator;

public class DescriptionSorter implements Comparator<Modifier> {

    @Override
    public int compare(Modifier o1, Modifier o2) {
        return o1.getDescription().compareToIgnoreCase(o2.getDescription());
    }
}
