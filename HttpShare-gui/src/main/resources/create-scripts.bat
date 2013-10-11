@rem create right click share shortcut
set output=right-click-shortcut.reg
echo Windows Registry Editor Version 5.00 > %output%
echo [HKEY_LOCAL_MACHINE\SOFTWARE\Classes\*\shell\Share via HTTP\command] >> %output%
@set double_backslash=%cd%
@set double_backslash=%double_backslash:\=\\%
echo @="\"%double_backslash%\\httpshare.bat\" \"%%1\""  >> %output%

@rem creat run script
echo start %cd%\httpshare.jar %%1 > httpshare.bat
pause

