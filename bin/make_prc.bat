set MAIN=%1

if "%MAIN%"=="" goto nok
goto ok
:nok
set MAIN=@files.txt
:ok

java -DKAWT=%KAWT% -cp %KAWT%\bin BatchBuilder -d kjava_c -verified kjava_v -prc -zip -bcp %KJAVA_BCP%;%KAWT%\build\kjava\kawt_io_net_v %MAIN% %2 %3 %4 %5 %6 %7 %8 %9 

generated.bat
