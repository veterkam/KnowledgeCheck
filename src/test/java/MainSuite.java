import edu.javatraining.knowledgecheck.controller.dto.DtoValidator;
import edu.javatraining.knowledgecheck.controller.dto.StudentDto;
import edu.javatraining.knowledgecheck.controller.dto.TutorDto;
import edu.javatraining.knowledgecheck.controller.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.ScriptAssert;
import org.testng.Assert;
import org.testng.annotations.Test;

import org.hibernate.validator.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainSuite {
    private static final Logger logger = LogManager.getLogger("MainSuite");
    @Test
    public void main() {

        Validator validator;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Car car = new Car( "hello", "hello", "DD-AB-123", 4 );

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate( car );


        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "may not be null",
                constraintViolations.iterator().next().getMessage()
        );
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

        Assert.assertEquals( 1, errors.size() );
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

        Assert.assertEquals( 1, errors.size() );
    }

    @Test
    public void studentDtoTest() {

        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("Ft");
        studentDto.setLastName("LastName");
        studentDto.setPassword("123");
        studentDto.setConfirmPassword("1423");
        studentDto.setEmail("mailcom");
        //tutorDto.setRole("Tutor");
        studentDto.setYear(" dsfdsf ");

        Map<String, List<String>> errors = DtoValidator.validate(studentDto);

        Assert.assertEquals( 1, errors.size() );
    }


    @Test
    public void genCSFK() {
        logger.trace("" + Math.random());
        logger.trace("" + Math.random());
        logger.trace("" + Math.random());
        logger.trace("" + Math.random());
        logger.trace("" + Math.random());
    }


}
