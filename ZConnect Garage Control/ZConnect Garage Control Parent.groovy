import groovy.transform.Field

/**
 *  ZConnect Garage Door Controller
 */
metadata {
    definition (name: "ZConnect Garage Door Opener", namespace: "hubitat", author: "dazpad") {
        capability "Refresh"
        capability "Actuator"
        capability "Switch"
        capability "Sensor"
        capability "Configuration"
        
        command "childOn"
        command "childOff"
        
        attribute "childswitch", "string"

        fingerprint mfr:"024E", prod:"0021", model:"0014", deviceJoinName: "ZConnect Garage Door Controller"
        fingerprint deviceId: "0x8700", inClusters: "0x5E,0x27,0x25,0x86,0x73,0x85,0x59,0x8E,0x60,0x70,0x72,0x7A,0x98,0x5A"
    }

    preferences {
	    
	    input name: "txtEnable",            type: "bool", title: "Enable descriptionText logging",  defaultValue: true
        input name: "debugOutput",          type: "bool", title: "Enable debug logging",            defaultValue: false
        input name: "enableComponents",     type: "bool", title: "Enable child devices",            defaultValue: false
        
        input name: "showRelayParameters",  type: "bool", title: "Show relay parameters",           defaultValue: false

        //
        // Relay configuration parameters
        //
        if (showRelayParameters) {
            input name: "param1", type: "enum", options: [0: "Normal", 1: "Reverse", 2: "Strobe"], defaultValue: "0", required: true,
                 title: "<b>Parameter No. 1</b>",
           description: "<p>Relay 1 mode selection (Normal, Reverse and Strobe).<br>" +
                        "Default value: Normal</p>"
                
            input name: "param2", type: "number", range: "0..65000", defaultValue: "10", required: true,
                 title: "<b>Parameter No. 2</b>",
           description: "<p>Relay 1 and 2 closed contacts time interval in strobe mode.<br>" +
                        "Available settings: [1 - 65000] (0,1 s – 6500 s) Time period in miliseconds.<br>" +
                        "Default value: 10</p>"
                
            input name: "param3", type: "number", range: "0..65000", defaultValue: "10", required: true,
                 title: "<b>Parameter No. 3</b>",
           description: "<p>Relay 1 and 2 open contacts time interval in strobe mode.<br>" +
                        "Available settings: [1 - 65000] (0,1 s – 6500 s) Time period in miliseconds.<br>" +
                        "Default value: 10</p>"
                
            input name: "param4", type: "number", range: "0..65000", defaultValue: "5", required: true,
                 title: "<b>Parameter No. 4</b>",
           description: "<p>Auto off - Relay 1.<br>" +
                        "Available settings:<br>" +
                        "[1 - 65000] (0,1 s – 6500 s) Time period for auto off, in miliseconds.<br>" +
                        "0 - Auto off disabled.<br>" +
                        "Default value: 5</p>"
                
            input name: "param5", type: "number", range: "0..65000", defaultValue: "0", required: true,
                 title: "<b>Parameter No. 5</b>",
           description: "<p>Time delay for ON command relay 1<br>" +
                        "Available settings: [1 - 65000] (0,1 s – 6500 s) ON Command Delay, in miliseconds.<br>" +
                        "0 - Delay disabled.<br>" +
                        "Default value: 0</p>"
                
            input name: "param6", type: "number", range: "0..65000", defaultValue: "0", required: true,
                 title: "<b>Parameter No. 6</b>",
           description: "<p>Time delay for OFF command relay 1<br>" +
                        "Available settings: [1 - 65000] (0,1 s – 6500 s) OFF Command Delay, in miliseconds.<br>" +
                        "0 - Delay disabled.<br>" +
                        "Default value: 0</p>"
                
            input name: "param7", type: "enum", options: [0: "Restore to last known state", 1: "Return to \"off\" position"], defaultValue: "0", required: true,
                 title: "<b>Parameter No. 7</b>",
           description: "<p>Relay 1 state after power interruption<br>" +
                        "Default value: Restore last known state.</p>"

            input name: "param8", type: "enum", options: [0: "Accept all ON/OFF", 1: "Ignore all ON/OFF", 2: "Ignore all ON, accept all OFF", 3: "Accept all ON, ignore all OFF"], defaultValue: 0, required:  true,
                 title: "<b>Parameter No. 8</b>",
           description: "<p>Behaviour towards broadband command to turn all ON / all OFF.</p>"
                
            input name: "param9", type: "enum", options: [0: "Visible", 1: "Not visible"], defaultValue: 0, required: true,
                 title: "<b>Parameter No. 9</b>",
           description: "<p>Hide Relay 2 in Controller Interface. Note: Module must be excluded then re-included for this parameter to take effect.<br>" +
                        "Default value: Visible</p>"
                
            input name: "param10", type: "enum", options: [0: "Normal mode", 1: "Reverse mode", 2: "Strobe mode"], defaultValue: 0, required: true,
                 title: "<b>Parameter No. 10</b>",
           description: "<p>Relay 2 mode selection. Relay 2 can be operated in 3 different modes:<br>" +
                        "a) Normal mode - Closed contacts when On, open contacts when OFF<br>" +
                        "b) Reverse mode - Closed contacts when OFF, open contacts when ON<br>" +
                        "c) Strobe mode - Strobe when ON, open contacts when OFF<br>" +
                        "Default value: Normal mode</p>"
                
            input name: "param11", type: "number", range: "0..65000", defaultValue: "5", required: true,
                 title: "<b>Parameter No. 11</b>",
           description: "<p>Auto off - Relay 2. <br>" +
                        "Available settings: [1 - 65000] (0,1 s – 6500 s) Time period for auto off, in miliseconds.<br>" +
                        "0 - Auto off disabled.<br>" +
                        "Default value: 0</p>"
                
            input name: "param12", type: "number", range: "0..65000", defaultValue: "5", required: true,
                 title: "<b>Parameter No. 12</b>",
           description: "<p>Time delay for ON command relay 2" +
                        "Available settings: [1 - 65000] (0,1 s – 6500 s) ON Command Delay, in miliseconds<br>" +
                        "0 - Delay disabled.<br>" +
                        "Default value: 0</p>"
                
            input name: "param13", type: "number", range: "0..65000", defaultValue: "0", required: true,
                 title: "<b>Parameter No. 13</b>",
           description: "<p>Time delay for OFF command relay 2.<br>" +
                        "Available settings: [1 - 65000] (0,1 s – 6500 s) OFF Command Delay, in miliseconds.<br>" +
                        "0 - Delay disabled.<br>" +
                        "Default value: 0</p>"
                
            input name: "param14", type: "enum", options: [0: "Restore to last known state", 1: "Return to OFF position"], defaultValue: 0, required: true,
                 title: "<b>Parameter No. 14</b>",
           description: "<p>Relay 2 state after power interruption<br>" +
                        "Default: Restore last known state</p>"
                
            input name: "param15", type: "enum", options: [0: "No association", 1: "Associate"], defaultValue: 0, required: true,
                 title: "<b>Parameter No. 15</b>",
           description: "<p>Association between relay 1 and relay 2. When relay 1 is switched ON, relay 2 will be switched ON as well.<br>" +
                        "Default value: No association</p>"
                
            input name: "param16", type: "enum", options: [0: "No interlock", 1: "Interlock"], defaultValue: 0, required: true,
                 title: "<b>Parameter No. 16</b>",
           description: "<p>Relay 1 and sensor 1 interlock. When interlocked, relay 1 is blocked when sensor 1 is triggered.<br>" +
                        "Default value: No interlock.</p>"
        }



        input name: "showSensorParameters", type: "bool", title: "Show sensor parameters",      defaultValue: false

        //
        //  Sensor configuration parameters
        //
        if (showSensorParameters) {
            input name: "param20", type: "enum", options: [0: "No integration", 1: "Sensor 1", 2: "Sensor 2", 3: "Sensor 3", 4: "Sensor 4"], defaultValue: 0, required: true,
                 title: "<b>Parameter No. 20</b>",
           description: "<p>Relay 1 and connected sensor integration. Configure whether sensor does control relay 1. Sensor 3 & 4 are via S-Bus. Sensor 4 is temperature. <br>" +
                        "Default value: No integration.</p>"

            input name: "param21", type: "enum", options: [0: "No integration", 1: "Sensor 1", 2: "Sensor 2", 3: "Sensor 3", 4: "Sensor 4"], defaultValue: 0, required: true,
                 title: "<b>Parameter No. 21</b>",
           description: "<p>Relay 2 and connected sensor integration. Configure whether sensor does control relay 2. Sensor 3 & 4 are via S-Bus. Sensor 4 is temperature. <br>" +
                        "Default value: No integration.</p>"           
            
            input name: "param24", type: "enum", defaultValue: 0, required: true,
               options: [
                            0: "Do nothing", 
                            1: "ON without action, OFF with trigger", 
                            2: "OFF without action, ON with trigger", 
                            3: "ON without action", 
                            4: "OFF without action", 
                            5: "ON with trigger", 
                            6: "OFF with trigger",
                            7: "Send real sensor value",
                            8: "Binary sensor bistable switch mode"
                    ],
                 title: "<b>Parameter No. 24</b>",
           description: "<p>Sensor 1 options values for processing. Devices included in association group 2 will be using this parameter for controlling sensor 1. <br>" +
                        "Bistable switch mode will change relay status to opposite following every event. Only for binary sensor on condition that it was grouped with relay (parameter 20 or 21 has value = 4).<br>"
                        "Default value: Do nothing.</p>"

            input name: "param26", type: "number", range: "10..600", defaultValue: 300, required: true,
                 title: "<b>Parameter No. 26</b>",
           description: "<p>Time interval in sconds to submit sensor 1 readings to controller.<br>" +
                        "Value range: 10-600 seconds<br>" +
                        "0 = not to send received values by force<br>" +
                        "Default value: 300</p>"

            input name: "param29", type: "enum", defaultValue: 0, required: true,
               options: [
                            0: "Do nothing", 
                            1: "ON without action, OFF with trigger", 
                            2: "OFF without action, ON with trigger", 
                            3: "ON without action", 
                            4: "OFF without action", 
                            5: "ON with trigger", 
                            6: "OFF with trigger",
                            7: "Send real sensor value",
                            8: "Binary sensor bistable switch mode"
                    ],
                 title: "<b>Parameter No. 29</b>",
           description: "<p>Sensor 2 options values for processing. Devices included in association group 3 will be using this parameter for controlling sensor 2. <br>" +
                        "Bistable switch mode will change relay status to opposite following every event. Only for binary sensor on condition that it was grouped with relay (parameter 20 or 21 has value = 4).<br>"
                        "Default value: Do nothing.</p>"

            input name: "param31", type: "number", range: "10..600", defaultValue: 300, required: true,
                 title: "<b>Parameter No. 31</b>",
           description: "<p>Time interval in sconds to submit sensor 2 readings to controller.<br>" +
                        "Value range: 10-600 seconds<br>" +
                        "0 = not to send received values by force<br>" +
                        "Default value: 300</p>"

            input name: "param32", type: "number", range: "-60..1000", defaultValue: 0, required: true,
                 title: "<b>Parameter No. 32</b>",
           description: "<p>Multilevel sensor 3 values for turning devices ON in association group 4 or device itself<br>" +
                        "Please note, value selection depends on sensor type and its readings.<br>" +
                        "Default value: 0</p>"

            input name: "param33", type: "number", range: "-60..1000", defaultValue: 0, required: true,
                 title: "<b>Parameter No. 33</b>",
           description: "<p>Multilevel sensor 3 values for turning devices OFF in association group 4 or device itself<br>" +
                        "Please note, value selection depends on sensor type and its readings.<br>" +
                        "Default value: 0</p>"

            input name: "param34", type: "enum", defaultValue: 0, required: true,
               options: [
                            0: "Do nothing", 
                            1: "ON without action, OFF with trigger", 
                            2: "OFF without action, ON with trigger", 
                            3: "ON without action", 
                            4: "OFF without action", 
                            5: "ON with trigger", 
                            6: "OFF with trigger",
                            7: "Send real sensor value",
                            8: "Binary sensor bistable switch mode"
                    ],
                 title: "<b>Parameter No. 34</b>",
           description: "<p>Sensor 3 options values for processing. Devices included in association group 4 will be using this parameter for controlling sensor 3. <br>" +
                        "Bistable switch mode will change relay status to opposite following every event. Only for binary sensor on condition that it was grouped with relay (parameter 20 or 21 has value = 4).<br>"
                        "Default value: Do nothing.</p>"

            //input name: "param35", type: "number", range: "1..100", defaultValue: 0, required: true,
            //     title: "<b>Parameter No. 35</b>",
           //description: "<p>Time interval in sconds to submit sensor 3 readings to controller.<br>" +
           //             "Value range: 1-100 seconds<br>" +
           //             "Default value: Varies. Please see manual.</p>"

            input name: "param36", type: "number", range: "10..600", defaultValue: 300, required: true,
                 title: "<b>Parameter No. 36</b>",
           description: "<p>Enforced time interval in sconds to submit sensor 3 readings to association group 1.<br>" +
                        "Value range: 10-600 seconds<br>" +
                        "0 = not to send received values by force<br>" +
                        "Default value: 300</p>"

            input name: "param37", type: "number", range: "-60..1000", defaultValue: 0, required: true,
                 title: "<b>Parameter No. 37</b>",
           description: "<p>Multilevel sensor 4 values for turning devices ON in association group 5 or device itself<br>" +
                        "Please note, value selection depends on sensor type and its readings.<br>" +
                        "Default value: 0</p>"

            input name: "param38", type: "number", range: "-60..1000", defaultValue: 0, required: true,
                 title: "<b>Parameter No. 38</b>",
           description: "<p>Multilevel sensor 4 values for turning devices OFF in association group 5 or device itself<br>" +
                        "Please note, value selection depends on sensor type and its readings.<br>" +
                        "Default value: 0</p>"

            input name: "param39", type: "enum", defaultValue: 0, required: true,
               options: [
                            0: "Do nothing", 
                            1: "ON without action, OFF with trigger", 
                            2: "OFF without action, ON with trigger", 
                            3: "ON without action", 
                            4: "OFF without action", 
                            5: "ON with trigger", 
                            6: "OFF with trigger",
                            7: "Send real sensor value",
                            8: "Binary sensor bistable switch mode"
                    ],
                 title: "<b>Parameter No. 39</b>",
           description: "<p>Sensor 4 options values for processing. Devices included in association group 5 will be using this parameter for controlling sensor 4. <br>" +
                        "Bistable switch mode will change relay status to opposite following every event. Only for binary sensor on condition that it was grouped with relay (parameter 20 or 21 has value = 4).<br>"
                        "Default value: Do nothing.</p>"        

            //input name: "param40", type: "number", range: "1..100", defaultValue: 0, required: true,
            //     title: "<b>Parameter No. 40</b>",
           //description: "<p>Time interval in sconds to submit sensor 4 readings to controller.<br>" +
            //            "Value range: 1-100 seconds<br>" +
            //            "Default value: Varies. Please see manual.</p>"   

            input name: "param41", type: "number", range: "10..600", defaultValue: 300, required: true,
                 title: "<b>Parameter No. 41</b>",
           description: "<p>Enforced time interval in sconds to submit sensor 4 readings to association group 1.<br>" +
                        "Value range: 10-600 seconds<br>" +
                        "0 = not to send received values by force<br>" +
                        "Default value: 300</p>"         
        }
    }          
}
// Child drivers
@Field final String RELAY_COMPONENT_NAMESPACE = "hubitat"
@Field final String SENSOR_COMPONENT_NAMESPACE = "erocm123"
@Field final String RELAY_COMPONENT_DRIVER = "Binary Switch Child Device"
@Field final String SENSOR_COMPONENT_DRIVER = "Contact Sensor Child Device"



