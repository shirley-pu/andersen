# Java Application to Calculate Bilayer Deformation Energy 
This is a desktop application which calculates the difference in bilayer deformations energies between two proteins states with different lengths. 
Below is a screenshot of the program on Windows: 
![Screenshot of the app on Windows](https://github.com/shirley-pu/andersen/blob/main/program-screenshot.png?raw=true)

This application is based on the research of the Olaf S. Andersen lab at Weill Cornell Medicine: 
https://physiology.med.cornell.edu/faculty/andersen/lab/

# Instructions 
Below are instructions for using this application.
## Requirements
You must have Java 16.0.2 (2021-07-20 release) or newer to run the program. 
Download the latest Java here: https://www.oracle.com/java/technologies/javase-jdk16-downloads.html

Note: Most users will want to use the installer, not the compressed archive.

**For Mac users:**
You must have gfortran libraries installed. 
Download them here: https://github.com/fxcoudert/gfortran-for-macOS/releases

## Windows 
### 64-bit 
Download the .zip file 'java-app-win-64'. 
Extract the files.

You can click on the RunApp.bat file to run the program.

Alternately: 
Open Command Prompt and type the following commands (Press ENTER after each line). 
```
cd Downloads\java-app-win-64
java -jar RunCurvature.jar
```
**Note** 
The 'cd' command will be different if you do not extract within your Downloads folder. 
You can adjust the command like so: 
* Right-click on the java-app-win-64 folder and select "Properties"
* Copy the file path from Location. 
* Paste this file path after cd

### DLL 
If you encounter an error like this: 
```
Exception in thread "main" java.lang.UnsatisfiedLinkError: C:\Users\shirl\Desktop\Cornell\research\andersen\java app\java-app-win-64\besselk.dll: Can't find dependent libraries
```
Please try downloading the .zip file 'java-app-win-64-dll2'.
Type the same commands into Command Prompt but change the first line to the following: 
```
cd Downloads\java-app-win-64-dll2
```

## Mac 
The current version is for 64 bit machines. 

You can run the program by clicking the RunApp.sh file. 
Alternately:
Open Terminal and type the following commands (Press ENTER after each line). 

```
cd Downloads/java-app-mac
java -jar RunCurvature.jar
```

### Security Warnings
Because the dylib (contains FORTRAN-77 code for the Bessel functions) is unsigned, you may encounter some security warnings. 
Please read the file "mac-instructions" for a more detailed guide on what to do. 
A signed version that does not cause these warnings should be available soon. 

# Notes
More documentation is available in the Wiki. 
Please create an issue in this Github repo if there are any problems. 


