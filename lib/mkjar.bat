
mkdir src
xjc -d src -p io.github.longfish801.zenbun.xml.root root.xsd
javac -d . src\io\github\longfish801\zenbun\xml\root\*.java
jar -cvf root.jar io
rmdir /s /q io
rmdir /s /q src
