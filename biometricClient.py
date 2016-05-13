from websocket import create_connection
from datetime import datetime
import sys
from time import sleep
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
		userId = 1
		if len(sys.argv) == 2:
			userId = int(sys.argv[1])
		elif len(sys.argv) == 3:
			realTime = (sys.argv[2]=="--realtime")
			print realTime

		print "Started sending data."
		if not realTime:
			for line in bioData.readlines():
				data = changeToCorrectDate(line)
				data = setUser(data, 1)
				ws.send(data)
		else:
			# Look for data point in file corresponding to current time.
			sending = False
			previousTime = -1
			for line in bioData.readlines():
				date = datetime.today()
				time = date.strftime("%H:%M:%S")
				parts = line.split("2015-01-01T")
				t = parts[1]
				t = t[:8]
				if not sending:
					if t >= time:
						print "start sending"
						sending = True

				if sending:
					print "Sending data"
					# send data.
					# get time.

					print t + " <-> " + time
					while time < t:
						print "waiting for " + t + "With time = " + time
						# we are not yet at correct time -> wait a second and try again
						sleep(1)
						date = datetime.today()
						time = date.strftime("%H:%M:%S")
					# push data:
					data = changeToCorrectDate(line)
					data = setUser(data, 1)
					ws.send(data)

		print "Sent all data."
		ws.close()