def relay2NetworkId() {
    return "${device.deviceNetworkId}-1"
}
def sensor1NetworkId() {
    return "${device.deviceNetworkId}-2"
}
def sensor2NetworkId() {
    return "${device.deviceNetworkId}-3"
}

def installed() {
    log.trace "zConnect Garage Door Opener installed"
    configure()

    device.updateSetting("txtEnable", [type:"bool", value:true])
}

def updated() {
    logDebug "zConnect Garage Door Opener updated"
	
	unschedule()

	if (debugOutput) {
        runIn(1800, "logsOff")
    }

    if (showRelayParameters || showSensorParameters) {
        runIn(1800, "settingsOff")
    }
    
    createOrRemoveChildDevices()


    if (device.label != state.oldLabel) {
        childDevices.each {
            def newLabel = "$device.displayName (CH${channelNumber(it.deviceNetworkId)})"
            it.setLabel(newLabel)
        }
  
        state.oldLabel = device.label
    }

    configure()
}

def configure() {
    log.trace "zConnect Garage Door Opener executing 'configure'"
   
    def cmds = []
   
    if (param1) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:1, scaledConfigurationValue: param1.toInteger(), size: 1).format()            
    }
    if (param2) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:2, scaledConfigurationValue: param2.value, size: 2).format()
    }
    if (param3) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:3, scaledConfigurationValue: param3.value, size: 2).format()
    }
    if (param4) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:4, scaledConfigurationValue: param4.value, size: 2).format()
    }
    if (param5) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:5, scaledConfigurationValue: param5.value, size: 2).format()
    }
    if (param6) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:6, scaledConfigurationValue: param6.value, size: 2).format()
    }
    if (param7) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:7, scaledConfigurationValue: param7.toInteger(), size: 1).format()
    }
    if (param8) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:8, scaledConfigurationValue: param8.toInteger(), size: 1).format()
    }
    if (param9) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:9, scaledConfigurationValue: param9.toInteger(), size: 1).format()
    }
    if (param10) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:10, scaledConfigurationValue: param10.toInteger(), size: 1).format()
    }
    if (param11) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:11, scaledConfigurationValue: param11.value, size: 2).format()
    }
    if (param12) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:12, scaledConfigurationValue: param12.value, size: 2).format()
    }
    if (param13) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:13, scaledConfigurationValue: param13.value, size: 2).format()
    }
    if (param14) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:14, scaledConfigurationValue: param14.toInteger(), size: 1).format()
    }
    if (param15) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:15, scaledConfigurationValue: param15.toInteger(), size: 1).format()
    }
    if (param16) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:16, scaledConfigurationValue: param16.toInteger(), size: 1).format()
    }
    if (param20) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:20, scaledConfigurationValue: param20.toInteger(), size: 1).format()
    }
    if (param21) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:21, scaledConfigurationValue: param21.toInteger(), size: 1).format()
    }
    if (param24) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:24, scaledConfigurationValue: param24.toInteger(), size: 1).format()
    }
    if (param26) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:26, scaledConfigurationValue: param26.value, size: 2).format()
    }
    if (param29) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:29, scaledConfigurationValue: param29.toInteger(), size: 1).format()
    }
    if (param31) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:31, scaledConfigurationValue: param31.value, size: 2).format()
    }
    if (param32) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:32, scaledConfigurationValue: param32.value, size: 2).format()
    }
    if (param33) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:33, scaledConfigurationValue: param33.value, size: 2).format()
    }
    if (param34) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:34, scaledConfigurationValue: param34.toInteger(), size: 1).format()
    }
    //if (param35) {
    //    cmds += zwave.configurationV1.configurationSet(parameterNumber:35, scaledConfigurationValue: param35.value, size: 2).format()
    //}
    if (param36) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:36, scaledConfigurationValue: param36.value, size: 2).format()
    }
    if (param37) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:37, scaledConfigurationValue: param37.value, size: 2).format()
    }
    if (param38) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:38, scaledConfigurationValue: param38.value, size: 2).format()
    }
    if (param39) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:39, scaledConfigurationValue: param39.toInteger(), size: 1).format()
    }
    //if (param40) {
    //    cmds += zwave.configurationV1.configurationSet(parameterNumber:40, scaledConfigurationValue: param40.value, size: 2).format()
    //}
    if (param41) {
        cmds += zwave.configurationV1.configurationSet(parameterNumber:41, scaledConfigurationValue: param41.value, size: 2).format()
    }

    return delayBetween(cmds, 500)
}
    

