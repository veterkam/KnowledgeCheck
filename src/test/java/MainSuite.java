import com.epam.javatraining.knowledgecheck.model.entity.Student;
import com.epam.javatraining.knowledgecheck.model.entity.User;
import com.epam.javatraining.knowledgecheck.service.Cipher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class MainSuite {
    private static final Logger logger = LogManager.getLogger("MainSuite");
    @Test
    public void main() {
        User a = new User();
        a.setId(1);
        a.setFirstname("Andrey");
        a.setLastname("Pskov");
        a.setUsername("tagil");
        a.setPassword("psw");
        a.setRole(User.Role.STUDENT);
        a.setEmail("email@box.com");
        a.setVerified(true);

        logger.trace("a = " + a);

        Student b = new Student(a);
        b.setFirstname("Vanya");
        b.setId(2);
        b.setSpecialty("specialty");

        logger.trace("b = " + b + ", spec = " + b.getSpecialty());
        logger.trace("a = " + a);

    }
}
