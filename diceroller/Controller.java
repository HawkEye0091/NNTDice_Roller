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

    public ChoiceBox<Modifier> startingModifierBox;
    public TextArea outputArea;
    public TextArea currentModifiersField;
    @FXML private TextField omakeValueBox;

    public TextField createModifierValue;
    public TextField createModifierDescription;
    @FXML RadioButton createCriticalTrue;
    @FXML RadioButton createCriticalFalse;
    private boolean toggleCritical;

    @FXML private TextField createModifierFirstCritical;
    @FXML private TextField createModifierSecondCritical;
    @FXML private TextField createModifierThirdCritical;
    @FXML private TextField createModifierFourthCritical;
    @FXML private TextField createModifierFifthCritical;
    @FXML private TextField createModifierSixthCritical;
    @FXML private TextArea createOutputArea;

    private ArrayList<Modifier> selectedModifiers = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            startingModifierBox.getItems().addAll(Main.rollModifiers);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void switchToCreate() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("guiCreate.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ninja Ninja Tactics");
            stage.setScene(new Scene(root, 550, 400));
            stage.show();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void handleAddModifier() {
        Modifier chosenModifier = startingModifierBox.getValue();
        currentModifiersField.appendText(chosenModifier.toString() + "\n");
        selectedModifiers.add(chosenModifier);
    }

    public void handleSetOmake() {
        int value = Integer.parseInt(omakeValueBox.getText());
        Modifier omakeModifier = new Modifier(value, "Omake", false);

        selectedModifiers.add(omakeModifier);
        currentModifiersField.appendText(omakeModifier.toString() + "\n");
    }

    public void handleRollDice() {
        Roller.roll(100, selectedModifiers, this);
    }

    public void handleToggleYesAction() {
        toggleCritical = true;
    }

    public void handleToggleNoAction() {
        toggleCritical = false;
    }

    public void handleCreateModifier() {
        ArrayList<Modifier> createdModifiers = new ArrayList<>();
        if (toggleCritical) {
            ArrayList<Integer> criticalValues = new ArrayList<>();
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
            } catch (IOException | NumberFormatException e) {
                createOutputArea.appendText("Inappropriate data in modifier value field. Please enter only integers in modifier value field.\n");
            }

            System.out.println(criticalValues);
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
