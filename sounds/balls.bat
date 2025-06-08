@echo off
setlocal enabledelayedexpansion

rem Loop over every .mp3 in this directory
for %%F in (*.mp3) do (
  echo Re-encoding "%%F"...
  rem Build a temp filename
  set "TMP=%%~nF.tmp.mp3"
  rem Run ffmpeg to CBR 128k
  ffmpeg -y -loglevel warning -i "%%F" -codec:a libmp3lame -b:a 128k "!TMP!"
  if errorlevel 1 (
    echo   Failed: %%F
    del /q "!TMP!"
  ) else (
    move /y "!TMP!" "%%F" >nul
    echo   Done: %%F
  )
)

echo All done.
pause
