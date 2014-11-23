package demo.designpattern.creational;

/**
 * 原型模式: 使用原型实例指定将要创建的对象类型，通过拷贝这个实例创建新的对象。
 */
public class PrototypeClient {
	public static void main(String[] args) {
		PackageInfo currentInfo = PackageInfo.clonePackage("John");
		System.out.println("Original package information:");
		display(currentInfo);
		currentInfo.setId(10000l);
		currentInfo.setReceiverName("Ryan");
		currentInfo.setReceiverAddress("People Square, Shanghai");
		System.out.println("\nNew package information:");
		display(currentInfo);
	}

	// other methods…

	private static void display(PackageInfo currentInfo) {
		System.out.println("id = " + currentInfo.getId());
		System.out.println("receiverName = " + currentInfo.getReceiverName());
		System.out.println("receiverAddress = "
				+ currentInfo.getReceiverAddress());
	}
}
class PackageInfo implements Cloneable {
	public PackageInfo clone() {
		try {
			return (PackageInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Cloning not allowed.");
			return null;
		}
	}

	public static PackageInfo clonePackage(String userName) {
		// load package as prototype data from db...
		PackageInfo prototype = loadPackageInfo(userName);
		// clone information ...
		prototype = prototype.clone();
		// initialize copied data ...
		prototype.setId(null);
		return prototype;
	}
	private static PackageInfo loadPackageInfo(String userName) {
		return new PackageInfo(userName);
	}

	public PackageInfo(String userName) {
		this.userName = userName;
	}
	// getters, setters and other methods...
	private long id;
	private String userName;
	private String receiverName;
	private String receiverAddress;
	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}