import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // Allows to send a bunch of messages to different people instead of just one
    private Socket socket;
    private BufferedWriter bufferedWriter; // Send messages to clients.
    private BufferedReader bufferedReader; // Read messages sent from client
    private String clientUsername;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket; // Socket is the connection between the server, clienthandler, and client.
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("Server" + clientUsername + "has entered the chat");
        } catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }


    @Override // Needed when implementing Runnable
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    public void broadcastMessage(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("Server" + clientUsername + "has left the chat!");
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
