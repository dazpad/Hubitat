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
        command "recreateChildDevices"
        command "deleteChildren"
        
        attribute "childswitch", "string"

        //fingerprint mfr:"024E", prod:"0021", model:"0014", deviceJoinName: "ZConnect Garage Door Controller"
        //fingerprint deviceId: "0x8700", inClusters: "0x5E,0x27,0x25,0x86,0x73,0x85,0x59,0x8E,0x60,0x70,0x72,0x7A,0x98,0x5A"
   }
    preferences {
        
        input name: "param1", type: "number", defaultValue: "0", required: true,
            title: "Parameter No. 1 - Relay 1 Mode Selection ([0] Normal, [1] Reverse and [2] Strobe)." + "Default value: 0."               
        
        input name: "param2", type: "number", range: "0..65000", defaultValue: "10", required: true,
            title: "Parameter No. 2 - Relay 1 and 2 Closed Contacts Time Interval in Strobe Mode " +
                   "Available settings:\n" +
                   "[1 - 65000] (0,1 s – 6500 s) Time period in miliseconds,\n" +
                   "Default value: 10."
        
        input name: "param3", type: "number", range: "0..65000", defaultValue: "10", required: true,
            title: "Parameter No. 2 - Relay 1 and 2 Open Contacts Time Interval in Strobe Mode " +
                   "Available settings:\n" +
                   "[1 - 65000] (0,1 s – 6500 s) Time period in miliseconds,\n" +
                   "Default value: 10."
        
        input name: "param4", type: "number", range: "0..65000", defaultValue: "5", required: true,
            title: "Parameter No. 4 - Auto off - Relay 1.  " +
                   "Available settings:\n" +
                   "[1 - 65000] (0,1 s – 6500 s) Time period for auto off, in miliseconds,\n" +
                   "0 - Auto off disabled.\n" +
                   "Default value: 5."
        
        input name: "param5", type: "number", range: "0..65000", defaultValue: "0", required: true,
            title: "Parameter No. 5 - Time Delay for ON Command Relay 1  " +
                   "Available settings:\n" +
                   "[1 - 65000] (0,1 s – 6500 s) ON Command Delay, in miliseconds,\n" +
                   "0 - Delay disabled.\n" +
                   "Default value: 0."
        
        input name: "param6", type: "number", range: "0..65000", defaultValue: "0", required: true,
            title: "Parameter No. 6 - Time Delay for OFF Command Relay 1  " +
                   "Available settings:\n" +
                   "[1 - 65000] (0,1 s – 6500 s) OFF Command Delay, in miliseconds,\n" +
                   "0 - Delay disabled.\n" +
                   "Default value: 0."
        
         input name: "param7", type: "number", defaultValue: "0", required: true,
            title: "Parameter No. 7 - Relay 1 State After Power Interruption ([0] Last Known State, [1] Revert to OFF). Default value: 0."
        
        input name: "param9", type: "number", defaultValue: "0", required: true,
            title: "Parameter No. 9 - Hide Relay 2 in Controller Interface ([0] Visible, [1] Not Visible). Default value: 0. Note: Module must be Excluded then Re Included for this parameter to take effect. " 
        
        input name: "param10", type: "number", defaultValue: "0", required: true,
            title: "Parameter No. 10 - Relay 2 Mode Selection ([0] Normal, [1] Reverse and [2] Strobe). Default value: 0."
        
        input name: "param11", type: "number", range: "0..65000", defaultValue: "5", required: true,
            title: "Parameter No. 11 - Auto off - Relay 2.  " +
                   "Available settings:\n" +
                   "[1 - 65000] (0,1 s – 6500 s) Time period for auto off, in miliseconds,\n" +
                   "0 - Auto off disabled.\n" +
                   "Default value: 0."
        
        input name: "param12", type: "number", range: "0..65000", defaultValue: "5", required: true,
            title: "Parameter No. 12 - Time Delay for ON Command Relay 2  " +
                   "Available settings:\n" +
                   "[1 - 65000] (0,1 s – 6500 s) ON Command Delay, in miliseconds,\n" +
                   "0 - Delay disabled.\n" +
                   "Default value: 0."
        
        input name: "param13", type: "number", range: "0..65000", defaultValue: "0", required: true,
            title: "Parameter No. 13 - Time Delay for OFF Command Relay 2  " +
                   "Available settings:\n" +
                   "[1 - 65000] (0,1 s – 6500 s) OFF Command Delay, in miliseconds,\n" +
                   "0 - Delay disabled.\n" +
                   "Default value: 0."
        
         input name: "param14", type: "number", defaultValue: "0", required: true,
            title: "Parameter No. 14 - Relay 2 State After Power Interruption ([0] Last Known State, [1] Revert to OFF). Default value: 0."
        
        input name: "param15", type: "number", defaultValue: "0", required: true,
            title: "Parameter No. 15 - Relay 1 and Relay Association ([0] No Association, [1] Relay1 ON = Relay 2 ON ). Default value: 0."
        
        input name: "param16", type: "number", defaultValue: "0", required: true,
            title: "Parameter No. 16 - Sensor S1 (Ch2) Relay 1 Interlock ([0] No Interlock, [1] Relay 1 Blocked by S1 Triggered). Default value: 0."
        
        
    }      
    
}

