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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.OptionalLong;

import org.junit.Before;
import org.junit.Test;

import pl.softech.knf.ofe.opf.OpenPensionFund;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundRepositoryTest {

	private XlsOpenPensionFundRepository repository;

	private void syso(final String template, final Object... args) {
		System.out.println(String.format(template, args));
	}

	@Before
	public void setUp() throws Exception {
	}

	private List<OpenPensionFund> findAll(final String fileName) {

		final URL resource = XlsOpenPensionFundRepositoryTest.class.getClassLoader().getResource(fileName);

		syso("Resource -> %s", resource.getFile());

		repository = new XlsOpenPensionFundRepository(new File(resource.getFile()));

		final List<OpenPensionFund> funds = repository.findAll();

		funds.forEach(f -> syso(f.toString()));

		return funds;

	}

	/**
	 * Test method for
	 * {@link pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository#findAll()}
	 * .
	 */
	@Test
	public void testFindAllForFile1() {

		final List<OpenPensionFund> funds = findAll("dane0402_tcm75-4044.xls");

		assertEquals(1, funds.stream().filter(e -> "Bankowy OFE".equals(e.getName())).count());

		assertEquals(0, funds.stream().filter(e -> "Total".equals(e.getName())).count());

		assertEquals(OptionalLong.of(389963l), funds.stream()//
				.filter(e -> "Bankowy OFE".equals(e.getName()))//
				.mapToLong(OpenPensionFund::getNumberOfMembers)//
				.reduce((l, r) -> l)//
		);

	}

	/**
	 * Test method for
	 * {@link pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository#findAll()}
	 * .
	 */
	@Test
	public void testFindAllForFile2() {

		final List<OpenPensionFund> funds = findAll("dane0104_tcm75-4000.xls");

		assertEquals(1, funds.stream().filter(e -> "Bankowy OFE".equals(e.getName())).count());

		assertEquals(0, funds.stream().filter(e -> "Total".equals(e.getName())).count());

		assertEquals(OptionalLong.of(400643l), funds.stream()//
				.filter(e -> "Bankowy OFE".equals(e.getName()))//
				.mapToLong(OpenPensionFund::getNumberOfMembers)//
				.reduce((l, r) -> l)//
		);

	}

	/**
	 * Test method for
	 * {@link pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository#findAll()}
	 * .
	 */
	@Test
	public void testFindAllForFile3() {

		final List<OpenPensionFund> funds = findAll("dane0204_tcm75-4001.xls");

		assertEquals(1, funds.stream().filter(e -> "Bankowy OFE".equals(e.getName())).count());

		assertEquals(0, funds.stream().filter(e -> "Total".equals(e.getName())).count());

		assertEquals(OptionalLong.of(401378l), funds.stream()//
				.filter(e -> "Bankowy OFE".equals(e.getName()))//
				.mapToLong(OpenPensionFund::getNumberOfMembers)//
				.reduce((l, r) -> l)//
		);

	}

	/**
	 * Test method for
	 * {@link pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository#findAll()}
	 * .
	 */
	@Test
	public void testFindAllForFile4() {

		final List<OpenPensionFund> funds = findAll("2010_01k_tcm75-18109.xls");

		assertEquals(1, funds.stream().filter(e -> "Nordea OFE".equals(e.getName())).count());

		assertEquals(0, funds.stream().filter(e -> "Total".equals(e.getName())).count());

		assertEquals(OptionalLong.of(831028l), funds.stream()//
				.filter(e -> "Nordea OFE".equals(e.getName()))//
				.mapToLong(OpenPensionFund::getNumberOfMembers)//
				.reduce((l, r) -> l)//
		);

	}

}
