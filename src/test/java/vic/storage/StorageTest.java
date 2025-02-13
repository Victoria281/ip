package vic.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import vic.tasks.Task;
import vic.tasks.TaskList;

public class StorageTest {

    @Test
    public void testCheckFileExists_createsFileWhenNotExists(@TempDir File tempDir) {
        String folderPath = tempDir.getAbsolutePath();
        String fileName = "/vic.txt";
        File file = new File(folderPath, fileName);
        assertFalse(file.exists());

        Storage storage = new Storage(fileName, folderPath);
        boolean result = storage.checkFileExists();
        assertTrue(result);
        assertTrue(file.exists());
    }

    @Test
    public void testCheckFileExists_whenFileAlreadyExists(@TempDir File tempDir) throws Exception {
        String folderPath = tempDir.getAbsolutePath();
        String fileName = "vic.txt";

        File file = new File(folderPath, fileName);
        file.getParentFile().mkdirs();
        boolean created = file.createNewFile();
        assertTrue(created || file.exists());

        Storage storage = new Storage(fileName, folderPath);

        boolean result = storage.checkFileExists();
        assertTrue(result);
    }

    @Test
    public void testLoadTasksFromFile_validData_shouldLoadCorrectTasks(@TempDir Path tempDir) throws IOException {
        String folderPath = tempDir.toString();
        String fileName = "vic.txt";
        Path filePath = tempDir.resolve(fileName);
        Files.write(filePath, "T | 0 | Read book\n".getBytes());

        TaskList tasks = new TaskList();
        Storage storage = new Storage("/" + fileName, folderPath);
        tasks = storage.loadTasksFromFile(tasks);

        assertNotNull(tasks, "TaskList should not be null");
        List<Task> taskList = tasks.getTasks();
        assertEquals(1, taskList.size(), "There should be exactly one task loaded");
        assertEquals("Read book", taskList.get(0).getDescription(), "The task description should match");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "T | |",
        "T",
        "K | |",
        "D",
        "E",
        "T | description | hi",
        "D | 0 | return book | ",
        "D | 0 | return book | 2025/01/01",
        "D | 2 | return book | 1/1/2025 2359",
        "D | 0 | return book | 1/1/2025 2359 | extra field",
        "E | 0 | project meeting | 1/1/2025 2359 | ",
        "E | 0 | project meeting | 2025/01/01 | 2025/01/02",
        "E | 2 | project meeting | 1/1/2025 2359 | 2/1/2025 2359",
        "E | 0 | project meeting | 1/1/2025 2359 | 2/1/2025 2359 | extra field",
        "N | 0 | something unknown",
        "N | 0 | ",
        "N | 2 | something",
        "N | 0 | something | extra field"
    })
    public void testLoadTasksFromFile_invalidData_shouldFixOrSkip(String invalidLine,
                                                                  @TempDir Path tempDir) throws IOException {
        String folderPath = tempDir.toString();
        String fileName = "vic.txt";

        Path filePath = tempDir.resolve(fileName);
        Files.write(filePath, invalidLine.getBytes());

        TaskList tasks = new TaskList();
        Storage storage = new Storage("/" + fileName, folderPath);
        tasks = storage.loadTasksFromFile(tasks);

        assertNotNull(tasks, "TaskList should not be null");

        List<Task> taskList = tasks.getTasks();
        if (invalidLine.startsWith("T | |")
                || invalidLine.startsWith("K | |")
                || invalidLine.startsWith("N")) {
            assertEquals(0, taskList.size(), "Task should be deleted: " + invalidLine);
        } else {
            assertEquals(1, taskList.size(), "Task should be fixed or valid: " + invalidLine);
        }

    }
}
