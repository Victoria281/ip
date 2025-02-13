package vic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import vic.actions.Action;
import vic.actions.AddAction;
import vic.enums.Command;
import vic.exceptions.TaskOutOfBoundsException;
import vic.exceptions.UnknownCommandException;
import vic.storage.Storage;
import vic.tasks.Task;
import vic.tasks.TaskList;


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
