package demo.designpattern.interfaceexample;

class Computer {
	public static void main(String[] args) {
		Intel i = new Intel();
		Dmeng d = new Dmeng();
		Mainboard m = new Mainboard();
		m.setCPU(i);
		m.setVideoCard(d);

		m.run();
	}
}

interface CPU {
	void work();

	String getName();
}

interface VideoCard {
	void Display();

	String getName();
}

class Mainboard {
	CPU cpu;
	VideoCard vc;

	void setCPU(CPU cpu) {
		this.cpu = cpu;
	}

	void setVideoCard(VideoCard vc) {
		this.vc = vc;
	}

	void run() {
		System.out.println(cpu.getName());
		cpu.work();
		System.out.println(vc.getName());
		vc.Display();
		System.out.println("Mainboard is running.");
	}
}

class Dmeng implements VideoCard {
	String name;

	public Dmeng() {
		name = "Dmeng's videocard";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void Display() {
		System.out.println("Dmeng's videocard is working.");
	}

	public String getName() {
		return name;
	}
}

class Intel implements CPU {
	String name;

	public Intel() {
		this.name = "Intel's CPU";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void work() {
		System.out.println("Intel's CPU is working.");
	}
}
