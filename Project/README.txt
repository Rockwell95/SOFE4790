DOMINICK MANCINI -- 100517944
SOFE 4790U Distributed Systems
Final Project Instructions

1. If you have not already done so, download the project to your local machine, and please ensure you have Node.js and Python 2.7 installed:

    https://nodejs.org/en/download/
    https://www.python.org/downloads/
    
**This project is a web application, and is therefore cross-platform and compatible with any operating system.**


2. Open a Terminal or Command Window in the Project Folder

3. If you are running a Unix-Like Operating System (i.e., macOS, Linux, etc.) enter the following command, and then go to step 5:
    ./ElectionMap.sh
    
4. If you are running a non-Unix-like OS (such as Microsoft Windows) the servers will need to be started manually.  
    a. Open two terminal windows.
    
    b. In the first, enter this command: 
        i. node RepresentServer\bin\www #Starts server on port 3000
        
    c. In the other, enter these two commands in the following order: 
        i. cd RepresentClient\app #Changes to client main directory
        ii. python -m SimpleHTTPServer 3001 #Starts client on port 3001
        
    d. Launch the web browser of your choice, and navigate to http://localhost:3001

5. The web application will begin loading, for more verbose information, open the developer tools console (in Google Chrome, you can hit F12 and select the "Console" tab). Please wait for the information to be downloaded from the remote server. The data totals approximately 7MB in size, however, it is retrieved iteratively, making the download process take up to 30 or 40 seconds. Once the data is downloaded, it will be printed to the developer console in JSON format, please allow another 10-20 seconds for the map to draw. During this time, the map will appear to be frozen.

Once the map is populated, the application is ready to use, and the downloaded data is cached, making successive accesses to the site faster.

6. The map should be colour-coded by electoral district, with a marker on your machine's apparent location. The colour coding is as follows:

    - Liberal:          Red
    - Conservative:     Blue
    - NDP:              Orange
    - Bloc Quebecois:   Cyan
    - Green Party:      Green
    - Independent:      Grey
    - Vacant/Other:     Magenta
    
7. To get more information on a district, click anywhere on it. A popup will appear at the click point. Allow 4-5 seconds for the data to download. Once downloaded, the popup will list the district name, its representing party, the member of parliament, the MP's e-mail address, a link to more information on the aforementioned MP, and the population of the district as of the last census.

8. To get new data, click the green refresh button under the map. This will clear the application's cache, and re-download the data from the API.

9. When you are done with the application, it can be terminated by running the EndProgram.sh script. If you are on Windows/Non-Unix-Like, simply type Ctrl+C (^C) into the two terminal windows created in step 4, this will manually terminate the node and python webservers.

**DISCLAIMER: EndProgram.sh issues a general 'killall' command to any processes with the name Python or Node** 
**If you are running and important applications with either of these processes, it is strongly advised to manually terminate the processes from your system monitor**
