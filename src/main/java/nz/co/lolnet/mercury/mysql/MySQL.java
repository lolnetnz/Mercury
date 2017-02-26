/*
 * Copyright 2017 lolnet.co.nz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.lolnet.mercury.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.entries.Database;
import nz.co.lolnet.mercury.util.ConsoleOutput;

public class MySQL {
	
	private final Database database;
	
	@Deprecated
	public MySQL(String database) {
		this.database = Mercury.getInstance().getConfig().getDatabases().get(database);
	}
	
	public MySQL(Database database) {
		this.database = database;
	}
	
	public Connection getMySQLConnection() {
		if (getDatabase() == null) {
			return null;
		}
		
		try {
			MysqlDataSource mysqlDataSource = new MysqlDataSource();
			mysqlDataSource.setServerName(getDatabase().getHost());
			mysqlDataSource.setPort(getDatabase().getPort());
			mysqlDataSource.setDatabaseName(getDatabase().getDatabase());
			mysqlDataSource.setUser(getDatabase().getUsername());
			mysqlDataSource.setPassword(getDatabase().getPassword());
			return mysqlDataSource.getConnection();
		} catch (SQLException ex) {
			ConsoleOutput.error("Exception getting connection to MySQL database!");
			ex.printStackTrace();
		}
		return null;
	}
	
	private Database getDatabase() {
		return database;
	}
}