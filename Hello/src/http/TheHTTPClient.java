package http;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class TheHTTPClient {
	public static void main(String[] args) {
		try {

			byte[] bites = Files.readAllBytes(Paths.get("d:\\wur.jpg"));
	
			HttpURLConnection httpUrlConnection = null;
			URL url = new URL("http://localhost:8000/bla");
			httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setDoOutput(true);
	
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
			httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data");
	
			DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());
			request.write(bites);
			request.flush();
			request.close();
	
			InputStream responseStream = httpUrlConnection.getInputStream();
			
			OutputStream fileStream = new FileOutputStream ("D:/wur" + (new Random()).nextInt() + "_client.jpg"); 

			byte[] buffer = new byte[1024];
            int len = responseStream.read(buffer);
            while (len != -1) {
            	fileStream.write(buffer, 0, len);
                len = responseStream.read(buffer);
            }
            
            fileStream.flush();
            fileStream.close();
	
			responseStream.close();
	
			httpUrlConnection.disconnect();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Bye!");
	}
}
