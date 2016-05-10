from websocket import create_connection
from datetime import datetime
"""
Takes a json String and looks for the date field and changes it to the current date.
"""
def changeToCorrectDate(jsonString):
	date = datetime.today()
#	yearString = date.year + "-" + date.month + "-" + date.day
	yearString = date.strftime("%Y-%m-%d")
	print yearString
	pos = jsonString.find("2015-01-01")
	print pos
	jsonString = jsonString.replace("2015-01-01", yearString)
	return jsonString
	
def setUser(jsonString,userId):
	pos = jsonString.find("joe")
	print pos
	jsonString = jsonString.replace("\"joe\"", str(userId))
	print jsonString
	return jsonString

if __name__ == "__main__":
	
	with open('data/example.json', 'r') as bioData:
		json = bioData.read()

		ws = create_connection("ws://localhost:8080/Fakebook/BiometricDataEndpoint")
		print "Sending example.json "
		print changeToCorrectDate(json)
		data = changeToCorrectDate(json)
		data = setUser(data, 1)
		print data
		ws.send(data)
		print "Sent"
		ws.close()
