package org.example;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.impl.JidCreate;


public class Main {

    public static void main(String[] args) {
        conect();
    }

    public static void conect() {
        final boolean[] luzAcesa = {true};
        new Thread(() -> {
            try {

                XMPPTCPConnectionConfiguration.Builder builder = //
                        XMPPTCPConnectionConfiguration.builder() //
                                .setHost("127.0.0.1") //
                                .setPort(5222) //
                                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) //
                                .setSendPresence(true) //
                                .setXmppDomain(JidCreate.domainBareFrom("10.1.11.122"));

                XMPPTCPConnection conn = new XMPPTCPConnection(builder.build());
                conn.connect();

                conn.login("user", "user");

                ChatManager chatManager = ChatManager.getInstanceFor(conn);
                chatManager.addChatListener((chat, b) -> {
                    chat.addMessageListener((chat1, message) -> {
                        System.out.println("Comando recebido - " + message.getBody());
                        try {
                            if (message.getBody().equalsIgnoreCase("temperatura")) {
                                int v = (int) (Math.random() * (50 - -10));
                                chat1.sendMessage(String.valueOf(v));
                                System.out.println("Enviado - " + v);
                            } else if (message.getBody().equalsIgnoreCase("luz")) {
                                luzAcesa[0] = !luzAcesa[0];
                                chat.sendMessage(String.valueOf(luzAcesa[0]));
                                System.out.println("Enviado - " + luzAcesa[0]);
                            }
                        } catch (SmackException.NotConnectedException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });

                System.out.println("Conectado");
                while (true) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
