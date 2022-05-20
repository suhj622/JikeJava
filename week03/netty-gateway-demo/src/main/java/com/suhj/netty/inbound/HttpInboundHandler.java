package com.suhj.netty.inbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.ReferenceCountUtil;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final String proxyServer;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception  {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws Exception {
        //拼接访问后端服务的url
        final String url = proxyServer + fullRequest.uri();
        //System.out.println("执行代理请求:"+ url);

        //处理请求
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet(url);

            // Create a custom response handler
            final HttpClientResponseHandler<byte[]> responseHandler = new HttpClientResponseHandler<byte[]>() {

                @Override
                public byte[] handleResponse(
                        final ClassicHttpResponse response) throws IOException {
                    //System.out.println("response:" + response);
                    final int status = response.getCode();
                    if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                        final HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toByteArray(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };

            byte[] content = httpclient.execute(httpget, responseHandler);
            FullHttpResponse response  = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", content.length);
            ctx.write(response);
            ctx.flush();
         }
    }

}
