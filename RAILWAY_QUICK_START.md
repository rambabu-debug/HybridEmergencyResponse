# Quick Railway Deployment Steps

## Quick Start (5 minutes)

### 1. Build Locally
```bash
./gradlew clean build
```

### 2. Verify Build Success
Check if JAR file exists:
```
build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar
```

### 3. Push to GitHub
```bash
git add .
git commit -m "Ready for Railway deployment"
git push
```

### 4. Deploy to Railway

1. **Go to https://railway.app**

2. **Sign up/Login** with GitHub

3. **Create New Project** → Select "Deploy from GitHub repo"

4. **Choose your repository**

5. **Railway Auto-Detects Gradle Project** ✓

6. **Go to Variables Tab** and add:
   ```
   DATABASE_URL=postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require
   DATABASE_USER=neondb_owner
   DATABASE_PASSWORD=npg_5wRfogI9nGuM
   PORT=8080
   ```

7. **Wait for Deployment** (Usually 3-5 minutes)

8. **Access Your App**
   - Railway will show your public URL
   - Example: `https://hybrid-emergency-response-production.railway.app`

### 5. Test the Application

```bash
curl https://your-project-name.railway.app
```

You should see the HTML response (your dashboard).

## What Railway Does Automatically

✅ Detects Gradle project  
✅ Installs Java 17+  
✅ Runs `gradlew clean build`  
✅ Creates JAR file  
✅ Deploys the application  
✅ Manages SSL/TLS certificates  
✅ Provides public domain  
✅ Auto-scales if needed  
✅ Monitors application health  

## Database Configuration

Your database is already hosted on Neon PostgreSQL:
- **Host**: ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech
- **Database**: neondb
- **User**: neondb_owner
- **Password**: npg_5wRfogI9nGuM (Change in production!)

Tables will be created automatically by Hibernate on first run.

## Monitoring After Deployment

In Railway Dashboard:

1. **Logs** → See real-time application output
2. **Metrics** → Monitor CPU, memory, network
3. **Deployments** → Track deployment history
4. **Environment** → Manage variables

## If Deployment Fails

1. **Check Logs** in Railway dashboard
2. **Verify build locally** runs successfully
3. **Ensure all files are committed** to GitHub
4. **Check environment variables** are set correctly
5. **Verify database connection** in Neon console

## Next Steps

- Add custom domain (optional)
- Set up monitoring alerts
- Configure backup strategy for database
- Update database credentials for production

---

**Your app is now live on the internet! 🎉**

