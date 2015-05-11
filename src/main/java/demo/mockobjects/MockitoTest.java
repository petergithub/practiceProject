package demo.mockobjects;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * http://mockito.github.io/mockito/docs/current/org/mockito/Mockito.html
 * http://gojko.net/2009/10/23/mockito-in-six-easy-examples/
 * @version Date: Nov 20, 2014 3:01:28 PM
 */
public class MockitoTest {
	
	/**
	 * 1. Let's verify some behavior! 
	 * Once created, mock will remember all interactions. Then you can
	 * selectively verify whatever interaction you are interested in.
	 * 
	 * @throws Exception
	 */
	public void testMockitoVerify() throws Exception {
		//Let's import Mockito statically so that the code looks clearer
		 
		//mock creation
		List<String> mockedList = Mockito.mock(List.class);
		 
		//using mock object
		mockedList.add("one");
		mockedList.clear();
		 
		//verification
		Mockito.verify(mockedList).add("one");
		Mockito.verify(mockedList).clear();

		mockedList.add("one");
		Mockito.verify(mockedList, Mockito.times(2)).add("one");
	}
	
	/**
	 * 2. How about some stubbing? 
	 * By default, for all methods that return value, mock returns null,
	 * an empty collection or appropriate primitive/primitive wrapper value (e.g: 0, false, ... for
	 * int/Integer, boolean/Boolean, ...). Stubbing can be overridden: for example common stubbing can
	 * go to fixture setup but the test methods can override it. Please note that overridding stubbing
	 * is a potential code smell that points out too much stubbing Once stubbed, the method will
	 * always return stubbed value regardless of how many times it is called. Last stubbing is more
	 * important - when you stubbed the same method with the same arguments many times. Other words:
	 * the order of stubbing matters but it is only meaningful rarely, e.g. when stubbing exactly the
	 * same method calls or sometimes when argument matchers are used, etc.
	 * 
	 * @throws Exception
	 */
	public void testMockitoStubbing() throws Exception {
	//You can mock concrete classes, not only interfaces
		LinkedList<String> mockedList = Mockito.mock(LinkedList.class);

		//stubbing
		Mockito.when(mockedList.get(0)).thenReturn("first");
		Mockito.when(mockedList.get(1)).thenThrow(new RuntimeException());

		//following prints "first"
		System.out.println(mockedList.get(0));

		//following throws runtime exception
		System.out.println(mockedList.get(1));

		//following prints "null" because get(999) was not stubbed
		System.out.println(mockedList.get(999));

		//Although it is possible to verify a stubbed invocation, usually it's just redundant
		//If your code cares what get(0) returns then something else breaks (often before even verify() gets executed).
		//If your code doesn't care what get(0) returns then it should not be stubbed. Not convinced? See here.
		Mockito.verify(mockedList).get(0);
	}
	
	/**
	 * To create a stub (or a mock), use mock(class). Then use when(mock).thenReturn(value) to specify
	 * the stub value for a method. If you specify more than one value, they will be returned in
	 * sequence until the last one is used, after which point the last specified value gets returned.
	 * (So to have a method return the same value always, just specify it once)
	 */
	@Test
	public void iterator_will_return_hello_world() {
		// arrange
		Iterator<String> i = Mockito.mock(Iterator.class);
		Mockito.when(i.next()).thenReturn("Hello").thenReturn("World");
		// act
		String result = i.next() + " " + i.next();
		// assert
		assertEquals("Hello World", result);
	}
	
	/**
	 * Stubs can also return different values depending on arguments passed into the method This
	 * creates a stub Comparable object and returns 1 if it is compared to a particular String value
	 * (“Test” in this case)
	 */
	@Test
	public void with_arguments(){
		Comparable<String> c = Mockito.mock(Comparable.class);
		Mockito.when(c.compareTo("Test")).thenReturn(1);
		assertEquals(1,c.compareTo("Test"));
	}
	
	/**
	 * If the method has arguments but you really don’t care what gets passed or cannot predict it,
	 * use anyInt() (and alternative values for other types). This stub comparable returns -1
	 * regardless of the actual method argument. With void methods, this gets a bit tricky as you
	 * can’t use them in the when() call
	 */
	@Test
	public void with_unspecified_arguments(){
		Comparable<Integer> c=Mockito.mock(Comparable.class);
		Mockito.when(c.compareTo(Matchers.anyInt())).thenReturn(-1);
		assertEquals(-1,c.compareTo(5));
	}
	
	/**
	 * The alternative syntax is doReturn(result).when(mock_object).void_method_call(); Instead of
	 * returning, you can also use .thenThrow() or doThrow() for void methods. For example: This
	 * example throws an IOException when the mock OutputStream close method is called. We verify
	 * easily that the OutputStreamWriter rethrows the exception of the wrapped output stream
	 * 
	 * @throws IOException
	 */
	@Test(expected=IOException.class)
	public void OutputStreamWriter_rethrows_an_exception_from_OutputStream() 
		throws IOException{
		OutputStream mock=Mockito.mock(OutputStream.class);
		OutputStreamWriter osw=new OutputStreamWriter(mock);
		Mockito.doThrow(new IOException()).when(mock).close();
		osw.close();
	}
	
	
	/**
	 * To verify actual calls to underlying objects (typical mock object usage), we can use
	 * verify(mock_object).method_call; This example will verify that OutputStreamWriter propagates
	 * the close method call to the wrapped output stream. You can use arguments on methods and
	 * matchers such as anyInt() similar to the previous example. Note that you can’t mix literals and
	 * matchers, so if you have multiple arguments they all have to be either literals or matchers.
	 * use eq(value) matcher to convert a literal into a matcher that compares on value.
	 * 
	 * @throws IOException
	 */
	@Test
	public void OutputStreamWriter_Closes_OutputStream_on_Close()
		 throws IOException{
		OutputStream mock=Mockito.mock(OutputStream.class);
		OutputStreamWriter osw=new OutputStreamWriter(mock);
		osw.close();
		Mockito.verify(mock).close();
	}
	
	
	/**
	 * Mockito comes with lots of matchers already built in, but sometimes you need a bit more
	 * flexibility. For example, OutputStreamWriter will buffer output and then send it to the wrapped
	 * object when flushed, but we don’t know how big the buffer is upfront. So we can’t use equality
	 * matching. However, we can supply our own matcher:
	 * 
	 * @throws IOException
	 */
	@Test
	public void OutputStreamWriter_Buffers_And_Forwards_To_OutputStream() 
		throws IOException{		
		OutputStream mock=Mockito.mock(OutputStream.class);
		OutputStreamWriter osw=new OutputStreamWriter(mock);
		osw.write('a');
		osw.flush();
		// can't do this as we don't know how long the array is going to be
		// verify(mock).write(new byte[]{'a'},0,1);

		BaseMatcher<byte[]> arrayStartingWithA=new BaseMatcher<byte[]> (){
			@Override
			public void describeTo(Description description) {
				// nothing
			}
			// check that first character is A
			@Override
			public boolean matches(Object item) {
				byte[] actual=(byte[]) item;
				return actual[0]=='a';
			}
		};
		// check that first character of the array is A, and that the other two arguments are 0 and 1
		Mockito.verify(mock).write(Matchers.argThat(arrayStartingWithA), Matchers.eq(0), Matchers.eq(1));	
	}
}