def refresh() {
    log.trace "zConnect Garage Door Opener executing 'refresh'"
    def cmds = []
    cmds << zwave.basicV1.basicGet().format()
    cmds << zwave.switchBinaryV1.switchBinaryGet().format()
    cmds << zwave.sensorBinaryV1.sensorBinaryGet().format()
    
    (1).each { endpoint ->
        cmds << encap(zwave.basicV1.basicGet(), endpoint)
        cmds << encap(zwave.switchBinaryV1.switchBinaryGet(), endpoint)
    } 
    (2..3).each { endpoint ->
        cmds << encap(zwave.sensorBinaryV1.sensorBinaryGet(), endpoint)
    }
 
    delayBetween(cmds, 100)
}


def parse(String description) {
    
    logDebug "parse $description"
    def result = null
 
    if (description.startsWith("Err")) {
        result = createEvent(descriptionText:description, isStateChange:true)
    } else {
        def cmd = zwave.parse(description, [0x60: 3, 0x25: 1, 0x20: 1, 0x8E: 2, 0x70: 1, 0x27: 1, 0x85: 1, 0x30: 1])
        logDebug "Command: ${cmd}"
  
        if (cmd) {
            result = zwaveEvent(cmd)
            if (txtEnable) { 
                log.info "parsed '${description}' to ${result.inspect()}"
            }
        } else {
            logDebug "Unparsed description $description"
        }
    }
 
    result
}


