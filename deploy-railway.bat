@echo off
REM Railway.app Deployment Script for Windows

echo.
echo ===================================================
echo Hybrid Emergency Response System - Railway Deploy
echo ===================================================
echo.

REM Step 1: Clean and Build
echo [1/5] Building the application...
call gradlew.bat clean build -x test

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Build failed!
    echo Please check the errors above.
    pause
    exit /b 1
)

echo.
echo [SUCCESS] Build completed successfully!
echo JAR file created at: build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar
echo.

REM Step 2: Check Git
echo [2/5] Checking Git status...
git status

echo.
echo [3/5] Next Steps for Railway Deployment:
echo.
echo 1. Commit and push to GitHub:
echo    git add .
echo    git commit -m "Ready for Railway deployment"
echo    git push
echo.
echo 2. Go to https://railway.app
echo.
echo 3. Click "New Project" -^> "Deploy from GitHub repo"
echo.
echo 4. Select your repository
echo.
echo 5. Add Environment Variables in Railway Dashboard:
echo    DATABASE_URL=postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require
echo    DATABASE_USER=neondb_owner
echo    DATABASE_PASSWORD=npg_5wRfogI9nGuM
echo.
echo 6. Railway will automatically deploy your app
echo.
echo 7. Access your app at the provided Railway URL
echo.
echo ===================================================
echo Ready for deployment!
echo ===================================================
pause

