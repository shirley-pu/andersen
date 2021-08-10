# Provided files
Java files for the program are provided.

They were compiled with the following command: 
```
javac -cp .\lib\* *.java 
```

The jar file was created with the following command: 
```
jar vmcf manifestfile RunCurvature.jar .\*
```

## Compiling shared libraries (.dll (Windows), .dylib (Mac OS), .so (Linux)) 
The FORTRAN-77 code comes from Amos 1985: https://dl.acm.org/doi/abs/10.1145/7921.214331

### Windows: 
gfortran was used to compile the .f Fortran code 
```
gfortran -fPIC -c -g -o besselk.o besselk.f 
```

gcc with MinGW64 was used to compile the .c file and the .dll 
```
gcc -shared -o besselk.dll BesselkJINIimp.o besselk.o besselk.def -lgfortran -lgquadmath 
```

### Mac: 
Recompile the Fortran code with the same command as above but on the Mac 

Recompile the .c code. 
Include the path to the JNI Header: 
```
gcc -g -O2 -c -I/Library/Developer/CommandLineTools/SDKs/MacOSX10.15.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers -o BesselkJINIimp.o BesselkJINIimp.c 
```

Compile the shared library
```
Gcc –shared –o besselk.so BesselkJINIimp.o besselk.o -L/usr/local/gfortran/lib/ -lgfortran –lquadmath 
```

**Note**: I compiled to the .so format and renamed it as a .dylib file. Java on Mac looks for .dylib files when loading libraries. **The library name that is used must have 'lib' prepended to the actual file on Mac, I renamed the library after but you can do it when compiling** 

**Note**: You can use 'locate' or the following command to find a library path:
```
sudo find /usr -iname 'libgfortran*.a' | xargs shasum
```
