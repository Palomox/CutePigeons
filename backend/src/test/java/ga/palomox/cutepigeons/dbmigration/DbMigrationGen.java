package ga.palomox.cutepigeons.dbmigration;

import java.io.IOException;

import io.ebean.dbmigration.DbMigration;
import io.ebean.platform.postgres.PostgresPlatform;

public class DbMigrationGen {
	public static void main(String[] args) {
		DbMigration migration = DbMigration.create();
		migration.setPlatform(new PostgresPlatform());
		migration.setVersion("1.0");
		migration.setName("Initial version");
		try {
			migration.generateMigration();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
