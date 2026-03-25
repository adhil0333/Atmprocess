@echo off
REM ATM System GitHub Push Script
echo ========================================
echo ATM Management System - GitHub Push
echo ========================================
echo.

REM Check if Git is installed
where git >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Git is not installed on your system!
    echo.
    echo Please install Git from: https://git-scm.com/download/win
    echo After installation, restart this script.
    pause
    exit /b 1
)

echo Git found. Initializing repository...
cd /d "%~dp0"

REM Initialize git if not already initialized
if not exist ".git" (
    echo Initializing git repository...
    git init
    echo.
)

REM Add all files
echo Adding files to git...
git add .
echo.

REM Commit
echo Committing changes...
git commit -m "ATM Management System - Complete Implementation with README"
echo.

REM Set main branch
echo Setting default branch to main...
git branch -M main
echo.

REM Add remote
echo Adding remote origin...
git remote remove origin 2>nul
git remote add origin https://github.com/kaniishkank/atmprocess.git
echo.

REM Push to GitHub
echo Pushing to GitHub...
echo.
echo NOTE: You may be prompted to login to GitHub
echo Press ENTER and follow the browser authentication or enter your credentials
echo.
pause
git push -u origin main

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo SUCCESS! Code pushed to GitHub!
    echo Repository: https://github.com/kaniishkank/atmprocess
    echo ========================================
) else (
    echo.
    echo ERROR: Failed to push to GitHub
    echo Make sure:
    echo  1. You have internet connection
    echo  2. Your GitHub credentials are correct
    echo  3. The repository exists at: https://github.com/kaniishkank/atmprocess
)

pause