def zwaveEvent(hubitat.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
    logDebug "multichannelv3.MultiChannelCmdEncap $cmd"
    def encapsulatedCommand = cmd.encapsulatedCommand([0x25: 1, 0x20: 1, 0x30: 1])
    logDebug "encapsulatedCommand: $encapsulatedCommand"
 
    if (encapsulatedCommand) {
        return zwaveEvent(encapsulatedCommand, cmd.sourceEndPoint as Integer)
    } else {
        logDebug "Unable to get encapsulated command: $encapsulatedCommand"
        return []
    }
}


def zwaveEvent(hubitat.zwave.commands.basicv1.BasicReport cmd, endpoint = null) {
    logDebug "basicv1.BasicReport $cmd, $endpoint"
    zwaveBinaryEvent(cmd, endpoint, "digital")
}

def zwaveEvent(hubitat.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd, endpoint = null) {
    logDebug "switchbinaryv1.SwitchBinaryReport $cmd, $endpoint"
    zwaveBinaryEvent(cmd, endpoint, "physical")
}

def zwaveEvent(hubitat.zwave.commands.sensorbinaryv1.SensorBinaryReport cmd, endpoint = null) {
    logDebug "sensorbinaryv1.SensorBinaryReport $cmd, $endpoint"
    zwaveSensorEvent(cmd, endpoint, "sensor")
    def currentstate
  	if (cmd.sensorValue != 255) {
        //sendEvent(name: "contact", value: "open", descriptionText: "binary sensor $endpoint is open")
		currentstate = "open"
        
       	} else {
        //sendEvent(name: "contact", value: "closed", descriptionText: "binary sensor $endpoint is closed")
    	currentstate = "closed"
        
	}
    def childDevice = childDevices.find{it.deviceNetworkId.endsWith("$endpoint")}
    def result = null
    
    if (childDevice) {
        logDebug "childDevice.sendEvent"
        childDevice.sendEvent(name: "contact", value: currentstate)
    }
    
    result
}

