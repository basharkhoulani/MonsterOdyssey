package de.uniks.stpmon.team_m.udp;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

    public class UdpClient {
        private final InetAddress serverAddress;
        private final int serverPort;
        private final List<Consumer<String>> messageHandlers = Collections.synchronizedList(new ArrayList<>());
        private DatagramSocket socket;
        private Thread receivingThread;

        public UdpClient(InetAddress serverAddress, int serverPort) {
            this.serverAddress = serverAddress;
            this.serverPort = serverPort;
        }

        public boolean isOpen() {
            return this.socket != null && !this.socket.isClosed();
        }

        public void open() throws IOException {
            onOpen(new DatagramSocket());
        }

        public void close() {
            if (socket != null && !socket.isClosed()) {
                onClose();
            }
        }

        @OnOpen
        public void onOpen(DatagramSocket socket) {
            this.socket = socket;
        }

        @OnClose
        public void onClose() {
            socket.close();
            this.socket = null;
            receivingThread.interrupt();
        }

        @OnMessage
        public void onMessage(String message) {
            for (final Consumer<String> handler : this.messageHandlers) {
                handler.accept(message);
            }
        }

        @OnError
        public void onError(Throwable error) {
            error.printStackTrace();
        }

        public void addMessageHandler(Consumer<String> msgHandler) {
            this.messageHandlers.add(msgHandler);
        }

        public void removeMessageHandler(Consumer<String> msgHandler) {
            this.messageHandlers.remove(msgHandler);
        }

        public void sendMessage(String message) throws IOException {
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            socket.send(sendPacket);
        }

        protected void startReceiving() {
            receivingThread = new Thread(() -> {
                while (isOpen()) {
                    try {
                        byte[] receiveData = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socket.receive(receivePacket);
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        onMessage(message);
                    } catch (IOException e) {
                        onError(e);
                    }
                }
            });
            receivingThread.start();
        }

        public boolean hasMessageHandler() {
            return !this.messageHandlers.isEmpty();
        }
    }

