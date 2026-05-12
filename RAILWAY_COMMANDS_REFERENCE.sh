#!/bin/bash
# Railway Deployment - All Commands Reference

# ============================================================================
# SECTION 1: PREPARATION COMMANDS
# ============================================================================

# Build the application (required before deployment)
# Run this from project root
gradlew clean build              # On Windows: gradlew.bat clean build
gradlew clean build -x test      # Skip tests for faster build

# Verify build success
ls build/libs/*.jar              # Should show the JAR file


# ============================================================================
# SECTION 2: GIT COMMANDS
# ============================================================================

# Initialize repository (if not already done)
git init

# Check git status
git status

# Add all files
git add .

# Commit changes
git commit -m "Hybrid Emergency Response System - Ready for Railway deployment"

# Push to GitHub
git push origin main             # Or 'master' if using master branch

# View commit history
git log --oneline


# ============================================================================
# SECTION 3: VERIFICATION COMMANDS
# ============================================================================

# Check Java version (must be 17 or higher)
java -version

# Check Gradle version
gradlew --version

# Verify JAR was created
jar tf build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar | head

# Check if application can start (local testing)
java -jar build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar

# Test database connection locally
# Set environment variables first, then test


# ============================================================================
# SECTION 4: RAILWAY CLI COMMANDS (Optional - Advanced Users)
# ============================================================================

# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login

# Link to Railway project
railway link

# Check status
railway status

# View environment variables
railway variables list

# Set environment variables
railway variables set DATABASE_URL="postgresql://..."
railway variables set DATABASE_USER="neondb_owner"
railway variables set DATABASE_PASSWORD="npg_5wRfogI9nGuM"

# Deploy
railway up

# View logs
railway logs

# View metrics
railway metrics


# ============================================================================
# SECTION 5: TESTING COMMANDS (After Deployment)
# ============================================================================

# Test if application is running
curl https://your-railway-url.railway.app

# Test API endpoint
curl https://your-railway-url.railway.app/api/health

# Check response headers
curl -i https://your-railway-url.railway.app

# Test with verbose output
curl -v https://your-railway-url.railway.app


# ============================================================================
# SECTION 6: DATABASE COMMANDS
# ============================================================================

# Test PostgreSQL connection (if psql installed locally)
psql postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require

# List tables
\dt

# Count records in users table
SELECT COUNT(*) FROM users;

# View all environment variables
echo $DATABASE_URL
echo $DATABASE_USER
echo $DATABASE_PASSWORD


# ============================================================================
# SECTION 7: TROUBLESHOOTING COMMANDS
# ============================================================================

# Check if port is in use (Windows)
netstat -ano | findstr :8080

# Kill process on specific port (Windows)
taskkill /PID process_id /F

# Check if port is in use (Mac/Linux)
lsof -i :8080

# Kill process on specific port (Mac/Linux)
kill -9 process_id

# Clear Gradle cache (if build issues)
gradlew clean --no-daemon

# Rebuild without cache
gradlew clean build -g /tmp/gradle_cache

# View detailed build log
gradlew build --info

# View full stack trace on error
gradlew build --stacktrace


# ============================================================================
# SECTION 8: ENVIRONMENT VARIABLES (Windows Command Prompt)
# ============================================================================

# Set variables (Windows CMD)
set DATABASE_URL=postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require
set DATABASE_USER=neondb_owner
set DATABASE_PASSWORD=npg_5wRfogI9nGuM

# View variables (Windows CMD)
echo %DATABASE_URL%
echo %DATABASE_USER%
echo %DATABASE_PASSWORD%

# Set variables (Windows PowerShell)
$env:DATABASE_URL = "postgresql://..."
$env:DATABASE_USER = "neondb_owner"
$env:DATABASE_PASSWORD = "npg_5wRfogI9nGuM"

# View variables (Windows PowerShell)
Write-Host $env:DATABASE_URL


# ============================================================================
# SECTION 9: DOCKER COMMANDS (Advanced - Optional)
# ============================================================================

# Build Docker image locally
docker build -t hybrid-emergency-response .

# Run Docker container
docker run -p 8080:8080 -e DATABASE_URL="postgresql://..." hybrid-emergency-response

# View running containers
docker ps

# View logs from container
docker logs container_id


# ============================================================================
# SECTION 10: USEFUL LINKS (Use in Terminal/Browser)
# ============================================================================

# Open websites using command line
# Windows:
start https://railway.app
start https://console.neon.tech
start https://github.com

# Mac:
open https://railway.app
open https://console.neon.tech
open https://github.com

# Linux:
xdg-open https://railway.app
xdg-open https://console.neon.tech
xdg-open https://github.com


# ============================================================================
# SECTION 11: QUICK START SCRIPT (Copy & Paste All Lines)
# ============================================================================

# For Windows (PowerShell)
$commands = @(
    "gradlew.bat clean build",
    "git add .",
    'git commit -m "Ready for Railway deployment"',
    "git push origin main"
)

foreach ($cmd in $commands) {
    Write-Host "Running: $cmd"
    Invoke-Expression $cmd
}

# For Mac/Linux (Bash)
#!/bin/bash
commands=(
    "gradlew clean build"
    "git add ."
    'git commit -m "Ready for Railway deployment"'
    "git push origin main"
)

for cmd in "${commands[@]}"; do
    echo "Running: $cmd"
    eval "$cmd"
done


# ============================================================================
# SECTION 12: MONITORING COMMANDS
# ============================================================================

# Watch application logs live (using Railway CLI)
railway logs --tail

# Check CPU and memory usage
# Windows: Task Manager or
wmic os get totalvisiblememorybytes

# Mac/Linux:
top
free -h

# Monitor network connections
# Windows:
netstat -an

# Mac/Linux:
netstat -an | grep ESTABLISHED


# ============================================================================
# SECTION 13: COMMON ERROR FIXES
# ============================================================================

# If "gradlew not found"
dir gradlew*                     # Verify it exists
./gradlew.bat --version          # Windows
./gradlew --version              # Mac/Linux

# If "Java not found"
java -version
# Install Java 17+ if not present

# If "Git not found"
git --version
# Install Git from https://git-scm.com

# If "Port already in use"
# Kill process using that port (see Section 7)

# If "Build fails"
gradlew clean --no-daemon
gradlew build --stacktrace


# ============================================================================
# SECTION 14: DEPLOYMENT VERIFICATION
# ============================================================================

# After deployment, verify everything works

# 1. Check application is running
curl https://your-railway-url.railway.app

# 2. Test database connection
# Try registering a user through the UI

# 3. Test authentication
# Login with your credentials

# 4. Test features
# Click SOS button, test all features

# 5. Check database
# Go to https://console.neon.tech and verify data


# ============================================================================
# SECTION 15: CLEANUP & OPTIMIZATION
# ============================================================================

# Remove build directory
rm -r build/                     # Mac/Linux
rmdir /s /q build               # Windows

# Remove .gradle directory
rm -r .gradle/                   # Mac/Linux
rmdir /s /q .gradle             # Windows

# Clean up Docker (if used)
docker system prune -a

# Archive old builds
jar czf build-archive-$(date +%Y%m%d).tar.gz build/

# ============================================================================
# END OF REFERENCE
# ============================================================================

# For more information, see:
#   - RAILWAY_QUICK_START.md
#   - RAILWAY_DEPLOYMENT_CHECKLIST.md
#   - RAILWAY_REFERENCE_CARD.txt