def zwaveBinaryEvent(cmd, endpoint, type) {
    logDebug "zwaveBinaryEvent cmd $cmd, endpoint $endpoint, type $type"
    def childDevice = childDevices.find{it.deviceNetworkId.endsWith("$endpoint")}
    def result = null
 
    if (childDevice) {
        logDebug "childDevice.sendEvent $cmd.value"
        childDevice.sendEvent(name: "switch", value: cmd.value ? "on" : "off", type: type)
       
    } else {
        result = createEvent(name: "switch", value: cmd.value ? "on" : "off", type: type)
    }
 
    result
}



def zwaveSensorEvent(cmd, endpoint, type) {
    logDebug "zwaveSensorEvent cmd $cmd, endpoint $endpoint, type $type"
    def childDevice = childDevices.find{it.deviceNetworkId.endsWith("$endpoint")}
    def result = null
    
    if (childDevice) {
        logDebug "childDevice.sendEvent"
        childDevice.sendEvent(name: "contact", value: $currentstate)
    } else {
        result = null
    }
    result

}
        
        
def zwaveEvent(hubitat.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
    logDebug "manufacturerspecificv2.ManufacturerSpecificReport cmd $cmd"
    updateDataValue("MSR", String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId))
}

def zwaveEvent(hubitat.zwave.commands.configurationv2.ConfigurationReport cmd) {
    logDebug "configurationv1.ConfigurationReport: parameter ${cmd.parameterNumber} with a byte size of ${cmd.size} is set to ${cmd.configurationValue}"
}

