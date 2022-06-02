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


@WebServlet("/getConnectionRequests")
public class GetConnectionRequests extends HttpServlet{

    
    public GetConnectionRequests(){
        super();
    }

     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
        PrintWriter getConnectionRequests = response.getWriter();
        getConnectionRequests.print(concatenatedFile);
        getConnectionRequests.flush();
     }
}