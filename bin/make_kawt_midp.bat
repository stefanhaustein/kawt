set verified=%KAWT%\build\midp\verified
set classes=%KAWT%\build\midp\classes

if exist %classes% rmdir %classes% /S /Q
if exist %verified% rmdir %verified% /S /Q

java -DKAWT=%KAWT% -cp %KAWT%\bin\ BatchBuilder  -jar %KAWT%\build\midp\kawt_midp.zip -verified %VERIFIED% -d %CLASSES% -bcp %MIDP_BCP% @%KAWT%\build\files_kawt.txt @%KAWT%\build\files_impl_midp.txt 
call generated.bat
