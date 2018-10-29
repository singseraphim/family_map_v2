
import Server.Services.Fill.Fill;
import Server.Services.Fill.FillResponse;

public class Main {
    public static void main(String[] args) {
        Fill fillService = new Fill();
        FillResponse response = fillService.fill("TestDescendant", 1);
        System.out.println(response.message);
    }
}



/*
CAPTAIN'S LOG:
Got started on handlers, using Rodham notes as guide. Not sure how to send my response back via json, or how to test this.

UNSOLVED ISSUES:
How to send response back via json
How to implement the UI and get the server connections going
How to test what's happening in the handlers
Do I need separate handlers for event vs events
Unit test can't find my JSON files


Unit test statuses:
Services:
Person- complete
Clear- complete
Load- complete
Register- complete
Event- complete
Fill- tested in main perfectly, unit test can't find JSON files.
Login- complete

DAOs:
Auth: complete
Event: complete
Person: complete
User: complete



MONDAY
Handlers
Math homework
252 homework

TUESDAY
Work
Handlers

WEDNESDAY
Work
Handlers
Math homework
252 homework




Things to do when tidying code:
Make the DAO's private data members instead of declaring them everywhere.
Remove assert statements.


 */


/*
        JSONParser
        JSONObject
        JSONArray

        parse into object, parse object into array.


           unit test coverage:
           need to test for things go wrong- one case of things working, two or three tests of things breaking.


         */

