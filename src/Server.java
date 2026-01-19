import javax.imageio.IIOException;
import java.io.IOException;
import java.net.ServerSocket;

public class Server{
    private ServerSocket serverSocket; // Used as a Listening device for receiving messages.

    public Server(ServerSocket serverSocket) { // Constructor to hold the server, constructors use "this" keyword
        this.serverSocket = serverSocket; // alt + insert brings up a quick menu to easily build constructors or methods
    }

    public void startServer(){ // Method responsible for keeping the server up.

        try{
            while(!serverSocket.isClosed()); { // While the server socket is not closed it won't shut down

                serverSocket.accept();// The socket stops here until the server is connected
                System.out.printf("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket); // Object

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch(IIOException e) {

        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Prints a list of exactly what the computer was doing at the time of the crash

        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}