def installed() {
    log.debug "installed"
    createChildDevices()
    configure()
}

def updated() {
    log.debug "updated"
    
    if (!childDevices) {
        createChildDevices()
    }
    else if (device.label != state.oldLabel) {
        childDevices.each {
            def newLabel = "$device.displayName (CH${channelNumber(it.deviceNetworkId)})"
            it.setLabel(newLabel)
        }
  
        state.oldLabel = device.label
    }

    configure()
}

def configure() {
    log.debug "Executing 'configure'"
   
   delayBetween([
                   
                   zwave.configurationV1.configurationSet(parameterNumber:1, configurationValue:[param1.value]).format(),            
                   zwave.configurationV1.configurationSet(parameterNumber:2, configurationValue:[param2.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:3, configurationValue:[param3.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:4, configurationValue:[param4.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:5, configurationValue:[param5.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:6, configurationValue:[param6.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:7, configurationValue:[param7.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:9, configurationValue:[param9.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:10, configurationValue:[param10.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:11, configurationValue:[param11.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:12, configurationValue:[param12.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:13, configurationValue:[param13.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:14, configurationValue:[param14.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:15, configurationValue:[param15.value]).format(),
                   zwave.configurationV1.configurationSet(parameterNumber:16, configurationValue:[param16.value]).format()
               ])
}
    

def refresh() {
    log.debug "refresh"
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

def recreateChildDevices() {
    log.debug "recreateChildDevices"
    deleteChildren()
    createChildDevices()
}

def deleteChildren() {
	log.debug "deleteChildren"
	def children = getChildDevices()
    
    children.each {child->
  		deleteChildDevice(child.deviceNetworkId)
    }
}


def parse(String description) {
    log.debug "parse $description"
    def result = null
 
    if (description.startsWith("Err")) {
        result = createEvent(descriptionText:description, isStateChange:true)
    } else {
        def cmd = zwave.parse(description, [0x60: 3, 0x25: 1, 0x20: 1, 0x8E: 2, 0x70: 1, 0x27: 1, 0x85: 1, 0x30: 1])
        log.debug "Command: ${cmd}"
  
        if (cmd) {
            result = zwaveEvent(cmd)
            log.debug "parsed '${description}' to ${result.inspect()}"
        } else {
            log.debug "Unparsed description $description"
        }
    }
 
    result
}


def zwaveEvent(hubitat.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
    log.debug "multichannelv3.MultiChannelCmdEncap $cmd"
    def encapsulatedCommand = cmd.encapsulatedCommand([0x25: 1, 0x20: 1, 0x30: 1])
    log.debug "encapsulatedCommand: $encapsulatedCommand"
 
    if (encapsulatedCommand) {
        return zwaveEvent(encapsulatedCommand, cmd.sourceEndPoint as Integer)
    } else {
        log.debug "Unable to get encapsulated command: $encapsulatedCommand"
        return []
    }
}


def zwaveEvent(hubitat.zwave.commands.basicv1.BasicReport cmd, endpoint = null) {
    log.debug "basicv1.BasicReport $cmd, $endpoint"
    zwaveBinaryEvent(cmd, endpoint, "digital")
}

def zwaveEvent(hubitat.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd, endpoint = null) {
    log.debug "switchbinaryv1.SwitchBinaryReport $cmd, $endpoint"
    zwaveBinaryEvent(cmd, endpoint, "physical")
}

def zwaveEvent(hubitat.zwave.commands.sensorbinaryv1.SensorBinaryReport cmd, endpoint = null) {
    log.debug "sensorbinaryv1.SensorBinaryReport $cmd, $endpoint"
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
        log.debug "childDevice.sendEvent"
        childDevice.sendEvent(name: "contact", value: currentstate)
    }
    
    result
}

def zwaveBinaryEvent(cmd, endpoint, type) {
    log.debug "zwaveBinaryEvent cmd $cmd, endpoint $endpoint, type $type"
    def childDevice = childDevices.find{it.deviceNetworkId.endsWith("$endpoint")}
    def result = null
 
    if (childDevice) {
        log.debug "childDevice.sendEvent $cmd.value"
        childDevice.sendEvent(name: "switch", value: cmd.value ? "on" : "off", type: type)
       
    } else {
        result = createEvent(name: "switch", value: cmd.value ? "on" : "off", type: type)
    }
 
    result
}



def zwaveSensorEvent(cmd, endpoint, type) {
    log.debug "zwaveSensorEvent cmd $cmd, endpoint $endpoint, type $type"
    def childDevice = childDevices.find{it.deviceNetworkId.endsWith("$endpoint")}
    def result = null
    
    if (childDevice) {
        log.debug "childDevice.sendEvent"
        childDevice.sendEvent(name: "contact", value: $currentstate)
    } else {
        result = null
    }
    result

}
        
        
def zwaveEvent(hubitat.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
    log.debug "manufacturerspecificv2.ManufacturerSpecificReport cmd $cmd"
    updateDataValue("MSR", String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId))
}

def zwaveEvent(hubitat.zwave.commands.configurationv2.ConfigurationReport cmd) {
    log.debug "configurationv1.ConfigurationReport: parameter ${cmd.parameterNumber} with a byte size of ${cmd.size} is set to ${cmd.configurationValue}"
}

def zwaveEvent(hubitat.zwave.commands.configurationv1.ConfigurationReport cmd) {
    log.debug "configurationv1.ConfigurationReport: parameter ${cmd.parameterNumber} with a byte size of ${cmd.size} is set to ${cmd.configurationValue}"
}

def zwaveEvent(hubitat.zwave.commands.versionv1.VersionReport cmd, endpoint) {
   log.debug "versionv1.VersionReport, applicationVersion $cmd.applicationVersion, cmd $cmd, endpoint $endpoint"
}

def zwaveEvent(hubitat.zwave.Command cmd, endpoint) {
    log.debug "${device.displayName}: Unhandled ${cmd}" + (endpoint ? " from endpoint $endpoint" : "")
}

def zwaveEvent(hubitat.zwave.Command cmd) {
    log.debug "${device.displayName}: Unhandled ${cmd}"
}

def on() {
    log.debug "on"
    def cmds = []
    cmds << zwave.basicV1.basicSet(value: 0xFF).format()
    cmds << zwave.basicV1.basicGet().format()
    
    
    return delayBetween(cmds, 1000)
}

def off() {
    log.debug "off"
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
    log.debug "onOffCmd, value: $value, endpoint: $endpoint"
    
    def cmds = []
    cmds << encap(zwave.basicV1.basicSet(value: value), endpoint)
    cmds << encap(zwave.basicV1.basicGet(), endpoint)
    
    return delayBetween(cmds, 1000)
}

private channelNumber(String dni) {
    def ch = dni.split("-")[-1] as Integer
    return ch
}

private void createChildDevices() {
    log.debug "createChildDevices"
    
    for (i in 1) {
        addChildDevice("hubitat", "Binary Switch Child Device", "$device.deviceNetworkId-$i", [name: "ch$i", label: "$device.displayName $i", isComponent: true])
    }
    for (i in 2..3) {
        addChildDevice("erocm123", "Contact Sensor Child Device", "$device.deviceNetworkId-$i", [name: "ch$i", label: "$device.displayName $i", isComponent: true])
    }
}



	private encap(cmd, endpoint) {
	    if (endpoint) {
	        zwave.multiChannelV3.multiChannelCmdEncap(destinationEndPoint:endpoint).encapsulate(cmd).format()
	    } else {
	        cmd.format()
	    }
	}