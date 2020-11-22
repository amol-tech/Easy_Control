cd ..

call mvn clean install -DskipTests -e

cmd /k
echo Press any key...
pause>nul