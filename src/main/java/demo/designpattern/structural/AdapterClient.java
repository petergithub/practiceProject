package demo.designpattern.structural;

import org.apache.log4j.Logger;

//client和Adaptee之前是什么关系？多对多。一个写好的client处理程序，
//在后续可能会有需要新的处理对象，而这些处理对象不符合 client的接口需要，
//那么就需要对些Adaptee进行适配，适配成满足client需要的Adapter。
//这是一个client对多个 Adaptee的情况。也有多个client对一Adaptee的情况，
//因为多个client都可能需要去执行Adaptee，不同的client需要不同的接口对象，
//那么这个Adaptee就会被适配成不同的接口，这是多个client对一个Adaptee的情况。
//首先，适配器是为client服务的，是先有client，再有Adapter，或者说，是先从client的角度看待，
//发现Adaptee不能满足接口的统一性，需要Adapter。而Adaptee可能在任何Adapter之前
//的任何时候出现都可以。client是我们的代码，而且是已经存在的代码，里面的实现方式是: 
public class AdapterClient {
	Adapter adapter = new AdapterImp();

	void method() {
		adapter.methodAdapter();
	}

	public static void main(String[] args) {
		new AdapterClient().method();
	}
}

// 请注意，这个client里面持有的Adapter是指一个接口或者抽象，并不是说就是适配器。
// 因为我们有client的时候，还不需要适配器。
class Adaptee {
	protected static final Logger logger = Logger.getLogger(Adaptee.class);

	public void methodAdaptee() {
		logger.info("the methodAdaptee in Adaptee which couldn't be invoke directly by Client");
	}
}

// 在client接口一定的情况下，希望client也能去执行adaptee的methodAdaptee方法，
// 但是client只认识adapter的methodAdapter方法，怎么办呢？这个时候，适配器隆重出场了。
// 通过这个转换，我们就可以把Adaptee包裹成Adapter，然后送给client。client发现这是个Adapter，
// 具有methodAdapter方法，从而去执行。但是由于内部实际包装了Adaptee的方法，从而达成了执行Adaptee的方法的目标。
class AdapterImp implements Adapter {
	protected static final Logger logger = Logger.getLogger(Adaptee.class);
	Adaptee adaptee = new Adaptee();

	public void methodAdapter() {
		logger.info("call the methodAdaptee in Adaptee thru AdapterImp which implements interface Adapter. ");
		adaptee.methodAdaptee();
	}
}

interface Adapter {
	void methodAdapter();
}
