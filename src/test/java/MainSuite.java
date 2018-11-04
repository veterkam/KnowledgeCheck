import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class MainSuite {
    private static final Logger logger = LogManager.getLogger("MainSuite");
    @Test
    public void main() {
        try {
            long i = Long.parseLong("111");
            logger.trace("success! long = " + i);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }

    }
}