def zwaveEvent(hubitat.zwave.commands.configurationv1.ConfigurationReport cmd) {
    logDebug "configurationv1.ConfigurationReport: parameter ${cmd.parameterNumber} with a byte size of ${cmd.size} is set to ${cmd.configurationValue}"
}

def zwaveEvent(hubitat.zwave.commands.versionv1.VersionReport cmd, endpoint) {
   logDebug "versionv1.VersionReport, applicationVersion $cmd.applicationVersion, cmd $cmd, endpoint $endpoint"
}

def zwaveEvent(hubitat.zwave.Command cmd, endpoint) {
    logDebug "${device.displayName}: Unhandled ${cmd}" + (endpoint ? " from endpoint $endpoint" : "")
}

def zwaveEvent(hubitat.zwave.Command cmd) {
    logDebug "${device.displayName}: Unhandled ${cmd}"
}

def on() {
    logDebug "on"
    def cmds = []
    cmds << zwave.basicV1.basicSet(value: 0xFF).format()
    cmds << zwave.basicV1.basicGet().format()   
    
    return delayBetween(cmds, 1000)
}

def off() {
    logDebug "off"
    def cmds = []
    cmds << zwave.basicV1.basicSet(value: 0x00).format()
    cmds << zwave.basicV1.basicGet().format()
   
    return delayBetween(cmds, 1000)
}

