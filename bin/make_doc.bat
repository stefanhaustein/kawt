%KAWT_DRIVE%
cd %KAWT%\build
javadoc -group "kAWT Packages (J2ME specific)" "java.awt:java.awt.*" -group "Additional kAWT Packages (J2ME and J2SE)" "de.kawt:de.kawt.shell" -group "CLDC Additions (J2ME specific)" "java.io:java.net:java.util" -group "RMS Package (J2ME and J2SE)" "javax.microedition.rms" -classpath \ -bootclasspath %KJAVA_BCP% -sourcepath %KAWT%\src_de;%KAWT%\src_java;%KAWT%\src_java_addon;%KAWT%\src_javax java.awt java.awt.event java.awt.image java.net java.io java.util de.kawt de.kawt.shell javax.microedition.rms -windowtitle "kAWT API Documentation" -d %KAWT%\doc\api -doctitle "<FONT ID="FrameTitleFont"><B>kAWT - kilobyte Abstract Window Toolkit</B></FONT><h5><B>(c) by Kroll & Haustein GbR 1999-2001</B></h5>" -overview %KAWT%\build\overview.html

zip -r kawt_doc ..\doc\api\*.*  




