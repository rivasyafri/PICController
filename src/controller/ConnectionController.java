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
import jssc.SerialPortTimeoutException;
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
    
    public double readData() throws SerialPortTimeoutException {
        try {
            String rawdata = con.readData();
            Double data;
            if (rawdata == null || rawdata.isEmpty()) {
                data = (double) 0;
                readStatus = false;
                connectionStatus = false;
            } else {
                data = Double.valueOf(rawdata);
            }
            return data/1000;
        } catch (SerialPortException ex) {
            System.out.println(ex);
            return 0;
        }
    }
    
    public void startReading(double kp, double ki, double kd, double setpoint, int rotation) throws SerialPortException {
        sendAllData(1,kp,ki,kd,setpoint,rotation);
        readStatus = true;
    }
    
    public void stopReading() throws SerialPortException {
        con.writeData(new Integer(0).toString());
        readStatus = false;
    }
    
    public void modifyData(double kp, double ki, double kd, double setpoint, int rotation) throws SerialPortException {
        if (readStatus) {
            sendAllData(2,kp,ki,kd,setpoint,rotation);
        }
    }
    
    public void setReadStatus(boolean status) {
        readStatus = status;
    }
    
    public boolean getReadStatus() {
        return readStatus;
    }
    
    public void setConnectionStatus(boolean status) {
        connectionStatus = status;
    }
    
    public boolean getConnectionStatus() {
        return connectionStatus;
    }
    
    private void sendAllData(int stat, double kp, double ki, double kd, double setpoint, int rotation) throws SerialPortException {
        con.writeData(Integer.toString(stat));    
        con.writeData(convertData(setpoint));
        con.writeData(convertData(kp));
        con.writeData(convertData(ki));
        con.writeData(convertData(kd));
        con.writeData(Integer.toString(rotation));
    }
    
    private String convertData(double data) {
        String stringdata = Integer.toString((int) (data*1000));
        while (stringdata.length() < 5) {
            stringdata = "0"+stringdata;
        }
        if (stringdata.length() > 5) {
            stringdata = stringdata.substring(0, 5);
        }
        return stringdata;    
    }
}
