package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class TheHTTPServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started!");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Client details:\n";
            response += "RequestMethod: " + exchange.getRequestMethod() + "\n";
            response += "RequestURI: " + exchange.getRequestURI() + "\n";
            response += "Protocol: " + exchange.getProtocol() + "\n";
            response += "RemoteAddress: " + exchange.getRemoteAddress().getHostName() + "\n";
            response += "RequestURI: " + exchange.getRequestURI() + "\n";
            System.out.println(response);
            
            if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStream inStream = exchange.getRequestBody();
				OutputStream fileOutStream = new FileOutputStream ("D:/wur" + (new Random()).nextInt() + "_serever.jpg"); 

				byte[] buffer = new byte[1024];
                int len = inStream.read(buffer);
                while (len != -1) {
                	fileOutStream.write(buffer, 0, len);
                    len = inStream.read(buffer);
                }
                
                fileOutStream.flush();
                fileOutStream.close();
            }

            File file = new File("D:/wur.jpg");
            exchange.sendResponseHeaders(200, file.length());
            OutputStream responseStream = exchange.getResponseBody();

			InputStream fileInStream = new FileInputStream (file); 
			{
				byte[] buffer = new byte[1024];
	            int len = fileInStream.read(buffer);
	            while (len != -1) {
	            	responseStream.write(buffer, 0, len);
	                len = fileInStream.read(buffer);
	            }
			}
			fileInStream.close();
			
			responseStream.flush();
			responseStream.close();
        }
    }
}