import java.net.*;
import java.io.*;
import java.util.*;
import java.util.zip.CRC32;
import java.nio.*;


public class Ex2Client{

    public static void main(String[] args) throws Exception{
        
        try(Socket socket = new Socket("codebank.xyz",38102)){
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(out);
            byte[] message = new byte[100];
            int b1,b2,b;
            ByteBuffer buffer = ByteBuffer.allocate(4);


            System.out.println("Connected to Server.");
            System.out.println("Received bytes: ");
			

            for(int i = 0 ; i < 100 ; i++){
                if(i%10==0) System.out.print("\n\t");
                b1 = is.read();
                b2 = is.read();
                b = (b1*16) + b2;
                message[i] = (byte) b;
                System.out.print((Integer.toHexString(b1) + Integer.toHexString(b2)).toUpperCase());       
            }

            CRC32 crc = new CRC32();
            crc.reset();
            crc.update(message,0,100);
            long toServer = crc.getValue();
            String toConsole = Long.toHexString(toServer);
            toConsole = toConsole.toUpperCase();
            System.out.println("\n\nGenerated CRC32: "+ toConsole);

            buffer.putInt( (int) toServer);
			byte[] writeToServer = buffer.array();
			out.write(writeToServer);

            int fromServer = br.read();
			if (fromServer == 1){
				System.out.println("Response Good.");
			}else{
				System.out.println("Response Bad.");
			}
            System.out.println("Disconnected from Server.");

        } catch(Exception e){

        }



        
    }




}