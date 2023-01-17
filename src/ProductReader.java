import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * @author lohbecjz@mail.uc.edu
 */

public class ProductReader
{
    static Scanner console = new Scanner(System.in);
    static String id = "";
    static String name = "";
    static String description = "";
    static double cost = 0;
    static boolean needsToBeSaved = false;
    static ArrayList<String> lines = new ArrayList<>();
    static boolean confirm = false;
    static boolean saveChoice = false;
    static String fileList;
    static String fileName = "";
    static String costConvert;

    public static void main(String[] args)
    {
        final String menu = "[A]-Add [S]-Save [Q]-Quit ";
        boolean done = false;
        String cmd;

        do
        {
            // display the menu options
            // get a menu choice
            cmd = SafeInput.getRegExString(console, menu, "[AaVvSsQq]");
            cmd = cmd.toUpperCase();

            // execute the choice
            switch(cmd)
            {
                case "A":
                    addItem();
                    break;
                case "S":
                    saveFile();
                    break;
                case "Q":
                    quit();
                    break;
            }

        }

        while(!done);

    }

    // prompt the user for a list item
    // make sure that it is not an empty string (getNonZeroLengthString)
    // add it to the list
    private static void addItem()
    {
        id = SafeInput.getNonZeroLenString(console, "Enter ID number ");
        name = SafeInput.getNonZeroLenString(console, "Enter product name ");
        description = SafeInput.getNonZeroLenString(console, "Enter product description ");
        cost = SafeInput.getDouble(console, "Enter cost ");
        costConvert = Double.toString(cost);
        System.out.println("");
        fileList = String.join(", ", id, name, description, costConvert);
        lines.add(fileList);
        System.out.println("List item added!");
        System.out.println("");
        needsToBeSaved = true;
    }

    // save the current list file to disk
    // replace the previous version if is present
    private static void saveFile()
    {
        File workingDirectory = new File(System.getProperty("user.dir"));
        if (Objects.equals(fileName, ""))
        {
            fileName = "ProductTestData.txt";
        }
        Path file = Paths.get(workingDirectory.getPath() + "\\src\\" + fileName);

        try
        {
            Files.deleteIfExists(file);
            OutputStream out = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

            for(String scribe : lines)
            {
                writer.write(scribe, 0 , scribe.length());
                writer.newLine();
            }

            writer.close();
            System.out.println("Data file written!");
            System.out.println("");
            needsToBeSaved = false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // quit the program
    // verify if they want to quit
    // prompt them to save if they have not
    private static void quit()
    {
        // ask are you sure?
        confirm = SafeInput.getYNConfirm(console, "Are you sure you want to quit? Enter [Y] for Yes, [N] for No: ");
        System.out.println("");
        if (confirm)
        {
            if (needsToBeSaved)
            {
                System.out.println("Changes have not been saved.");
                saveChoice = SafeInput.getYNConfirm(console,"Would you like to save your file? Enter [Y] for Yes, [N] for No: ");
                if (saveChoice)
                {
                    saveFile();
                }
            }
            System.exit(0);
        }
    }

}