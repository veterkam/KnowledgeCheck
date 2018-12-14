package edu.javatraining.knowledgecheck;

import edu.javatraining.knowledgecheck.controller.AccountControllerServlet;
import org.testng.annotations.Test;

public class AccountControllerSuite {

    @Test
    public void testShouldShowRegisterForm() throws Exception {

        testGetMethod("/account/register", "account/register");
    }

    private void testGetMethod(String urlTemplate, String expectedViewName) throws Exception {
        AccountControllerServlet controller = new AccountControllerServlet();

    }
}
