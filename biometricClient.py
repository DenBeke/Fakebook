from websocket import create_connection
from datetime import datetime
import sys
"""
Takes a json String and looks for the date field and changes it to the current date.
"""
def changeToCorrectDate(jsonString):
	date = datetime.today()
#	yearString = date.year + "-" + date.month + "-" + date.day
	yearString = date.strftime("%Y-%m-%d")
	pos = jsonString.find("2015-01-01")
	jsonString = jsonString.replace("2015-01-01", yearString)
	return jsonString
	
def setUser(jsonString,userId):
	pos = jsonString.find("joe")
	jsonString = jsonString.replace("\"joe\"", str(userId))
	return jsonString

if __name__ == "__main__":
	
	with open('data/heart-rate.json', 'r') as bioData:
		ws = create_connection("ws://localhost:8080/Fakebook/BiometricDataEndpoint")
		realTime = False
		if len(sys.argv) > 1:
			realTime = (sys.argv[1]=="--realTime")

		print "Started sending data."
		if not realTime:
			for line in bioData.readlines():

				data = changeToCorrectDate(line)
				data = setUser(data, 1)
				ws.send(data)
		print "Sent all data."
		ws.close()
