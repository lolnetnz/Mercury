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

package nz.co.lolnet.mercury.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nz.co.lolnet.mercury.Mercury;

public class ConsoleOutput {
	
	public static void info(String string) {
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] [Info] [Mercury]: " + string);
	}
	
	public static void warn(String string) {
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] [Warn] [Mercury]: " + string);
	}
	
	public static void error(String string) {
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] [Error] [Mercury]: " + string);
	}
	
	public static void debug(String string) {
		if (Mercury.getInstance().getConfig() != null && Mercury.getInstance().getConfig().isDebug()) {
			System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] [Debug] [Mercury]: " + string);
		}
	}
}