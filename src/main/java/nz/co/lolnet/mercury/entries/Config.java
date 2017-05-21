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

package nz.co.lolnet.mercury.entries;

import java.util.HashMap;

public class Config {
	
	private boolean debug;
	private HashMap<String, Account> accounts;
	private HashMap<String, Database> databases;
	
	public boolean isDebug() {
		return debug;
	}
	
	public HashMap<String, Account> getAccounts() {
		return accounts;
	}
	
	public HashMap<String, Database> getDatabases() {
		return databases;
	}
}