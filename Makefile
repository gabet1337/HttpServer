
default: prepare
	javac ./src/httpServer/*.java ./src/services/*.java -d ./build

prepare:
	mkdir build

start:
	java -cp build httpServer.WebServer 8010 ./FilesToBeServed

clean:
	rm -rf ./build
