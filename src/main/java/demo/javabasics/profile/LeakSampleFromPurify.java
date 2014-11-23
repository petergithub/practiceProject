package demo.javabasics.profile;

import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Frame;
import java.util.Vector;

public class LeakSampleFromPurify extends Frame {
	private static final long serialVersionUID = 1L;
	Runtime rt;
	public boolean bLeakMemory;
	public boolean bLeakOnce;
	public Object oLeakObject;

	@SuppressWarnings("deprecation")
	public LeakSampleFromPurify(String title) {
		super(title);
		bLeakMemory = false;
		bLeakOnce = false;
		oLeakObject = new Object();
		rt = java.lang.Runtime.getRuntime();
		setLayout(null);
		setSize(344, 240);
		label1 = new java.awt.Label("Free Memory");
		label1.setBounds(36, 24, 120, 24);
		add(label1);
		label2 = new java.awt.Label("Total Memory");
		label2.setBounds(36, 60, 120, 24);
		add(label2);
		txtFreeMemory = new java.awt.TextField();
		txtFreeMemory.setBounds(180, 24, 144, 24);
		add(txtFreeMemory);
		txtTotalMemory = new java.awt.TextField();
		txtTotalMemory.setBounds(180, 60, 144, 24);
		add(txtTotalMemory);
		btnStart = new java.awt.Button();
		btnStart.setLabel("Start");
		btnStart.setBounds(108, 144, 144, 36);
		btnStart.setBackground(new Color(12632256));
		add(btnStart);
		btnExit = new java.awt.Button();
		btnExit.setLabel("Exit");
		btnExit.setBounds(108, 190, 144, 36);
		btnExit.setBackground(new Color(12632256));
		add(btnExit);
		Group1 = new CheckboxGroup();
		radioLeakSome = new java.awt.Checkbox("Leak Some", Group1, false);
		radioLeakSome.setBounds(48, 108, 96, 24);
		add(radioLeakSome);
		radioLeakContinuous = new java.awt.Checkbox("Leak Continuously",
				Group1, false);
		radioLeakContinuous.setBounds(180, 108, 144, 24);
		add(radioLeakContinuous);
		// make LeakSome the default state
		radioLeakSome.setState(true);
		// }}
		// {{REGISTER_LISTENERS
		Action lAction = new Action();
		btnStart.addActionListener(lAction);
		btnExit.addActionListener(lAction);
		// }}
		// start the process thread
		@SuppressWarnings("unused")
		Process procThread = new Process(10);
		this.show();
	}

	// {{DECLARE_CONTROLS
	java.awt.Label label1;
	java.awt.Label label2;
	java.awt.TextField txtFreeMemory;
	java.awt.TextField txtTotalMemory;
	java.awt.Button btnStart;
	java.awt.Button btnExit;
	java.awt.Checkbox radioLeakSome;
	CheckboxGroup Group1;
	java.awt.Checkbox radioLeakContinuous;

	// }}
	void btnStart_Clicked(java.awt.event.ActionEvent event) {
		if (radioLeakSome.getState() == true) {
			synchronized (oLeakObject) {
				bLeakMemory = true;
				bLeakOnce = true;
			}
		} else {
			if (btnStart.getLabel() == "Start")
				btnStart.setLabel("Stop");
			else {
				btnStart.setLabel("Start");
				// stop the leak
				synchronized (oLeakObject) {
					bLeakMemory = false;
				}
				return;
			}
			synchronized (oLeakObject) {
				bLeakMemory = true;
				bLeakOnce = false;
			}
		}
	}

	/**
	 * Process
	 */
	class Process extends Thread {
		public Vector<byte[]> vBytes;
		public int cnt;

		public Process(int id) {
			// pre-allocate enough space for specified no of strings
			vBytes = new Vector<byte[]>(id);
			cnt = id;
			this.start();
		}

		public void run() {
			int i = 0;
			while (true) {
				synchronized (oLeakObject) {
					if (bLeakMemory == true) {
						// allocate memory and add a reference to it
						for (i = 0; i <= cnt; i++) {
							vBytes.addElement(new byte[8196]);
						}
						for (i = 0; i < cnt; i++) {
							vBytes.removeElementAt(0);
						}
						if (bLeakOnce == true) {
							bLeakMemory = false;
						}
						txtFreeMemory.setText(String.valueOf(rt.freeMemory()));
						txtTotalMemory
								.setText(String.valueOf(rt.totalMemory()));
					}
				}
				// kill some time between processing
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	class Action implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			Object object = event.getSource();
			if (object == btnStart)
				btnStart_Clicked(event);
			else if (object == btnExit) System.exit(0);
		}
	}

	public static void main(String args[]) {
		@SuppressWarnings("unused")
		LeakSampleFromPurify app = new LeakSampleFromPurify("LeakSample");
	}
}
