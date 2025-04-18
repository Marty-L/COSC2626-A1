# COSC2626-A1
## Cloud Computing Assignment 1 - Amazon AWS Web App  

### AWS Tools Used:
* EC2 - Elastic Cloud Compute
* S3 - Simple Storage Service
* API Gateway 
* DynamoDB

## Creating and Populating the Databases
Change the permissions of the Bash script. `chmod u+x src/main/resources/data/createPopulateDB.sh`

Run `./src/main/resources/data/createPopulateDB.sh`


## How to run the Web Application
Follow Lab2 guide:
	* Activity 1 - Create EC2 instance
	
	* Activity 2 - Create Linux VM 
		* Used 24.04 not 20.04 (only free-tier option available)
		* t2-micro
		* Security Group rules as prescribed in lab guide:
			- shh on port 22, source anywhere
			- HTTP on port 80, source anywhere
			- HTTPS on port 443, source anywhere
			- All TCP on ports 0-65535, source anywhere
			- All ICMP - IPv4 all ports, soruce anywhere
			
		* Public IPv4 DNS:ec2-18-208-140-119.compute-1.amazonaws.com
	
	* Activity 3 - Connect to your VM Instance
		* Requires updating labuser.pkk each time with AWS keys
		
	* Activity 4 - Customize your VM Instance
		* Linux commands run:
			* sudo apt upgrade
			* sudo apt-get update
			* sudo apt install openjdk-17-jdk -y
			* java -version
				- Result: 	openjdk version "17.0.14" 2025-01-21
							OpenJDK Runtime Environment (build 17.0.14+7-Ubuntu-124.04)
							OpenJDK 64-Bit Server VM 
							(build 17.0.14+7-Ubuntu-124.04, mixed mode, sharing)
			* which javac
				- Result: /usr/bin/javac
			* sudo apt install maven -y
			* mvn -v
				- Result: 	Apache Maven 3.8.7
							Maven home: /usr/share/maven
							Java version: 17.0.14, vendor: Ubuntu, runtime: /usr/lib/jvm/java-17-openjdk-amd64
							Default locale: en, platform encoding: UTF-8
							OS name: "linux", version: "6.8.0-1024-aws", arch: "amd64", family: "unix"
			* git clone https://github.com/Marty-L/COSC2626-A1.git
			* cd COSC2626-A1
			* vim src/main/resources/application.properties
				- edit in your AWS keys BEFORE building 
			* mvn clean package
				- Tests are deliberately skipped in POM.xml:
					<plugins>
					<! --- ...other plugins... -->
						<plugin>
							<groupId>org.apache.maven.plugins</groupId>
							<artifactId>maven-surefire-plugin</artifactId>
							<version>2.22.2</version>
							<configuration>
								<skipTests>true</skipTests>
							</configuration>
						</plugin>
					</plugins>
			* sudo java -jar target/COSC2626-A1-0.0.1-SNAPSHOT.jar
