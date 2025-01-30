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

    /**
     * Returns reply message
     *
     * @param msg reply from bot
     * @return formatted message with lines
     */
    static String replyFormatter(String msg) {
        return line + "\t " + msg + "\n" + line;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println(intro);

        String response = input.nextLine();

        while (!response.equals("bye")) {
            System.out.println(replyFormatter(response));
            response = input.nextLine();
        }
        System.out.println(outro);


    }
}
