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

import org.json.*;

@WebServlet("/deleteRequest")
public class DeleteRequest extends HttpServlet{
    public DeleteRequest(){
        super();
    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append(System.lineSeparator());
        }
        JSONObject requestToDelete = new JSONObject(buffer.toString());
        System.out.println("This is the JSON Object for the data: " + requestToDelete);
        
        File  wholeFile =new File ("src\\main\\resources\\connectionRequests.json");
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

        //Make the concatenated file into an object.
        JSONObject objectForConcatenatedFile = new JSONObject(concatenatedFile);
        //Change the connectionRequests from the file into just an array. 
        JSONArray parsedConnectionRequests = objectForConcatenatedFile.getJSONArray("connectionRequests");
        System.out.println("This is the array: " + parsedConnectionRequests);
        /*
        int index = parsedConnectionRequests.indexOf(requestToDelete);
        System.out.println("This is the index in the array: " + index);
        */
        System.out.println("This is the connection request from the data in the request: " + requestToDelete.getString("connectionId") );
        int index = 0;
        for( int i = 0; i < parsedConnectionRequests.length(); i++){
            String savedConnectionRequest = parsedConnectionRequests.getJSONObject(i).getString("connectionId");
            String requestToDeleteConnectionId = requestToDelete.getString("connectionId");
            System.out.println("A save connection request " + savedConnectionRequest);
            if(savedConnectionRequest.equals(requestToDeleteConnectionId)){
                index = i;
            }
        }
        System.out.println("This is the index for the connection to delete: " + index);
        parsedConnectionRequests.remove(index);
        System.out.println("This is the new object that we are sending to the file: " + objectForConcatenatedFile);

        Path path = Paths.get(wholeFile.toString());
        String str = objectForConcatenatedFile.toString(4);
        byte[] arr = str.getBytes();
        try{
            Files.write( path, arr);
        }catch(IOException ex){
            System.out.print("Invalid Path");
        }
    }
}

