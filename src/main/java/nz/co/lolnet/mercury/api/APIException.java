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

package nz.co.lolnet.mercury.api;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import nz.co.lolnet.mercury.util.ConsoleOutput;
import nz.co.lolnet.mercury.util.JsonResponse;

@Provider
public class APIException implements ExceptionMapper<Throwable> {
	
	@Override
	public Response toResponse(Throwable throwable) {
		
		ConsoleOutput.error("Exception - " + throwable.getClass().getName());
		
		if (!(throwable instanceof ClientErrorException)) {
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(JsonResponse.error("Internal Server Error", throwable.getMessage()))
					.type(MediaType.APPLICATION_JSON)
					.build();
		}
		
		ClientErrorException clientErrorException = (ClientErrorException) throwable;
		return Response
				.status(clientErrorException.getResponse().getStatus())
				.entity(JsonResponse.error(clientErrorException.getResponse().getStatusInfo().getReasonPhrase(), clientErrorException.getMessage()))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}