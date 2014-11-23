package demo.designpattern.structural;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全的组合模式:对于子节点的管理操作（add，remove 和getChild 等操作）放
 * 在了分支类BranchComposite 类里，叶子类BranchLeaf 类看不到这些操作的定义.
 * 因为叶子节点没有add，remove和getChild 这样的操作，
 * 如果尝试调用这些方法，在编译时就会报错
 * <p>
 * 还有一种方式透明的组合模式:把管理子节点的操作放在Component，然后让叶子 类<br>
 * 的这些操 作在运行时失效（可以不做任何处理，也可以抛出异常等）<br>
 * 在运行过程中才可能被发现，但是这种方式具有良好的透明性
 */
public class CompositeClient {
	public static void main(String[] args) {
		BranchComposite china = new BranchComposite("CN", "China Branch");
		BranchComposite shanghai = new BranchComposite("Sh", "ShanghaiBranch");
		BranchLeaf huangpu = new BranchLeaf("Hp", "Huangpu Branch");
		BranchLeaf yangpu = new BranchLeaf("Yp", "Yangpu Branch");
		BranchLeaf pudong = new BranchLeaf("Pd", "Pudong Branch");
		BranchComposite beijing = new BranchComposite("Bj", "Beijing Branch");
		BranchLeaf dongcheng = new BranchLeaf("Dc", "Dongcheng Branch");
		BranchLeaf xicheng = new BranchLeaf("Xc", "Xicheng Branch");
		BranchLeaf haidian = new BranchLeaf("Hd", "Haidian Branch");
		shanghai.add(huangpu);
		shanghai.add(yangpu);
		shanghai.add(pudong);
		beijing.add(dongcheng);
		beijing.add(xicheng);
		beijing.add(haidian);
		china.add(shanghai);
		china.add(beijing);
		System.out.println("Displaying the head bank information");
		display(china);
		System.out.println("\nDisplaying Shanghai bank branch information");
		display(shanghai);
		System.out
				.println("\nDisplaying Pudong bank branch information in Shanghai");
		display(pudong);
	}

	private static void display(BranchComponent branch) {
		branch.display();
	}
	// other methods…
}

abstract class BranchComponent {
	protected String name;
	protected String discription;

	public String getName() {
		throw new UnsupportedOperationException();
	}

	public String getDiscription() {
		throw new UnsupportedOperationException();
	}

	public void display() {
		throw new UnsupportedOperationException();
	}
}

class BranchComposite extends BranchComponent {
	private List<BranchComponent> childrenBranch = new ArrayList<BranchComponent>();

	public BranchComposite(String name, String discription) {
		this.name = name;
		this.discription = discription;
	}

	// other field and methods…
	public void display() {
		System.out.printf("%s: %s\n", name, discription);
		for (BranchComponent child : childrenBranch) {
			child.display();
		}
	}

	public void add(BranchComponent child) {
		childrenBranch.add(child);
	}

	public void remove(BranchComponent child) {
		childrenBranch.remove(child);
	}

	public BranchComponent getChild(int i) {
		return childrenBranch.get(i);
	}
}

class BranchLeaf extends BranchComponent {
	public BranchLeaf(String name, String discription) {
		this.name = name;
		this.discription = discription;
	}

	// fields and constructors…
	public void display() {
		System.out.printf("\t%s: %s\n", name, discription);
	}
	// other methods…
}
