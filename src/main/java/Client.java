import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    final static int PORT = 20000;

    public static void main(String[] args) throws IOException {
        startClient();
    }

    static void startClient() throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
        final SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(inetSocketAddress);
        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String msg;
            while (true) {
                System.out.println("Enter message for server...");
                msg = scanner.nextLine();
                if ("end".equals(msg)) break;

                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(1000);

                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            socketChannel.close();
        }

    }
}
