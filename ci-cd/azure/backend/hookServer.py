# this server listens for connections on /secret456 from the CI
# and triggers a script containing building istructions to refresh the deployment
from http.server import BaseHTTPRequestHandler,HTTPServer
import os 
import sys, getopt, subprocess

PORT_NUMBER = 1234
script = ""

#This class will handles any incoming request 
class myHandler(BaseHTTPRequestHandler):
	#Handler for the GET requests
    def do_GET(self):
        os.system("echo received >> hooksLog")
        if self.path!="/secret456":
            self.send_error(401,'unauthorized :' + str(self.path))
        else:
            self.send_response(200)
            self.send_header('Content-type','text/html')
            self.end_headers()
            # Send the html message
            self.wfile.write("refreshed project".encode('utf-8'))
            os.system(script + str(" &"))
		
try:
    PORT_NUMBER = 1234
    script = ""
    try:
        opts, args = getopt.getopt(sys.argv[1:],"hp:s:")
    except getopt.GetoptError:
        print ('hookServer.py -p <portNumber> -s <scriptFile>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print ('hookServer.py -p <portNumber> -s <scriptFile>')
            sys.exit()
        elif opt in ("-p"):
            PORT_NUMBER= int(arg)
        elif opt in ("-s"):
            script= arg

	#Create a web server and define the handler to manage the
	#incoming request
    server = HTTPServer(('', PORT_NUMBER), myHandler)
    print ('\n----Started hooks server on port ' , PORT_NUMBER)
	
	#Wait forever for incoming http requests
    server.serve_forever()

except KeyboardInterrupt:
	print ('^C received, shutting down the web server')
	server.socket.close()