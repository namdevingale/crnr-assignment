package com.crnr.hcms.assignment;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public class AppTest extends SWTBotTestCase {

	static Thread uiThread;
	
	static Shell shell;
	
	private final static CyclicBarrier swtBarrier = new CyclicBarrier(2);
	
	@BeforeClass
	public static synchronized void setupApp() {
		if (uiThread == null) {
			uiThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {	
						while (true) {
							// open and layout the shell
							HcsmAssignmentSwtApplication window = new HcsmAssignmentSwtApplication();
							window.open();
							shell = window.getShell();

							// wait for the test setup
							swtBarrier.await();
							
							// run the event loop
							window.eventLoop(Display.getDefault());
	
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			uiThread.setDaemon(true);
			uiThread.start();
		}
	}

	@Before
	public final void setupSWTBot() throws InterruptedException, BrokenBarrierException {
		// synchronize with the thread opening the shell
		swtBarrier.await();
		bot = new SWTBot(shell);
	}

	@After
	public void closeShell() throws InterruptedException {
		// close the shell
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				shell.close();
			}
		});
	}

	protected void assertResultGivenInput(String input, String expectedResult) {
		bot.textWithLabel("Patient Name : ").setText(input);
		bot.button("Create").click();
		assertEquals(expectedResult, bot.textWithId("messageBox").getText());
	}

}
