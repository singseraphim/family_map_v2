package Server.Services.Register;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Server.Model.User;
import Server.Services.Clear.Clear;

import static org.junit.Assert.*;

public class RegisterTest {
    User user = new User();
    RegisterRequest request = new RegisterRequest();
    Register registerService = new Register();
    Clear clearService = new Clear();

    @Before
    public void setUp() throws Exception {
        clearService.clear();
        user.userName = "b";
        user.password = "c";
        user.lastName = "d";
        user.firstName = "e";
        user.email = "f";
        user.gender = "f";
        user.personID = "h";

        request.userName = "b";
        request.password = "c";
        request.lastName = "d";
        request.firstName = "e";
        request.email = "f";
        request.gender = "f";
    }

    @After
    public void tearDown() throws Exception {
    }

    //note: This fails because my unit tests have a problem when I call the fill method in register.
    // when I run my unit tests, my fill method can't find the json files with
    // the random data. I talked to Kara and we couldn't figure out how to fix it. It works perfectly
    // when I run it in main, and I passed it off fine.
    @Test
    public void register() {
        RegisterResponse response = registerService.register(request);
        assertTrue(response.success);
        assertNotNull(response.authToken);
        assertNotNull(response.personID);
    }

    @Test
    public void registerEmptyData() {
        request.userName = "";
        RegisterResponse response = registerService.register(request);
        assertFalse(response.success);
        assertTrue(response.message == "Username blank");
    }
    @Test
    public void registerInvalidGender() {

        request.gender = "z";
        RegisterResponse response = registerService.register(request);
        assertFalse(response.success);
        assertTrue(response.message == "Invalid gender");

    }



}