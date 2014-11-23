package demo.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Algorithm {
	/**
	 * MD5 即Message-Digest Algorithm 5(信息-摘要算法5)，用于确保信息传输完整一致。
	 * 是计算机广泛使用的杂凑算法之一(又译摘要算法、哈希算法)。
	 * MD5的作用是让大容量信息在用数字签名软件签署私人密钥前被"压缩"成一种保密的格式(就是把一个任意长度的字节串变换成一定长的十六进制数字串)。
	 */
	public static final String MD5 = "MD5";

	/**
	 * SHA 是一种数据加密算法，该算法经过加密专家多年来的发展和改进已日益完善，现在已成为公认的最安全的散列算法之一，并被广泛使用。
	 * 该算法的思想是接收一段明文，然后以一种不可逆的方式将它转换成一段(通常更小)密文，也可以简单的理解为取一串输入码(称为预映射或信息)，
	 * 并把它们转化为长度较短、位数固定的输出序列即散列值(也称为信息摘要或信息认证代码)的过程。
	 * 散列函数值可以说时对明文的一种"指纹"或是"摘要"所以对散列值的数字签名就可以视为对此明文的数字签名。
	 */
	public static final String SHA = "SHA";

	/**
	 * DES算法为密码体制中的对称密码体制，又被成为美国数据加密标准，是1972年美国IBM公司研制的对称密码体制加密算法。 明文按64位进行分组,
	 * 密钥长64位，密钥事实上是56位参与DES运算(第8、16、24、32、40、48、56、64位是校验位，
	 * 使得每个密钥都有奇数个1)分组后的明文组和56位的密钥按位替代或交换的方法形成密文组的加密方法。
	 */
	public static final String DES = "DES";

	/**
	 * 3DES又称Triple DES，是DES加密算法的一种模式，它使用3条56位的密钥对3DES
	 * 数据进行三次加密。数据加密标准(DES)是美国的一种由来已久的加密标准，它使用对称密钥加密法，并于1981年被ANSI组织规范为ANSI
	 * X.3.92
	 * 。DES使用56位密钥和密码块的方法，而在密码块的方法中，文本被分成64位大小的文本块然后再进行加密。比起最初的DES，3DES更为安全。 　　
	 * 3DES(即Triple
	 * DES)是DES向AES过渡的加密算法(1999年，NIST将3-DES指定为过渡的加密标准)，是DES的一个更安全的变形。它以DES为基本模块，
	 * 通过组合分组方法设计出分组加密算法，其具体实现如下: 
	 * 设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的密钥，P代表明文，C代表密文， 这样， 　　
	 * 3DES加密过程为: C=Ek3(Dk2(Ek1(P))) 3DES解密过程为: P=Dk1((EK2(Dk3(C)))
	 */
	public static final String DESede = "DESede";

	/**
	 * AES密码学中的高级加密标准(Advanced Encryption Standard，AES)，又称
	 * 高级加密标准Rijndael加密法，是美国联邦政府采用的一种区块加密标准。
	 * 这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。经过五年的甄选流程
	 * ，高级加密标准由美国国家标准与技术研究院(NIST)于2001年11月26日发布于FIPS PUB 197，并在2002年5月26日成为有效的标准
	 */
	public static final String AES = "AES";

	/**
	 * 该算法于1977年由美国麻省理工学院MIT(Massachusetts Institute of Technology)的Ronal
	 * Rivest，Adi Shamir和Len
	 * Adleman三位年轻教授提出，并以三人的姓氏Rivest，Shamir和Adlernan命名为RSA算法，
	 * 是一个支持变长密钥的公共密钥算法，需要加密的文件快的长度也是可变的! 所谓RSA加密算法，是世界上第一个非对称加密算法，也是数论的第一个实际应用。
	 * RSA是目前最有影响力的公钥加密算法
	 * ，它能够抵抗到目前为止已知的所有密码攻击，已被ISO推荐为公钥数据加密标准。RSA算法基于一个十分简单的数论事实
	 * : 将两个大素数相乘十分容易，但那时想要对其乘积进行因式分解却极其困难，因此可以将乘积公开作为加密密钥。
	 */
	public static final String RSA = "RSA";
	/**
	 * Encrypts a file using the Caesar cipher.
	 */
	public void caesar(String inFile, String outFile, String keyStr) {
		try {
			FileInputStream in = new FileInputStream(inFile);
			FileOutputStream out = new FileOutputStream(outFile);
			int key = Integer.parseInt(keyStr);
			int ch;
			while ((ch = in.read()) != -1) {
				byte c = (byte) (ch + key);
				out.write(c);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void testCaesar() {
		String inFile = "alice.txt";
		String outFile = "bob.txt";
		String keyStr = "3"; 
		caesar(inFile, outFile, keyStr);
	}
}
