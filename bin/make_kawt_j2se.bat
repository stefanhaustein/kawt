set verified=%KAWT%\build\j2se\verified
set classes=%KAWT%\build\j2se\classes

if exist %classes% rmdir %classes% /S /Q
if exist %verified% rmdir %verified% /S /Q

java -DKAWT=%KAWT% -cp %KAWT%\bin\ BatchBuilder -jar %KAWT%\build\j2se\kawt_j2se.zip -verified %VERIFIED% -d %CLASSES% @%KAWT%\build\files_kawt_de.txt @%KAWT%\build\files_impl_j2se.txt
call generated.bat
