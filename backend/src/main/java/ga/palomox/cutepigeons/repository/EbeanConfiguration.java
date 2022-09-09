package ga.palomox.cutepigeons.repository;

import java.util.Properties;

import io.ebean.DB;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;

public class EbeanConfiguration {
	private String username;
	private String password;
	private String dbUrl;
	private String driver; 
	
	
	public EbeanConfiguration(String username, String password, String dbUrl, String driver) {
		this.username = username;
		this.password = password;
		this.dbUrl = dbUrl;
		this.driver = driver;
	}
	
	public void configure() {
		DatabaseConfig cfg = new DatabaseConfig();

		Properties properties = new Properties();
		properties.put("datasource.db.username", this.username);
		properties.put("datasource.db.password", this.password);
		properties.put("datasource.db.databaseUrl", this.dbUrl);
		properties.put("datasource.db.databaseDriver", this.driver);


		cfg.loadFromProperties(properties);
		cfg.setRegister(true);
		cfg.setDefaultServer(true);
		DatabaseFactory.create(cfg);

		MigrationConfig config = new MigrationConfig();
		config.setMigrationPath("classpath:dbmigration");

		MigrationRunner runner = new MigrationRunner(config);
		runner.run(DB.getDefault().dataSource());

	}
}
