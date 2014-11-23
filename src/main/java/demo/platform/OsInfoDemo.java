package demo.platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OsInfoDemo {
	private final static Logger log = LoggerFactory.getLogger(OsInfoDemo.class);
	private static final int SLEEP_TIME = 500;
	private static final int PERCENT = 100;
	private static final int FAULTLENGTH = 10;
//	private static final int KB = 1024;

	@Test
	public void testGetOsInfo() {
		String os = System.getProperty("os.name");
		System.out.println("操作系统的版本" + os);

		// 获得线程总数
		ThreadGroup parentThread = Thread.currentThread().getThreadGroup();
		for (; parentThread.getParent() != null; parentThread = parentThread
				.getParent())
			;
		int totalThread = parentThread.activeCount();
		System.out.println("获得线程总数:" + totalThread);
		System.out.println("Mac Address:" + getWindowsMacAddress());
	}

	public static String getWindowsMacAddress() {
		String address = "";
		String os = System.getProperty("os.name");
		if (os == null || !os.startsWith("Windows")) return address;
		BufferedReader br = null;
		try {
			ProcessBuilder pb = new ProcessBuilder("ipconfig", "/all");
			Process p = pb.start();
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			boolean isLocalConnection = false;
			while ((line = br.readLine()) != null) {
				log.debug("line = {}", line);
				if (!isLocalConnection) {
					isLocalConnection = line.contains("本地连接")
							|| line.contains("Local Area Connection");
				}
				if (isLocalConnection) {
					boolean isPhysicalAddress = line.contains("物理地址")
							|| line.contains("Physical Address");
					address = line.substring(line.lastIndexOf(":") + 1).trim();
					if (isPhysicalAddress && !address.isEmpty()) {
						break;
					}
				}
			}
		} catch (IOException e) {
			log.error("Exception in OsInfoDemo.getWindowsMacAddress()", e);
		} finally {
			IoUtils.close(br);
		}
		return address;
	}

	// 内存检测
	/*public void getOsMemory() {
		Runtime rt = Runtime.getRuntime();
		// getting the Runtime instance of the JVM
		// obtaining basic information
		log.info("# of processors: {}", rt.availableProcessors());
		log.info(" Maximum memory: {}", rt.maxMemory());
		log.info("   Total memory: {}", rt.totalMemory());
		log.info("    Free memory: {}", rt.freeMemory());

		// free和use和total均为KB
		long total = rt.totalMemory();
		long free = rt.freeMemory();
		long use = total - free;
		System.out.println("系统内存已用的空间为" + use / KB + " MB");
		System.out.println("系统内存的空闲空间为" + free / KB + " MB");
		System.out.println("系统总内存空间为" + total / KB + " MB");

//		import sun.management.ManagementFactory;
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();
		long physicalMemoryFree = osmxb.getFreePhysicalMemorySize();
		long physicalMemoryTotal = osmxb.getTotalPhysicalMemorySize();
		long physicalMemoryUse = physicalMemoryTotal - physicalMemoryFree;
		System.out.println("系统物理内存已用的空间为" + physicalMemoryFree / KB + " MB");
		System.out.println("系统物理内存的空闲空间为" + physicalMemoryUse / KB + " MB");
		System.out.println("总物理内存" + physicalMemoryTotal / KB + " MB");

		// 获取内存使用率
		// 总的物理内存+虚拟内存
		long virtualMemoryTotal = osmxb.getTotalSwapSpaceSize();
		// 剩余的物理内存
		Double percent = (Double) (1 - physicalMemoryFree * 1.0
				/ virtualMemoryTotal) * 100;
		String str = MessageFormat.format("内存已使用:{0}%", percent.intValue());
		log.info("内存使用率 = {}", str);
	}*/

	@Test
	// 获取文件系统使用率
	public void getWindowsDiskUsage() {
		// 操作系统
		List<String> list = new ArrayList<String>();
		for (char c = 'A'; c <= 'Z'; c++) {
			String dirName = c + ":/";
			File win = new File(dirName);
			if (win.exists()) {
				long total = win.getTotalSpace();
				long free = win.getFreeSpace();
				Double percent = (Double) (1 - free * 1.0 / total) * 100;
				String str = MessageFormat.format("{0}:盘  已使用{1}%", c,
						percent.intValue());
				list.add(str);
			}
		}
		log.info("文件系统使用率 = {}", list);
	}

	@Test
	// 获得cpu使用率
	public void getWindowsCpuRatioForWindows() {
		int ratio = 0;
		try {
			// 取进程信息
			long[] c0 = readWindowsCpu();
			Thread.sleep(SLEEP_TIME);
			long[] c1 = readWindowsCpu();
			if (c0 != null && c1 != null) {
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				ratio = Double.valueOf(
						PERCENT * (busytime) * 1.0 / (busytime + idletime))
						.intValue();
			}
		} catch (InterruptedException e) {
			log.error("Exception in WindowsInfoUtil.getCpuRatioForWindows()", e);
		} catch (IOException e) {
			log.error("Exception in WindowsInfoUtil.getCpuRatioForWindows()", e);
		}
		String percent = MessageFormat.format("CPU使用率:{0}%", ratio);
		log.info("ratio = {}", percent);
	}

	// 读取cpu相关信息
	private static long[] readWindowsCpu() throws IOException {
		String procCmd = System.getenv("windir")
				+ "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
		// C:\WINDOWS\system32\wbem\wmic.exe
		log.info("procCmd = {}", procCmd);
		Process proc = Runtime.getRuntime().exec(procCmd);
		long[] result = new long[2];
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if (line == null || line.length() < FAULTLENGTH) {
				return null;
			}
			int captionIdx = line.indexOf("Caption");
			int commandLineIdx = line.indexOf("CommandLine");
			int kernelModeTimeIdx = line.indexOf("KernelModeTime");
			int readOperationCountIdx = line.indexOf("ReadOperationCount");
			int userModeTimeIdx = line.indexOf("UserModeTime");
			int writeOperationCountIdx = line.indexOf("WriteOperationCount");
			long idleTime = 0;
			long kernelTime = 0;
			long userTime = 0;
			while ((line = input.readLine()) != null) {
				if (line.length() < writeOperationCountIdx) {
					continue;
				}
				// 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
				// ThreadCount,UserModeTime,WriteOperation
				String caption = substring(line, captionIdx, commandLineIdx - 1)
						.trim();
				String cmd = substring(line, commandLineIdx,
						kernelModeTimeIdx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0) {
					continue;
				}
				String kernelModeTime = substring(line, kernelModeTimeIdx,
						readOperationCountIdx - 1).trim();
				String userModeTime = substring(line, userModeTimeIdx,
						writeOperationCountIdx - 1).trim();
				if (caption.equals("System Idle Process")
						|| caption.equals("System")) {
					if (kernelModeTime.length() > 0)
						idleTime += Long.valueOf(kernelModeTime).longValue();
					if (userModeTime.length() > 0)
						idleTime += Long.valueOf(userModeTime).longValue();
					continue;
				}
				if (kernelModeTime.length() > 0)
					kernelTime += Long.valueOf(kernelModeTime).longValue();
				if (userModeTime.length() > 0)
					userTime += Long.valueOf(userModeTime).longValue();
			}
			result[0] = idleTime;
			result[1] = kernelTime + userTime;
			return result;
		} finally {
			IoUtils.close(proc.getInputStream());
		}
	}

	/**
	 * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在 包含汉字的字符串时存在隐患，现调整如下：
	 * 
	 * @param src 要截取的字符串
	 * @param start_idx 开始坐标（包括该坐标)
	 * @param end_idx 截止坐标（包括该坐标）
	 * @return
	 */
	private static String substring(String src, int start_idx, int end_idx) {
		byte[] srcBytes = src.getBytes();
		String result = "";
		for (int i = start_idx; i <= end_idx; i++) {
			result += (char) srcBytes[i];
		}
		return result;
	}
}
