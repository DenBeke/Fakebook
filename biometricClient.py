from websocket import create_connection

if __name__ == "__main__":
	
	with open('data/example.json', 'r') as bioData:
		json = bioData.read()

		ws = create_connection("ws://localhost:8080/Fakebook/BiometricDataEndpoint")
		print "Sending  example.json "
		ws.send(json)
		print "Sent"
		ws.close()
