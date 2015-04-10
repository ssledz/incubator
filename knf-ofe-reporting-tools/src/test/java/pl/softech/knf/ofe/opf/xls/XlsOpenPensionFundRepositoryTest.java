/*
 * Copyright 2015 Sławomir Śledź <slawomir.sledz@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.knf.ofe.opf.xls;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.softech.knf.ofe.opf.OpenPensionFund;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundRepositoryTest {

	private XlsOpenPensionFundRepository repository;

	private void syso(final String template, final Object...args) {
		System.out.println(String.format(template, args));
	}
	
	@Before
	public void setUp() throws Exception {
		final URL resource = XlsOpenPensionFundRepositoryTest.class.getClassLoader().getResource("dane0402_tcm75-4044.xls");
		syso("Resource -> %s", resource.getFile());
		repository = new XlsOpenPensionFundRepository(new File(resource.getFile()));
	}

	/**
	 * Test method for {@link pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository#findAll()}.
	 */
	@Test
	public void testFindAll() {
		
		final List<OpenPensionFund> funds = repository.findAll();
		
	}

}
