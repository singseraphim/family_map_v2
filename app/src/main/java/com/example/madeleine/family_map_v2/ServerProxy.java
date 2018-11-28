package com.example.madeleine.family_map_v2;

import com.example.madeleine.family_map_v2.Model.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerProxy {
    public String serverHost;
    public String serverPort;

    public ServerProxy() {

    }
    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();

        String urlStr = "http://" + serverHost + ":" + serverPort + "/user/register";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST"); //we're adding data to the server so post method
            conn.connect();
            OutputStream reqBody = conn.getOutputStream();
            Gson gson = new Gson();

            Writer out = new OutputStreamWriter(reqBody);
            gson.toJson(request, out);
            out.close();
            reqBody.close();

            //next, check status. Check that it's HTTP_OK
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //  get response body out into response object; else return null.
                //I forgot to actually do the thing???

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }

                response = gson.fromJson(sb.toString(), RegisterResponse.class);
                return response;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
            response.message = e.toString();
            response.success = false;
            return response;
        }
    }

    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();

        String urlStr = "http://" + serverHost + ":" + serverPort + "/user/login";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST"); //we're adding data to the server so post method
            conn.connect();
            OutputStream reqBody = conn.getOutputStream();
            Gson gson = new Gson();
            Writer out = new OutputStreamWriter(reqBody);
            gson.toJson(request, out);
            out.close();
            reqBody.close();

            //next, check status. Check that it's HTTP_OK
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //  get response body out into response object; else return null.
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }

                response = gson.fromJson(sb.toString(), LoginResponse.class);
                return response;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
            response.success = false;
            response.message = e.toString();
            return response;
        }

    }

    public PeopleResponse getPeople(String authToken) {
        PeopleResponse response;

        String urlStr = "http://" + serverHost + ":" + serverPort + "/person";
        try {
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Authorization", authToken);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.connect();
            OutputStream reqBody = conn.getOutputStream();
            Gson gson = new Gson();

            //next, check status. Check that it's HTTP_OK
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                response = gson.fromJson(sb.toString(), PeopleResponse.class);
                return response;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public EventsResponse getEvents(String authToken) {
        EventsResponse response;

        String urlStr = "http://" + serverHost + ":" + serverPort + "/event";
        try {
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Authorization", authToken);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.connect();
            OutputStream reqBody = conn.getOutputStream();
            Gson gson = new Gson();

            //next, check status. Check that it's HTTP_OK
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                response = gson.fromJson(sb.toString(), EventsResponse.class);
                return response;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}

/*
CAPTAIN'S LOG:
Current issues:
Server proxy refuses connection when the app is running. It's happy during the unit tests.
I feel like I am SO DANG CLOSE MAN GOOD GRAVY




 */