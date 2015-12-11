/**
 *   利用LineBasedFrameDecoder 解决TCP粘包问题
 *    LineBasedFrameDecoder   依次遍历ByteBuf中的可读字节，判断是否有\n或者\r\n,如果有，就作为结束为止。
 *    StringDecoder  将接受到的对象转换成字符串。
 */
package demo.netty.stickypacket.correct.linebased;