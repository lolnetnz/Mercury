/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet.mercury.api;

/**
 *
 * @author James
 */
public interface APIRequest {
    
    public Object run();
    
    public String getRequestName();
    
    public String getPermission();
    
    public String getOutputClassName();
}
