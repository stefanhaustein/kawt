if "%1"=="" goto default
set version=%1
goto doit

:default
set version=kawt

:doit
set verified=%KAWT%\build\kjava\%version%_v
set classes=%KAWT%\build\kjava\%version%_c

if exist %classes% rmdir %classes% /S /Q
if exist %verified% rmdir %verified% /S /Q

java -DKAWT=%KAWT% -cp %KAWT%\bin\ BatchBuilder -zip -jar %KAWT%\build\kjava\%version%.zip -verified %VERIFIED% -d %CLASSES% -pdb %KAWT%\build\kjava\%version%.pdb -bcp %KJAVA_BCP% @%KAWT%\build\files_%VERSION%.txt @%KAWT%\build\files_impl_kjava.txt

call generated.bat


