package com.example.madeleine.family_map_v2;

import android.provider.Contacts;

import com.example.madeleine.family_map_v2.Model.EventsResponse;
import com.example.madeleine.family_map_v2.Model.LoginRequest;
import com.example.madeleine.family_map_v2.Model.LoginResponse;
import com.example.madeleine.family_map_v2.Model.PeopleResponse;
import com.example.madeleine.family_map_v2.Model.RegisterRequest;
import com.example.madeleine.family_map_v2.Model.RegisterResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerProxyTest {
    RegisterRequest regRequest = new RegisterRequest();
    LoginRequest logRequest = new LoginRequest();
    ServerProxy proxy = new ServerProxy();

    @Before
    public void setUp() throws Exception {
        regRequest.email = "an email";
        regRequest.firstName = "coolBeans";
        regRequest.lastName = "mcFly";
        regRequest.gender = "f";
        regRequest.password = "doot";
        regRequest.userName = "theSuperDarkOne";

        logRequest.password = "doot";
        logRequest.userName = "theSuperDarkOne";

        proxy.serverHost = "localhost";
        proxy.serverPort = "8083";
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void register() {
        RegisterResponse response = proxy.register(regRequest);
        assertTrue(response.success);
    }

    @Test
    public void registerInvalid() {
        regRequest.gender = "z";
        RegisterResponse response = proxy.register(regRequest);
        assertFalse(response.success);
    }

    @Test
    public void login() {
        LoginResponse response = proxy.login(logRequest);
        assertTrue(response.success);
    }

    @Test
    public void loginInvalid() {
        logRequest.password = "bloop";
        LoginResponse response = proxy.login(logRequest);
        assertFalse(response.success);
    }

    @Test
    public void getPeople() {
        PeopleResponse response = proxy.getPeople("aa3ceef3-31dc-4c84-9309-ad67fbbf6523");
        assertTrue(response.success);
        assertTrue(response.data.size() > 1);
    }

    @Test
    public void getEvents() {
        EventsResponse response = proxy.getEvents("aa3ceef3-31dc-4c84-9309-ad67fbbf6523");
        assertTrue(response.success);
        assertTrue(response.data.size() > 1);
    }
}