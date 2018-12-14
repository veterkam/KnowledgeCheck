package edu.javatraining.knowledgecheck;

import edu.javatraining.knowledgecheck.controller.dto.*;
import edu.javatraining.knowledgecheck.tools.LocaleMsgReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;


public class DtoSuite {
    private static final Logger logger = LogManager.getLogger("Test");
    @Test
    public void main() {
    }

    @Test
    public void userDtoValidationTest() {

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
    public void tutorDtoValidationTest() {

        TutorDto tutorDto = new TutorDto();
        tutorDto.setFirstName("Ft");
        tutorDto.setLastName("LastName");
        tutorDto.setPassword("123");
        tutorDto.setConfirmPassword("1423");
        tutorDto.setEmail("mailcom");
        tutorDto.setPosition("  ");

        Map<String, List<String>> errors = DtoValidator.validate(tutorDto);

        Assert.assertEquals( errors.size(), 8 );
    }

    @Test
    public void studentDtoValidationTest() {

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
    public void testDtoValidationTest() {

        TestDto testDto = new TestDto();
        testDto.setDescription("de");
        testDto.setTitle("Title");
        testDto.setSubjectId("1");
        testDto.setTestId("12");

        Map<String, List<String>> errors = DtoValidator.validate(testDto);

        Assert.assertEquals( errors.size(), 1);
    }

    @Test
    public void testDtoTimeLimitationValidationTest() {

        TestDto testDto = new TestDto();
        testDto.setDescription("Description of the test");
        testDto.setTitle("Title");
        testDto.setSubjectId("1");
        testDto.setTestId("12");
        testDto.setTimeLimitation("99:10:71");
        Map<String, List<String>> errors = DtoValidator.validate(testDto);

        Assert.assertEquals( errors.size(), 1);
    }



    @Test
    public void answerDtoValidationTest() {

        javax.persistence.spi.PersistenceProviderResolverHolder.setPersistenceProviderResolver(null);
        AnswerDto answer = new AnswerDto();
        answer.setDescription("");
        answer.setAnswerId("12");

        Map<String, List<String>> errors = DtoValidator.validate(answer);

        Assert.assertEquals(errors.size(), 1 );
    }

    @Test
    public void localeMsgReaderValidationTest() {

        String message = LocaleMsgReader.message("ru", "app.testing.your_result_is", 30);
        String expect = "Ваш результат 30% правильных ответов!";

        Assert.assertEquals( message, expect );
    }



}
