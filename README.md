# SmdXmlConverter
Converts Server Map Data files to XML and vice versa.

TL;DR - How to Use
--------------------------------
1) Place a COPY of the smd files you wish to convert into the smd-sources directory.
The program will only read these files, but you should make a hobby of making backups.
2) Run the program, let the program convert the SMD files to XML in the xml-from-smd folder.
3) Do whatever you want to do with these xml files.  
4) If you want to compile the xml into smd then place the xml file(s)
into xml-to-compile folder. Run the program. Compiled smd will go into smd-compiled

Common Issues
-----------------------
1) On a lot of SMD files, the wape gate data is incorrectly formatted.  If you get a 
crash reading in smd files, check that smd file's warp data out with twostar's 
warp gate editor.

Unlikely issues
--------------------------
1) If you have a very large smd (exceeding 20MB), you'll need to modify the output buffers max size 
in converter.properties : outputBuffer.maxSize.  

Issue Reporting
-----------------
Post a bug report on the snoxd.net thread: (placeholder)
Before reporting an issue, go into converter.properties and change:
output.level=info
to
output.level=debug

Rerun the program, and attach that along with what file likely caused it (smd or xml).  
I'll try to look into it when I have free time.

