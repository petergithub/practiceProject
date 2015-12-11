/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package demo.netty.stickypacket.correct.linebased;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 一：TCP粘包拆包简介 TCP是个"流"协议，底层并不了解上次业务数据的具体含义，它是根据TCP缓冲区的实际情况对数据包进行划分，
 * 所以在数据传输过程中一个完整的数据包可能会被TCP拆分成多个包进行发送，也有可能把多个小的数据包封装成一个大的数据包发送，这就是TCP的粘包拆包问题。
 * 
 * 二：粘包拆包的解决思路 由于底层的TCP无法理解上层的业务数据，所以解决思路只能通过上层的应用协议栈设计来解决，目前主流协议的解决方案有：
 * (1)消息定长，如长度200个字节，不够的补空格； (2)在数据包结尾添加回车换行符进行分割，如FTP协议；
 * (3)将消息分为消息头和消息体，消息头中包含消息的总长度。
 * 
 * 三：Netty关于粘包拆包的解决方案
 * 对于数据量较小时并不会出现粘包问题，但当数据报文较大时，或者压力一旦上来，就会存在粘包拆包问题。下面将模拟粘包拆包案例。
 * 
 * 测试说明： 测试结果正确的收到了消息，说明LineBasedFrameDecoder和StringDecoder成功解决了TCP的粘包问题。
 * 除了上述的解码器，Netty还提供了多种支持TCP粘包拆包的解码器，用来满足不同的用户需求，在接下来的文章中将继续讲解这些解码器。
 * 
 * @author Shang Pu
 * @version Date：Dec 9, 2015 4:01:58 PM
 */
public class TimeClient {

	public void connect(int port, String host) throws Exception {
		// 配置客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new TimeClientHandler());
						}
					});

			// 发起异步连接操作
			ChannelFuture f = b.connect(host, port).sync();

			// 当代客户端链路关闭
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出，释放NIO线程组
			group.shutdownGracefully();
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
			}
		}
		new TimeClient().connect(port, "127.0.0.1");
	}
}
