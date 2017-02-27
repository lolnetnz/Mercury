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
public class getPlayerBalance implements APIRequest{

    String playerName;
    @Override
    public Object run() {
        return 137137;
    }

    @Override
    public String getRequestName() {
        return "getPlayerBalance";
    }

    @Override
    public String getPermission() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getOutputClassName() {
        return Integer.class.getCanonicalName();
    }

    @Override
    public void acceptInputs(Object input) {
        if (input instanceof String)
        {
            playerName = (String) input;
        }
    }
    
}
