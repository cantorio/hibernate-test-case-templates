/*
 * Copyright 2014 JBoss Inc
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
package org.hibernate.bugs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.test.ExtendedSettings;
import org.hibernate.test.ExtendedSettingsHolder;
import org.hibernate.test.ModuleConfiguration;
import org.hibernate.test.SimpleModule;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * Although ORMStandaloneTestCase is perfectly acceptable as a reproducer, usage of this class is much preferred.
 * Since we nearly always include a regression test with bug fixes, providing your reproducer using this method
 * simplifies the process.
 *
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
public class ORMUnitTestCase extends BaseCoreFunctionalTestCase {

	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
		};
	}

	// If you use *.hbm.xml mappings, instead of annotations, add the mappings here.
	@Override
	protected String[] getMappings() {
		return new String[] {
				"Metadata.hbm.xml"
		};
	}
	// If those mappings reside somewhere other than resources/org/hibernate/test, change this.
	@Override
	protected String getBaseForMappings() {
		return "org/hibernate/test/";
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
	}
	
	/** Fill database */
	@Override
	protected void prepareTest() throws Exception {
		super.prepareTest();
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		
		ExtendedSettings settings = new ExtendedSettings();
		settings.setData("some data");
		
		ModuleConfiguration conf = new ModuleConfiguration();
		conf.setSettings(settings);
		settings.getRelatedConfigurations().add(conf);
		
		s.persist(settings);
		
		SimpleModule module = new SimpleModule();
		module.setConfig(conf);
		
		s.persist(module);
		
		ExtendedSettingsHolder holder = new ExtendedSettingsHolder();
		holder.setExtendedSettings(settings);
		
		s.persist(holder);
		
		tx.commit();
		s.close();

	}

	@Test
	public void hhh123TestWithTransactionsError() throws Exception {
		// BaseCoreFunctionalTestCase automatically creates the SessionFactory and provides the Session.
		Session s = openSession();
		
		doInTransaction(s, () -> {
			List<SimpleModule> modules = s.createQuery("from SimpleModule", SimpleModule.class).list();
			assertFalse(modules.isEmpty());
			assertEquals(modules.get(0).getConfig().getSettings().getData(), "some data");
		});

		doInTransaction(s, () -> {
			ExtendedSettingsHolder settingsHolder = s.get(ExtendedSettingsHolder.class, 1L);
			assertNotNull(settingsHolder.getExtendedSettings());
		});

		s.close();
	}

	@Test
	public void hhh123TestWithTransactionsOk() throws Exception {
		// BaseCoreFunctionalTestCase automatically creates the SessionFactory and provides the Session.
		Session s = openSession();
		
		// INFO: order of transactions is changed, comparing to hhh123TestWithTransactionsError
		doInTransaction(s, () -> {
			ExtendedSettingsHolder settingsHolder = s.get(ExtendedSettingsHolder.class, 1L);
			assertNotNull(settingsHolder.getExtendedSettings());
		});
		
		doInTransaction(s, () -> {
			List<SimpleModule> modules = s.createQuery("from SimpleModule", SimpleModule.class).list();
			assertFalse(modules.isEmpty());
			assertEquals(modules.get(0).getConfig().getSettings().getData(), "some data");
		});

		s.close();
	}

	@Test
	public void hhh123TestWithoutTransactionsError() throws Exception {
		// BaseCoreFunctionalTestCase automatically creates the SessionFactory and provides the Session.
		Session s = openSession();
		
		List<SimpleModule> modules = s.createQuery("from SimpleModule", SimpleModule.class).list();
		assertFalse(modules.isEmpty());
		assertEquals(modules.get(0).getConfig().getSettings().getData(), "some data");

		ExtendedSettingsHolder settingsHolder = s.get(ExtendedSettingsHolder.class, 1L);
		assertNotNull(settingsHolder.getExtendedSettings());

		s.close();
	}

	@Test
	public void hhh123TestWithoutTransactionsOk() throws Exception {
		// BaseCoreFunctionalTestCase automatically creates the SessionFactory and provides the Session.
		Session s = openSession();
		
		List<SimpleModule> modules = s.createQuery("from SimpleModule", SimpleModule.class).list();
		assertFalse(modules.isEmpty());
		// here no actual loading of ExtendedSettings occurred => no error
		assertNotNull(modules.get(0).getConfig().getSettings());

		ExtendedSettingsHolder settingsHolder = s.get(ExtendedSettingsHolder.class, 1L);
		assertNotNull(settingsHolder.getExtendedSettings());

		s.close();
	}

	private void doInTransaction(Session s, Runnable task) {
		Transaction tx = s.beginTransaction();
		task.run();
		tx.commit();
	}
}
