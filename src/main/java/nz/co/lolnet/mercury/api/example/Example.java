/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet.mercury.api.example;

import nz.co.lolnet.mercury.api.APIRequest;

/**
 *
 * @author James
 */
public class Example implements APIRequest{

    @Override
    public Object run() {
        return "Hello James";
    }

    @Override
    public String getRequestName() {
        return "james137137";
    }

    @Override
    public String getPermission() {
        return null; //no permission required
    }

    @Override
    public String getOutputClassName() {
       return String.class.getCanonicalName();
    }
    
    
    
}
