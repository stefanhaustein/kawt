set MAIN=%1

if "%MAIN%"=="" goto nok
goto ok
:nok
set MAIN=@files.txt
:ok

java -DKAWT=%KAWT% -cp %KAWT%\bin BatchBuilder -jarx -d midp_c -verified midp_v -bcp %MIDP_BCP%;%KAWT%\build\midp\verified %MAIN% %2 %3 %4 %5 %6 %7 %8 %9 

generated.bat
