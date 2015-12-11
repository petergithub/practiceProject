package demo.netty.stickypacket.fixedLengthFrameDecoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

/**
 * Created by Idea 14 whih "netty"
 *
 * @Auhor: karl.zhao
 * @Email: karl.zhao@qq.com
 * @Date: 2015-11-28
 * @Time: 16:58
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter{

    // 写日志
    private static final Logger logger =
            Logger.getLogger(TimeClientHandler.class.getName());

    private int counter;
    private final String echo_req = "hi,!@#$@#@SADAS,!_#_";

    public TimeClientHandler(){}

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg)
            throws Exception{
        String body = (String)msg;
        System.out.println("Now is : " + body+";the countor is : "+ ++counter);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        // 当客户端和服务端建立tcp成功之后，Netty的NIO线程会调用channelActive
        // 发送查询时间的指令给服务端。
        // 调用ChannelHandlerContext的writeAndFlush方法，将请求消息发送给服务端
        // 当服务端应答时，channelRead方法被调用

        for (int i=0;i<100;i++){
            ctx.writeAndFlush(Unpooled.copiedBuffer(echo_req.getBytes()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        logger.warning("message from:"+cause.getMessage());
        ctx.close();
    }
}
