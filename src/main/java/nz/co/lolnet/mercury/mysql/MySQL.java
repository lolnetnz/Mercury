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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

import nz.co.lolnet.mercury.Mercury;
import nz.co.lolnet.mercury.entries.Database;
import nz.co.lolnet.mercury.util.LogHelper;

public class MySQL implements AutoCloseable {
	
	private final Database database;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	@Deprecated
	public MySQL(String database) {
		this.database = Mercury.getInstance().getConfig().getDatabases().get(database);
	}
	
	public MySQL(Database database) {
		this.database = database;
	}
	
	public void createConnection() throws SQLException {
		if (getDatabase() == null) {
			throw new SQLException("Database is null!");
		}
		
		MysqlDataSource mysqlDataSource = new MysqlDataSource();
		mysqlDataSource.setServerName(getDatabase().getHost());
		mysqlDataSource.setPort(getDatabase().getPort());
		mysqlDataSource.setDatabaseName(getDatabase().getDatabase());
		mysqlDataSource.setUser(getDatabase().getUsername());
		mysqlDataSource.setPassword(getDatabase().getPassword());
		mysqlDataSource.setLoginTimeout(2500);
		connection = mysqlDataSource.getConnection();
	}
	
	@Override
	public void close() {
		try {
			if (getResultSet() != null) {
				getResultSet().close();
			}
		} catch (SQLException ex) {
			LogHelper.debug("Failed to close ResultSet!");
		}
		
		try {
			if (getPreparedStatement() != null) {
				getPreparedStatement().close();
			}
		} catch (SQLException ex) {
			LogHelper.debug("Failed to close PreparedStatement!");
		}
		
		try {
			if (getConnection() != null) {
				getConnection().close();
			}
		} catch (SQLException ex) {
			LogHelper.debug("Failed to close Connection!");
		}
	}
	
	public Database getDatabase() {
		return database;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}
	
	public void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
}