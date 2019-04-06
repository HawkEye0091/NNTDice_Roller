package diceroller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private ChoiceBox<Modifier> startingModifierBox;
    @FXML private TextArea outputArea;
    @FXML private TextArea currentModifiersField;
    @FXML private TextField temporaryValueBox;
    @FXML private TextField temporaryDescriptionBox;

    @FXML private TextField createModifierValue;
    @FXML private TextField createModifierDescription;
    @FXML RadioButton createCriticalTrue;
    @FXML RadioButton createCriticalFalse;
    @FXML private TextField createModifierFirstCritical;
    @FXML private TextField createModifierSecondCritical;
    @FXML private TextField createModifierThirdCritical;
    @FXML private TextField createModifierFourthCritical;
    @FXML private TextField createModifierFifthCritical;
    @FXML private TextField createModifierSixthCritical;
    @FXML private TextArea createOutputArea;
    private boolean toggleCritical;

    private ArrayList<Modifier> selectedModifiers = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Modifier>>> overList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // adds all the modifiers in Modifiers.txt to the drop down box. Also the source of all misery and pain... for some reason
            startingModifierBox.getItems().addAll(Main.rollModifiers);
            // checks to see if the overList has already had an element added, and if not, adds one. This is important to other code, since the first element is dynamic but isn't added elsewhere
            if (overList.size() == 0) {
                overList.add(new ArrayList<>());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // I don't know why this is terrible, but it is
    public void switchToCreate() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("guiCreate.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ninja Ninja Tactics Dice Roller");
            stage.setScene(new Scene(root, 550, 400));
            stage.show();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    // allows the user to add modifiers from the drop down box to the proper ArrayList and shows what modifier has been added in the correct field
    public void handleAddModifier() {
        Modifier chosenModifier = startingModifierBox.getValue();
        currentModifiersField.appendText(chosenModifier.toString() + "\n");
        selectedModifiers.add(chosenModifier);
    }

    // if the user presses the Add Roll button, this gets the first element of the overList ArrayList (which is an ArrayList of ArrayLists of Modifiers... I think) and creates an ArrayList of Modifiers
    // then it adds all the currently selected modifiers into this new ArrayList and clears the selected modifiers to make way for a possible new roll
    public void handleMultiRoll() {
        overList.get(0).add(new ArrayList<>());
        overList.get(0).get(overList.get(0).size() - 1).addAll(selectedModifiers);
        selectedModifiers.clear();

        // this is just for understandability, basically it checks to see if the user is adding an empty roll (just d100) to the multiRoll, and says so if they are
        if (overList.get(0).get(overList.get(0).size() - 1).isEmpty()) {
            currentModifiersField.appendText("  -Empty Roll Added-\n");
        } else {
            currentModifiersField.appendText("  -Roll Added-\n");
        }
    }

    // clears all the modifiers currently selected (not added to multiRoll) and creates a new instance of overList in the first (0) index, pushing the now defunct multi roll out of the way
    // I've just realized this is a terrible way to do this and the way I had it before was probably closer to a real solution then this is, but I don't want to rewrite it for the third time
    public void handleClearModifiers() {
    	selectedModifiers.clear();
    	currentModifiersField.clear();
    	overList.add(0, new ArrayList<>());
    }

    // clears the output area
    public void handleClearOutput() {
        outputArea.clear();
    }

    // combines the two methods above into one... for the lazy people. Note that this is only lazy because you can freely modify the output area, including deleting things
    public void handleClearAll() {
        handleClearModifiers();
        handleClearOutput();
    }

    // creates a new modifier based off the values in the temporary value and description boxes, then adds that modifier to the selected modifiers
    public void handleAddTemporary() {
    	Modifier temporaryModifier = new Modifier(Integer.parseInt(temporaryValueBox.getText()), temporaryDescriptionBox.getText(), false);
    	selectedModifiers.add(temporaryModifier);
    	currentModifiersField.appendText(temporaryModifier.toString() + "\n");
    }

    // checks to see if multi roll is being used. if so, goes through the proper ArrayList and rolls all the rolls in it. Otherwise, just rolls a normal d100 with no modifiers
    public void handleRollDice() {
        if (overList.get(0).size() != 0) {
            for (ArrayList<Modifier> list : overList.get(0)) {
                Roller.roll(100, list, outputArea);
            }
        } else {
            Roller.roll(100, selectedModifiers, outputArea);
        }
    }

    // checks to see if the modifier being created is set up to have critical values. This method is tripped if the answer is yes, which sets the proper boolean to true
    public void handleToggleYesAction() {
        toggleCritical = true;
    }

    // same as above, but reverse
    public void handleToggleNoAction() {
        toggleCritical = false;
    }

    // method to handle the creation of new modifiers which are to be written to the Modifiers.txt file
    // why is it so hard to read
    public void handleCreateModifier() {
        // I originally had it use this ArrayList because I wanted the user to be able to create multiple modifiers at once, but now the other side only receives it as an ArrayList and this is easier
        ArrayList<Modifier> createdModifiers = new ArrayList<>();

        // this part is only if the modifier has critical values
        if (toggleCritical) {
            ArrayList<Integer> criticalValues = new ArrayList<>();
            // this horrible piece of code checks to see if a critical value has been entered, and gets it if it has
            // I think this also allows the user to create a modifier with critical values that aren't in sequence, but I'm not sure. I'm also not sure if that's a feature or a bug
            if (!createModifierFirstCritical.getText().equals("")) {
                criticalValues.add(Integer.parseInt(createModifierFirstCritical.getText()));
            }
            if (!createModifierSecondCritical.getText().equals("")) {
                criticalValues.add(Integer.parseInt(createModifierSecondCritical.getText()));
            }
            if (!createModifierThirdCritical.getText().equals("")) {
                criticalValues.add(Integer.parseInt(createModifierThirdCritical.getText()));
            }
            if (!createModifierFourthCritical.getText().equals("")) {
                criticalValues.add(Integer.parseInt(createModifierFourthCritical.getText()));
            }
            if (!createModifierFifthCritical.getText().equals("")) {
                criticalValues.add(Integer.parseInt(createModifierFifthCritical.getText()));
            }
            if (!createModifierSixthCritical.getText().equals("")) {
                criticalValues.add(Integer.parseInt(createModifierSixthCritical.getText()));
            }

            // an actual bit of semi-decent code that has error checking and everything
            // basically gets the other pieces of data that are essential (unlike critical values) and creates a new modifier using them, then writes that new modifier to the text file
            try {
                int modValue;
                String modDescription;
                if (!createModifierValue.getText().equals("") && !createModifierDescription.getText().equals("")) {
                    modValue = Integer.parseInt(createModifierValue.getText());
                    modDescription = createModifierDescription.getText();

                    Modifier newModifier = new Modifier((modValue), modDescription, true, criticalValues);
                    createdModifiers.add(newModifier);
                    createOutputArea.appendText(newModifier + " - True\n");

                    Main.writeModifiers(createdModifiers, Main.databaseFile);
                } else if (createModifierValue.getText().equals("")) {
                    createOutputArea.appendText("Please enter a modifier value...\n");
                } else if (createModifierDescription.getText().equals("")) {
                    createOutputArea.appendText("Please enter a modifier description...\n");
                }

            // error catching! huzzah!
            } catch (IOException | NumberFormatException e) {
                createOutputArea.appendText("Inappropriate data in modifier value field. Please enter only integers in modifier value field.\n");
            }

        // the bit of code that does the same thing as above, but without critical values
        } else {
            try {
                int modValue;
                String modDescription;
                if (!createModifierValue.getText().equals("") && !createModifierDescription.getText().equals("")) {
                    modValue = Integer.parseInt(createModifierValue.getText());
                    modDescription = createModifierDescription.getText();

                    Modifier newModifier = new Modifier((modValue), modDescription, false);
                    createdModifiers.add(newModifier);
                    createOutputArea.appendText(newModifier + " - False\n");

                    Main.writeModifiers(createdModifiers, Main.databaseFile);
                } else if (createModifierValue.getText().equals("")) {
                    createOutputArea.appendText("Please enter a modifier value...\n");
                } else if (createModifierDescription.getText().equals("")) {
                    createOutputArea.appendText("Please enter a modifier description...\n");
                }
            } catch (IOException | NumberFormatException e) {
                createOutputArea.appendText("Inappropriate data in modifier value field. Please enter only integers in modifier value field.\n");
            }
        }
    }
}
