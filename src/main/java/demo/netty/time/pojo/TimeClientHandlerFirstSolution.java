package demo.netty.time.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class TimeClientHandlerFirstSolution extends ChannelInboundHandlerAdapter {
	private ByteBuf buf;

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		buf = ctx.alloc().buffer(4); // (1)
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		buf.release(); // (1)
		buf = null;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf m = (ByteBuf) msg;
		buf.writeBytes(m); // (2)
		m.release();
		// The simplistic solution is to create an internal cumulative buffer
		// and wait until all 4 bytes are received into the internal buffer
		if (buf.readableBytes() >= 4) { // (3)
			long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
			System.out.println(new Date(currentTimeMillis));
			ctx.close();
		}
	}

	// @Override
	// public void channelRead(ChannelHandlerContext ctx, Object msg) {
	// ByteBuf m = (ByteBuf) msg; // (1)
	// try {
	// long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
	// System.out.println(new Date(currentTimeMillis));
	// ctx.close();
	// } finally {
	// m.release();
	// }
	// }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}