if "%1"=="" goto default
set version=%1
goto doit

:default
set version=kawt

:doit
set verified=%KAWT%\build\jbed\%version%_v
set classes=%KAWT%\build\jbed\%version%_c

if exist %classes% rmdir %classes% /S /Q
if exist %verified% rmdir %verified% /S /Q

md %classes%
md %verified%

java -DKAWT=%KAWT% -cp %KAWT%\bin\ BatchBuilder -verified %VERIFIED% -d %CLASSES% -pdb %KAWT%\build\jbed\%version%.pdb -bcp %JBED_BCP% @%KAWT%\build\files_impl_jbed.txt @%KAWT%\build\files_%VERSION%.txt 
call generated.bat
