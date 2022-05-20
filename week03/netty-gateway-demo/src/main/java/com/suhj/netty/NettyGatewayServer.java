package com.suhj.netty;

import com.suhj.netty.inbound.HttpInboundServer;

import java.util.Arrays;

/**
 * Netty 网关 1.0
 */
public class NettyGatewayServer {

    public final static String GATEWAY_NAME = "NettyGateway";
    public final static String GATEWAY_VERSION = "1.0.0";

    public static void main(String[] args) {

        //网关服务端口
        String proxyPort = System.getProperty("proxyPort","8000");
        //业务服务地址
        String proxyServer = System.getProperty("proxyServer","http://localhost:8088");
        int port = Integer.parseInt(proxyPort);

        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" starting...");
        //创建服务网关
        HttpInboundServer server = new HttpInboundServer(port, proxyServer);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" started at http://localhost:" + port + " for server:" + server.toString());
        try {
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
