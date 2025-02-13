package Vic.parser;

import Vic.exceptions.TaskOutOfBoundsException;
import Vic.exceptions.UnknownCommandException;
import Vic.enums.Command;
import Vic.storage.Storage;
import Vic.tasks.TaskList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDateTime;
import Vic.actions.Action;
import Vic.actions.AddAction;

import java.util.ArrayList;
import java.util.Arrays;
import Vic.tasks.Task;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParserTest {

    @Test
    public void testParseDate_validDate() {
        String dateString = "1/1/2025 1300";
        LocalDateTime expectedDate = LocalDateTime.of(2025, 1, 1, 13, 0);
        LocalDateTime parsedDate = Parser.parseDate(dateString);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void testParseDate_emptyDate() {
        LocalDateTime defaultDate = Parser.DEFAULT_DATE;
        LocalDateTime parsedDate = Parser.parseDate("");
        assertEquals(defaultDate, parsedDate);
    }

    @Test
    public void testParseCommand_validTodoCommand() throws UnknownCommandException {
        Storage mockStorage = mock(Storage.class);
        TaskList mockTaskList = mock(TaskList.class);
        String action = "todo read book";
        Command command = Command.TODO;
        Action actionObject = Parser.parseCommand(command, action, mockStorage, mockTaskList);
        assertTrue(actionObject instanceof AddAction);
    }

    // Test parsing an unknown command (should throw UnknownCommandException)
    @Test
    public void testParseCommand_unknownCommand() {
        Storage mockStorage = mock(Storage.class);
        TaskList mockTaskList = mock(TaskList.class);
        String action = "unknown command";
        Command command = Command.NONE;

        assertThrows(UnknownCommandException.class, () -> {
            Parser.parseCommand(command, action, mockStorage, mockTaskList);
        });
    }

    @Test
    public void testParseTaskId_invalidTaskId() {
        TaskList mockTaskList = mock(TaskList.class);
        when(mockTaskList.getTasks())
                .thenReturn(new ArrayList<>(Arrays.asList(new Task("t1"), new Task("t2"))));
        TaskOutOfBoundsException exception = assertThrows(TaskOutOfBoundsException.class, () -> {
            Parser.parseTaskId("6", mockTaskList);
        });
        assertEquals("The task id provided is invalid! (⚆_⚆)", exception.getMessage());
    }

    @Test
    public void testParseTaskId_validTaskId() throws TaskOutOfBoundsException {
        TaskList mockTaskList = mock(TaskList.class);
        when(mockTaskList.getTasks())
                .thenReturn(new ArrayList<>(Arrays.asList(new Task("t1"), new Task("t2"))));
        int result = Parser.parseTaskId("2", mockTaskList);
        assertEquals(1, result);
    }
}
