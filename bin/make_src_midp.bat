%KAWT_DRIVE%
cd %KAWT%\src_java
jar cvfM %KAWT%\build\kawt_src_midp.zip java\awt\*.java java\awt\event\*.java java\awt\image\*.java java\lang\reflect\*.java java\util\EventObject.java 
cd %KAWT%\src_de
jar uvfM %KAWT%\build\kawt_src_midp.zip de\kawt\*.java de\kawt\impl\*.java de\kawt\impl\midp\*.java 

zip %KAWT%\build\kawt_src_midp.zip -d java\awt\FileDialog.java de\kawt\FileList.java de\kawt\FileListActivationListener.java de\kawt\impl\AbstractRecordStore.java