package demo.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		// Discard the received data silently.
		// ((ByteBuf) msg).release(); // (3)
		ByteBuf in = (ByteBuf) msg;
		try {

			System.out.println("Do something withmsg["
					+ in.toString(io.netty.util.CharsetUtil.US_ASCII) + "]");
			// while (in.isReadable()) {
			// System.out.print((char) in.readByte());
			// System.out.flush();
			// }
		} finally {
			ReferenceCountUtil.release(msg); // (2)
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}