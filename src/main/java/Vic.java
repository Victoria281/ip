import main.java.Task;

import java.util.Scanner;
import java.util.ArrayList;

public class Vic {
    private static final String line = "\t ____________________________________________________________\n";
    private static final String name = "Vic";
    private static final String intro = "\t Hello! I'm "
            + name
            +"\n"
            + "\t What can I do for you?\n"
            + line;
    private static final String outro = line + "\t Bye. Hope to see you again soon!\n" + line;
    private static final ArrayList<String> toDoList = new ArrayList<String>();

    /**
     * Returns reply message
     *
     * @param msg reply from bot
     * @return formatted message with lines
     */
    static String replyFormatter(String msg) {
        return line + "\t " + msg + "\n" + line;
    }

    /**
     * add item to toDoList
     *
     * @param item item to add
     */
    static void addToDoItem(String item) {
        toDoList.add(item);
        System.out.println(replyFormatter("added: " + item));
    }

    /**
     * prints the toDoList
     */
    static void printToDoList() {
        System.out.println(line);
        for (int i = 0; i < toDoList.size(); i++) {
            System.out.println("\t " + (i + 1) + ". " + toDoList.get(i));
        }
        System.out.println(line);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println(intro);

        String response = input.nextLine();

        while (!response.equals("bye")) {
            if (response.equals("list")) {
                printToDoList();
            } else {
                addToDoItem(response);
            }
            response = input.nextLine();
        }
        System.out.println(outro);


    }
}
