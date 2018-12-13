import edu.javatraining.knowledgecheck.controller.dto.*;
import edu.javatraining.knowledgecheck.tools.LocaleMsgReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;


public class MainSuite {
    private static final Logger logger = LogManager.getLogger("MainSuite");
    @Test
    public void main() {
    }

    @Test
    public void userDtoTest() {

        UserDto userDto = new UserDto();
        userDto.setFirstName("First");
        userDto.setLastName("LastName");
        userDto.setPassword("123");
        userDto.setConfirmPassword("1423");
        userDto.setEmail("mailcom");
        //userDto.setRole("Tutor");

        Map<String, List<String>> errors = DtoValidator.validate(userDto);

        Assert.assertEquals( errors.size(), 4 );
    }

    @Test
    public void tutorDtoTest() {

        TutorDto tutorDto = new TutorDto();
        tutorDto.setFirstName("Ft");
        tutorDto.setLastName("LastName");
        tutorDto.setPassword("123");
        tutorDto.setConfirmPassword("1423");
        tutorDto.setEmail("mailcom");
        //tutorDto.setRole("Tutor");
        tutorDto.setPosition("  ");

        Map<String, List<String>> errors = DtoValidator.validate(tutorDto);

        Assert.assertEquals( errors.size(), 8 );
    }

    @Test
    public void studentDtoTest() {

        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("");
        studentDto.setLastName("LastName");
        studentDto.setPassword("123");
        studentDto.setConfirmPassword("1423");
        studentDto.setEmail("mailcom");
        studentDto.setYear(" dsfdsf ");

        Map<String, List<String>> errors = DtoValidator.validate(studentDto);

        Assert.assertEquals( errors.size(), 8 );
    }

    @Test
    public void testDtoTest() {

        TestDto testDto = new TestDto();
        testDto.setDescription("de");
        testDto.setTitle("Title");
        testDto.setSubjectId("1");
        testDto.setTestId("12");

        Map<String, List<String>> errors = DtoValidator.validate(testDto);

        Assert.assertEquals( errors.size(), 1);
    }

    @Test
    public void answerDtoTest() {

        AnswerDto answer = new AnswerDto();
        answer.setDescription("");
        answer.setAnswerId("12");

        Map<String, List<String>> errors = DtoValidator.validate(answer);

        Assert.assertEquals(errors.size(), 1 );
    }

    @Test
    public void localeMsgReaderTest() {

        String message = LocaleMsgReader.message("ru", "app.testing.your_result_is", 30);
        String expect = "Ваш результат 30% правильных ответов!";

        Assert.assertEquals( message, expect );
    }



}
