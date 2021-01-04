/*
 * istatistica sensors
 */

//import groovy.transform.Field
import groovy.json.JsonSlurper

metadata {
	definition(name: "iStatistica Sensor Data V2", namespace: "dazpad", author: "Darren Edwards") {
		capability "Sensor"
		capability "Polling"
		capability "TemperatureMeasurement"
		
		
		attribute "PCH_Die", "number"
        attribute "LCD_Proximity", "number"
        attribute "BLC_Proximity", "number"
		attribute "Airflow_2", "number"
		attribute "Airport_Proximity", "number"
		attribute "Memory_Proximity", "number"
		attribute "CPU_1", "number"
		attribute "CPU_1_Proximity", "number"
		attribute "Power_Supply_1_Alt", "number"
		attribute "Mem_bank_A1", "number"
		attribute "CPU_1_Package", "number"
		attribute "PECI_SA", "number"
        attribute "Mem_Bank_A2", "number"
		attribute "CPU_Core_1", "number"
		attribute "PCH_Proximity", "number"
		attribute "PECI_CPU", "number"
		attribute "Airflow_1", "number"
        attribute "Mainboard_Proximity", "number"
		attribute "Mem_Module_A1", "number"
        attribute "GPU_Die", "number"
		attribute "PECI_GPU", "number"
	}
}

preferences {
	section("URIs") {
	
		input name: 'pcip' , type: 'text' , description: "IP Address of Computer Running Statistica Webserver - xxx.xxx.xxx.xxx"
		input name: 'port' , type: 'text' , description: "Webserver Port - default is 4027." , defaultValue: '4027'
		input name: 'updateMins', type: 'enum', description: "Select the update frequency", title: "Update frequency (minutes)\n0 is disabled", defaultValue: '5', options: ['0', '1', '2', '5','10','15','30'], required: true
		input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
	}
}

def logsOff() {
	log.warn "debug logging disabled..."
	device.updateSetting("logEnable", [value: "false", type: "bool"])
}

def updated() {
	unschedule()
	log.info "istatistica sensors updated..."
	log.warn "debug logging is: ${logEnable == true}"
	if (logEnable) runIn(1800, logsOff)
	if(updateMins != "0") {
		schedule("0 */${updateMins} * ? * *", poll)
	}	
}

def parse(String description) {
	if (logEnable) log.debug(description)
}


def poll() {
	if (logEnable) log.debug "sensors polling..."
	def url = "http://${pcip}:${port}/api/sensors"
    
	try {
		httpGet(url) { resp -> 
			if (logEnable) log.debug resp.getData()
          
	def sensors = resp.data.getText()
             log.info "sensors are ...${sensors}"
            
    def respValues = new JsonSlurper().parseText(sensors.trim())
           
          
            sendEvent(name: "PCH_Die", value: respValues."PCH Die")
            sendEvent(name: "LCD_Proximity", value: respValues."LCD Proximity")
            sendEvent(name: "BLC_Proximity", value: respValues."BLC Proximity")
            sendEvent(name: "Airflow_2", value: respValues."Airflow 2")   
			sendEvent(name: "Airport_Proximity", value: respValues."Airport Proximity")
            sendEvent(name: "Memory_Proximity", value: respValues."Memory Proximity")
			sendEvent(name: "CPU_1", value: respValues."CPU 1")
            sendEvent(name: "CPU_1_Proximity", value: respValues."CPU 1 Proximity")
            sendEvent(name: "Power_Supply_1_Alt", value: respValues."Power Supply 1 Alt.")
            sendEvent(name: "Mem_bank_A1", value: respValues."Mem Bank A1")
            sendEvent(name: "CPU_1_Package", value: respValues."CPU 1 Package")
            sendEvent(name: "PECI_SA", value: respValues."PECI SA")
            sendEvent(name: "Mem_Bank_A2", value: respValues."Mem Bank A2")
            sendEvent(name: "CPU_Core_1", value: respValues."CPU Core 1")
            sendEvent(name: "PCH_Proximity", value: respValues."PCH Proximity")
            sendEvent(name: "Mainboard_Proximity", value: respValues."Mainboard Proximity")
            sendEvent(name: "PECI_CPU" , value: respValues."PECI CPU")
            sendEvent(name: "Airflow_1" , value: respValues."Airflow 1")
            sendEvent(name: "Mem_Module_A1", value: respValues."Mem Module A1")
            sendEvent(name: "GPU_Die", value: respValues."GPU Die")
            sendEvent(name: "PECI_GPU", value: respValues."PECI GPU")
			
        }	
	} catch(Exception e) {
		log.debug "error occured calling httpget ${e}"
	}
}
