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

    @Test
    public void register() {
        RegisterResponse response = registerService.register(request);
        assertTrue(response.success);
        assertNotNull(response.authToken);
        assertNotNull(response.personID);

        response = registerService.register(request);
        assertFalse(response.success);

    }

    @Test
    public void registerNotUnique() {
        registerService.register(request);
        RegisterResponse response = registerService.register(request);
        assertFalse(response.success);
        assertTrue(response.message == "Username taken");
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