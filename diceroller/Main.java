package diceroller;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static ArrayList<Modifier> rollModifiers = new ArrayList<>();
    static File databaseFile = new File("Modifiers.txt");

    public static void main(String[] args) throws IOException {
        System.out.println("Main");
        readModifiers(rollModifiers, databaseFile);
        System.out.println(rollModifiers);

        rollModifiers.sort(new DescriptionSorter());
        System.out.println(rollModifiers);

        launch(args);
    }

    // very important: launches the GUI
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("guiMain.fxml"));
        primaryStage.setTitle("Ninja Ninja Tactics Dice Roller");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    // reads the database file and adds all the methods to the given ArrayList
    private static void readModifiers(ArrayList<Modifier> allModifiers, File dataFile) throws IOException {
        BufferedReader lineReader = new BufferedReader(new FileReader(dataFile));
        String checkLine;

        int count = 1;
        while ((checkLine = lineReader.readLine()) != null) {
            Modifier falseModifier = new Modifier(0, null, false);
            Modifier trueModifier = new Modifier(0, null, true, null);

            System.out.print(count + ", ");
            count++;

            Scanner lineProcessor = new Scanner(checkLine);
            Pattern delimiter = lineProcessor.delimiter();

            int modifierValue;
            String modifierDescription;
            boolean modifierIsCritical;
            ArrayList<Integer> modifierCriticalValues = new ArrayList<>();

            if (lineProcessor.hasNextInt()) {
                modifierValue = lineProcessor.nextInt();
                System.out.print(modifierValue + "=value ");

                falseModifier.setValue(modifierValue);
                trueModifier.setValue(modifierValue);
            }

            if (lineProcessor.hasNext()) {
                StringBuilder deleteSpace = new StringBuilder();

                lineProcessor.useDelimiter(":");
                modifierDescription = lineProcessor.next();

                deleteSpace.append(modifierDescription);
                deleteSpace.deleteCharAt(0);
                modifierDescription = deleteSpace.toString();

                lineProcessor.useDelimiter(delimiter);
                lineProcessor.next();
                System.out.print(modifierDescription + "=description ");

                falseModifier.setDescription(modifierDescription);
                trueModifier.setDescription(modifierDescription);
            }

            if (lineProcessor.hasNextBoolean()) {
                modifierIsCritical = lineProcessor.nextBoolean();
                System.out.print(modifierIsCritical + "=critical ");
                if (modifierIsCritical) {
                    trueModifier.setHasCritical(true);
                    while (lineProcessor.hasNextInt()) {
                        modifierCriticalValues.add(lineProcessor.nextInt());
                    }
                    trueModifier.setCriticalValues(modifierCriticalValues);
                    allModifiers.add(trueModifier);

                    System.out.print(modifierCriticalValues + "=critical values");
                } else {
                    falseModifier.setHasCritical(false);
                    allModifiers.add(falseModifier);
                }
            }

            System.out.println();
        }
    }

    // appends everything in the given ArrayList of modifiers to the end of the given file. In all cases, the given file is the Database.txt file.
    static void writeModifiers(ArrayList<Modifier> newModifiers, File databaseFile) throws IOException {
        FileWriter fstream = new FileWriter(databaseFile, true);
        BufferedWriter writer = new BufferedWriter(fstream);

        for (Modifier modifier : newModifiers) {
            String value = Integer.toString(modifier.getValue());
            writer.write(value);
            writer.write(" " + modifier.getDescription() + ": ");
            if (modifier.isHasCritical()) {
                writer.write("true ");
                for (int critValue : modifier.getCriticalValues()) {
                    writer.write(critValue + " ");
                }
            } else {
                writer.write("false");
            }
            writer.newLine();
        }

        writer.close();
        System.out.println("Finished writing...");
    }

    // these next two methods aren't important, they're just there so I don't have to add them back if/when I mess up and have to populate the file again.
    /**
    public static ArrayList<Modifier> getRollModifiers() {
        ArrayList<Modifier> rollModifiers = new ArrayList<>();
        populateModifiers(rollModifiers);

        return rollModifiers;
    }


    private static void populateModifiers(ArrayList<Modifier> modifierArrayList) {
        ArrayList<Integer> musicCriticalValues = new ArrayList<>();
        musicCriticalValues.add(19);

        Modifier akami = new Modifier(25, "Akami", false);
        Modifier sachi = new Modifier(25, "Sachi", false);
        Modifier learnedToPlayGames = new Modifier(5, "Learned to play games", false);
        Modifier handsome = new Modifier(16, "Handsome", false);
        Modifier manners = new Modifier(12, "Manners", false);
        Modifier aFriendlyGuy = new Modifier(15, "A Friendly Guy", false);
        Modifier raisedWithDogs = new Modifier(25, "Raised with Dogs", false);
        Modifier music = new Modifier(76, "Music", true, musicCriticalValues);
        Modifier chakraControl = new Modifier(50, "Chakra control", false);
        Modifier wise = new Modifier(51, "Wise", false);
        Modifier smart = new Modifier(32, "Smart", false);
        Modifier fit = new Modifier(61, "Fit", false);
        Modifier tactics = new Modifier(33, "Tactics", false);
        Modifier bladeSong = new Modifier(38, "Blade Song", false);
        Modifier stealth = new Modifier(74, "Stealth", false);
        Modifier poisonResistance = new Modifier(30, "Poison resistance", false);
        Modifier agility = new Modifier(10, "Agility", false);
        Modifier cooking = new Modifier(30, "Cooking", false);
        Modifier tracking = new Modifier(15, "Tracking", false);
        Modifier surgeon = new Modifier(38, "Surgeon", false);
        Modifier teacher = new Modifier(23, "Teacher", false);
        Modifier business = new Modifier(34, "Business", false);
        Modifier makingYourOwnLuck = new Modifier(10, "Making Your Own Luck", false);
        Modifier veteranOfTheEasternFront = new Modifier(5, "Veteran of the Eastern Front", false);
        Modifier sakumo = new Modifier(19, "Sakumo", false);
        Modifier haruna = new Modifier(12, "Haruna", false);
        Modifier kakashi = new Modifier(20, "Kakashi", false);
        Modifier shiroko = new Modifier(14, "Shiroko", false);
        Modifier yoruichi = new Modifier(16, "Yoruichi", false);
        Modifier yuki = new Modifier(12, "Yuki", false);
        Modifier senju = new Modifier(15, "Senju", false);
        Modifier tsunade = new Modifier(5, "Tsunade", false);
        Modifier shizune = new Modifier(11, "Shizune", false);
        Modifier nemo = new Modifier(20, "Nemo", false);
        Modifier orihime = new Modifier(14, "Orihime", false);
        Modifier hyuuga = new Modifier(8, "Hyuuga", false);
        Modifier shibue = new Modifier(16, "Shibue", false);
        Modifier anko = new Modifier(10, "Anko", false);
        Modifier mara = new Modifier(10, "Mara", false);
        Modifier chouchou = new Modifier(17, "Chouchou", false);
        Modifier uchiha = new Modifier(10, "Uchiha", false);
        Modifier rin = new Modifier(10, "Rin", false);
        Modifier teiru = new Modifier(10, "Teiru", false);
        Modifier koyuki = new Modifier(5, "Koyuki", false);
        Modifier team6 = new Modifier(30, "Team 6", false);
        Modifier kushina = new Modifier(21, "Kushina", false);
        Modifier erza = new Modifier(5, "Erza", false);
        Modifier sona = new Modifier(12, "Sona", false);
        Modifier juniper = new Modifier(13, "Juniper", false);
        Modifier miki = new Modifier(5, "Miki", false);
        Modifier momo = new Modifier(5, "Momo", false);
        Modifier gai = new Modifier(9, "Gai", false);
        Modifier ebisu = new Modifier(5, "Ebisu", false);
        Modifier chouza = new Modifier(5, "Chouza", false);
        Modifier suki = new Modifier(5, "Suki", false);
        Modifier azula = new Modifier(6, "Azula", false);
        Modifier mei = new Modifier(5, "Mei", false);
        Modifier inoue = new Modifier(5, "Inoue", false);
        Modifier sakura = new Modifier(12, "Sakura", false);
        Modifier sobieruSocial = new Modifier(24, "Sobieru (Social)", false);
        Modifier sobieruEvent = new Modifier(7, "Sobieru (Event)", false);
        Modifier mercenarySenses = new Modifier(10, "Mercenary Senses", false);

        modifierArrayList.add(akami);
        modifierArrayList.add(sachi);
        modifierArrayList.add(learnedToPlayGames);
        modifierArrayList.add(handsome);
        modifierArrayList.add(manners);
        modifierArrayList.add(aFriendlyGuy);
        modifierArrayList.add(raisedWithDogs);
        modifierArrayList.add(music);
        modifierArrayList.add(chakraControl);
        modifierArrayList.add(wise);
        modifierArrayList.add(smart);
        modifierArrayList.add(fit);
        modifierArrayList.add(tactics);
        modifierArrayList.add(bladeSong);
        modifierArrayList.add(stealth);
        modifierArrayList.add(poisonResistance);
        modifierArrayList.add(agility);
        modifierArrayList.add(cooking);
        modifierArrayList.add(tracking);
        modifierArrayList.add(surgeon);
        modifierArrayList.add(teacher);
        modifierArrayList.add(business);
        modifierArrayList.add(makingYourOwnLuck);
        modifierArrayList.add(veteranOfTheEasternFront);
        modifierArrayList.add(sakumo);
        modifierArrayList.add(haruna);
        modifierArrayList.add(kakashi);
        modifierArrayList.add(shiroko);
        modifierArrayList.add(yoruichi);
        modifierArrayList.add(yuki);
        modifierArrayList.add(senju);
        modifierArrayList.add(tsunade);
        modifierArrayList.add(shizune);
        modifierArrayList.add(nemo);
        modifierArrayList.add(orihime);
        modifierArrayList.add(hyuuga);
        modifierArrayList.add(shibue);
        modifierArrayList.add(anko);
        modifierArrayList.add(mara);
        modifierArrayList.add(chouchou);
        modifierArrayList.add(uchiha);
        modifierArrayList.add(rin);
        modifierArrayList.add(teiru);
        modifierArrayList.add(koyuki);
        modifierArrayList.add(team6);
        modifierArrayList.add(kushina);
        modifierArrayList.add(erza);
        modifierArrayList.add(sona);
        modifierArrayList.add(juniper);
        modifierArrayList.add(miki);
        modifierArrayList.add(momo);
        modifierArrayList.add(gai);
        modifierArrayList.add(ebisu);
        modifierArrayList.add(chouza);
        modifierArrayList.add(suki);
        modifierArrayList.add(azula);
        modifierArrayList.add(mei);
        modifierArrayList.add(inoue);
        modifierArrayList.add(sakura);
        modifierArrayList.add(sobieruSocial);
        modifierArrayList.add(sobieruEvent);
        modifierArrayList.add(mercenarySenses);
    }*/
}
