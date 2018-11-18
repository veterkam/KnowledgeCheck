import com.epam.javatraining.knowledgecheck.service.Cipher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class MainSuite {
    private static final Logger logger = LogManager.getLogger("MainSuite");
    @Test
    public void main() {
        String psw = "123";



        try {
            String code = Cipher.encode(psw);
            logger.trace(code);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}
