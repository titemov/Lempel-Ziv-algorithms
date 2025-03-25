# Lempel-Ziv-algorithms
__WORK IN PROGRESS__

Lempel-Ziv based algorithms: LZ77, LZ78, LZSS. For now program can encode LZ77 (decode and other algorithms wip)

## System requirements
Java version 23.0.1

JavaFX version 23.0.1

## Compilation

### Windows

1. Download latest [JDK](https://download.oracle.com/java/24/latest/jdk-24_windows-x64_bin.exe) and install it (adding variables to PATH).

2. Make sure you have javac.exe compiler `C:\Users\USER\.jdks\jdk-{version}\bin\javac.exe`

3. Download latest [JavaFx](https://gluonhq.com/products/javafx/) library and extract contents whereever it is reachable.

4. To compile, open command line in __source code directory__ and write  
   `javac.exe --module-path {Path to your javafx lib folder} --add-modules=javafx.controls Main.java`

    - For example if JavaFx contents extracted to `С:\Java`
      Then command should look like this:  
      `javac.exe --module-path "C:\Java\javafx-sdk-23.0.1\lib" --add-modules=javafx.controls Main.java` 
    - If you have problems with javac.exe, try to write __absolute path__ to javac.exe (see p.2)
      

5. After compilation run `Main` file:  
   `java --module-path {Path to your javafx lib folder} --add-modules=javafx.controls Main`

### Linux (Debian/Ubuntu)

1. Run `sudo apt update` and `sudo apt upgrade`

2. Install JDK via `sudo apt install default-jdk`

3. Download latest [JavaFx](https://gluonhq.com/products/javafx/) library and extract contents whereever it is reachable.

4. To compile, open command line in __source code directory__ and write  
   `javac --module-path {Path to your javafx lib folder} --add-modules=javafx.controls Main.java`

   - For example if JavaFx contents extracted to `home/USER/Java`
      Then command should look like this:  
      `javac --module-path "home/USER/Java/javafx-sdk-23.0.1/lib" --add-modules=javafx.controls Main.java`

5. After compilation run `Main` file:  
   `java --module-path {Path to your javafx lib folder} --add-modules=javafx.controls Main`
