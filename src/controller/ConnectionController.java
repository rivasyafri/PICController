/*
 * Copyright (C) 2015 Riva Syafri Rachmatullah
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package controller;

import javax.swing.Timer;
import jssc.SerialPortException;
import model.SerialConnection;

/**
 * Controller for Serial Connection
 * @author Riva Syafri Rachmatullah
 */
public class ConnectionController {
    private boolean connectionStatus = false;
    private boolean readStatus = false;
    private final int sizeofQueue = 1000;
    private SerialConnection con;
    private Timer timer;
    
    public ConnectionController() {}
    
    public void connect(String COM, int baudRate) throws SerialPortException {
        con = new SerialConnection(COM, baudRate);
        con.open();
        connectionStatus = true;
    }
    
    public void disconnect() throws SerialPortException {
        con.close();
        connectionStatus = false;
    }
    
    public double readData() {
        try {
            String rawdata = con.readData();
            Double data;
            if (rawdata == null || rawdata.isEmpty()) {
                data = (double) 0;
                readStatus = false;
            } else {
                data = Double.valueOf(rawdata);
            }
            return data;
        } catch (SerialPortException ex) {
            System.out.println(ex);
            return 0;
        }
    }
    
    public void switchReadStatus() throws SerialPortException {
        Integer passing;
        if (readStatus) {
            passing = 0;
        } else {
            passing = 1;
        }
        con.writeData(passing.toString());
        readStatus = !readStatus;
    }
    
    public void modifyData() {
        Integer passing;
        if (readStatus) {
            passing = 0;
            sendAllData();
        }
    }
    
    public void setReadStatus(boolean status) {
        readStatus = status;
    }
    
    public boolean getReadStatus() {
        return readStatus;
    }
    
    public boolean getConnectionStatus() {
        return connectionStatus;
    }
    
    private void sendAllData() {
        
    }
}
