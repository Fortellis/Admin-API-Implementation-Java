import java.io.IOException;
import java.io.PrintWriter;
  
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import java.io.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import com.google.gson.Gson;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.nio.charset.StandardCharsets;

import org.json.*;

import com.okta.jwt.*;
import java.time.Duration;

import java.util.*;

@WebServlet("/deactivate/*")
public class Deactivate extends HttpServlet{

    public Deactivate(){
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //Get authorization header.
        String authorizationHeader = request.getHeader("Authorization");
        //System.out.println("This is the authorization header: " + authorizationHeader.replace("Bearer", ""));

        try{
            AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer("https://identity-dev.fortellis.io/oauth2/aus1ni5i9n9WkzcYa2p7")
                .setAudience("api_providers")                
                .setConnectionTimeout(Duration.ofSeconds(1)) 
                .build();
                 
            Jwt jwt = jwtVerifier.decode(authorizationHeader.replace("Bearer", ""));
            System.out.println("This is the authentication decoded: " + jwt);

            //This is your deactivation request from the path parameter.  
            String UUID = request.getPathInfo().toString();
            System.out.println("This is the UUID: " + UUID);

            String deactivateRequest = "{\"connectionId\": \"" + UUID + "\"}";
            System.out.println("This is your deactivation request: " + deactivateRequest);
            newDeactivateRequest(deactivateRequest);
            PrintWriter deactivationResponse = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            deactivationResponse.print("{\"links\": [{\"href\": \"localhost:3000\", \"rel\": \"self\", \"method\": \"post\", \"title\": \"Deactivation Request\"}]}");
            deactivationResponse.flush();
        }catch(Exception e){
            System.out.println("You had a problem with the token.");
        }

    }
    public void newDeactivateRequest(String deactivateRequest) {

        try{
            ClassLoader classLoader = getClass().getClassLoader();
            File  wholeFile =new File (classLoader.getResource("deactivationRequests.json").getFile());
            InputStream inputStream = new FileInputStream(wholeFile);
            FileInputStream fis = new FileInputStream(wholeFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String concatenatedFile = "";
            String strLine;
            //Put all the lines from the file back together to make the original object.
            while((strLine = br.readLine()) != null){
                //System.out.println(strLine);
                concatenatedFile = concatenatedFile + strLine;
            }
            System.out.println("This is the concatenatedFile: " + concatenatedFile);
            //Make the concatenated lines a JSON object again. 
            JSONObject objectForConcatenatedFile = new JSONObject(concatenatedFile);
            //Change the connectionRequests from the file into just an array. 
            JSONArray parsedDeactivationRequests = objectForConcatenatedFile.getJSONArray("deactivationRequests");
            System.out.println("This is just the array of the deactivationRequests: "+ parsedDeactivationRequests);
            //Change the connectionRequest into an object
            JSONObject addObject = new JSONObject(deactivateRequest);
            //Put the connection request object into the array.
            parsedDeactivationRequests.put(addObject);
            System.out.println("This is the object for the concatenated file: "+ objectForConcatenatedFile.toString(4));

            Path path = Paths.get(wholeFile.toString());
            String str = objectForConcatenatedFile.toString(4);
            byte[] arr = str.getBytes();
            try{
                Files.write( path, arr);
            }catch(IOException ex){
                System.out.print("Invalid Path");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}   