def childOn(String dni) {
    sendEvent(name: "childswitch", value: "on")
    onOffCmd(0xFF, channelNumber(dni))
}   

def childOff(String dni) {
    sendEvent(name: "childswitch", value: "off")
    onOffCmd(0, channelNumber(dni))
}

private onOffCmd(value, endpoint) {
    logDebug "onOffCmd, value: $value, endpoint: $endpoint"
    
    def cmds = []
    cmds << encap(zwave.basicV1.basicSet(value: value), endpoint)
    cmds << encap(zwave.basicV1.basicGet(), endpoint)
    
    return delayBetween(cmds, 1000)
}

private channelNumber(String dni) {
    def ch = dni.split("-")[-1] as Integer
    return ch
}

private void createOrRemoveChildDevices() {
    logDebug "createChildDevices"
    
    def childRelay2 = getChildDevice(relay2NetworkId())
    def childSensor1 = getChildDevice(sensor1NetworkId())
    def childSensor2 = getChildDevice(sensor2NetworkId())

    //
    // Enable and disable child devices
    //
    if (enableComponents) {
        if (null == childRelay2) {
            log.info "adding child relay 2 device"
            addChildDevice(RELAY_COMPONENT_NAMESPACE, RELAY_COMPONENT_DRIVER, relay2NetworkId(), [name: "ch1", label: "$device.displayName 1", isComponent: true])
        }
        if (null == childSensor1) {
            log.info "adding child sensor 1 device"
            addChildDevice(SENSOR_COMPONENT_NAMESPACE, SENSOR_COMPONENT_DRIVER, sensor1NetworkId(), [name: "ch2", label: "$device.displayName 2", isComponent: true])
        }
        if (null == childSensor2) {
            log.info "adding child sensor 2 device"
            addChildDevice(SENSOR_COMPONENT_NAMESPACE, SENSOR_COMPONENT_DRIVER, sensor2NetworkId(), [name: "ch3", label: "$device.displayName 3", isComponent: true])
        }
    }
    else {
        if (childRelay2) {
            log.trace "removing child relay 2 device"
            deleteChildDevice(relay2NetworkId())
        }
        if (childSensor1) {
            log.trace "removing child sensor 1 device"
            deleteChildDevice(sensor1NetworkId())
        }
        if (childSensor2) {
            log.trace "removing child sensor 2 device"
            deleteChildDevice(sensor2NetworkId())
        }
    }
}

private encap(cmd, endpoint) {
    if (endpoint) {
        zwave.multiChannelV3.multiChannelCmdEncap(destinationEndPoint:endpoint).encapsulate(cmd).format()
    } else {
        cmd.format()
    }
}

def logsOff(){
    log.warn "debug logging disabled..."
    device.updateSetting("debugOutput", [value:"false", type:"bool"])
}

def setttingsOff(){
    log.warn "settings hidden..."
    device.updateSetting("showRelayParameters", [value:"false", type:"bool"])
    device.updateSetting("showSensorParameters", [value:"false", type:"bool"])
}

private logDebug(msg) {
    if (settings?.debugOutput || settings?.debugOutput == null) {
        log.debug "$msg"
    }
}
