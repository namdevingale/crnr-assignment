package com.crnr.hcms.assignment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class ValidWindowTest extends AppTest {

	@Test
	public void testBaseCase() {
		assertResultGivenInput("", "Please enter the patient name");
	}

}
