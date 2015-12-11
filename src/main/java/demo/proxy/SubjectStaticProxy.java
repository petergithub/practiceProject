package demo.proxy;

public class SubjectStaticProxy implements Subject {
	Subject subimpl = new SubjectImpl();

	public void doSomething() {
		subimpl.doSomething();
	}
}