# Railway.app Deployment Guide

This guide will help you deploy the Hybrid Emergency Response System to Railway.app.

## Prerequisites

1. **GitHub Account** - To connect your repository to Railway
2. **Railway Account** - Sign up at https://railway.app
3. **Project pushed to GitHub** - Your code must be in a GitHub repository
4. **Built Application** - The application JAR file should be built locally

## Step-by-Step Deployment Instructions

### Step 1: Build the Application Locally

```bash
./gradlew clean build
```

Or on Windows:
```bash
gradlew.bat clean build
```

### Step 2: Push to GitHub

If you haven't already, initialize git and push your code:

```bash
git init
git add .
git commit -m "Initial commit - Hybrid Emergency Response System"
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
git push -u origin main
```

### Step 3: Create a Railway Project

1. Go to https://railway.app
2. Click "New Project"
3. Select "Deploy from GitHub repo"
4. Select your repository
5. Railway will auto-detect the Gradle project

### Step 4: Configure Environment Variables in Railway

In your Railway project dashboard, go to **Variables** and add:

```
DATABASE_URL=postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require
DATABASE_USER=neondb_owner
DATABASE_PASSWORD=npg_5wRfogI9nGuM
PORT=8080
```

**For Production (change these in Neon):**
- Create new database credentials in Neon console
- Update the variables in Railway dashboard

### Step 5: Add a Procfile (Optional but Recommended)

Create a `Procfile` in your project root:

```
web: java -Dserver.port=${PORT} -jar build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar
```

### Step 6: Deploy

Railway will automatically deploy when you push to GitHub. Monitor the deployment:

1. Go to Railway Dashboard
2. Select your service
3. Check the "Deployments" tab for build progress
4. View logs in the "Logs" tab

### Step 7: Access Your Application

Once deployed, Railway will provide a public URL. You can access your application at:

```
https://your-project-name.railway.app
```

## Environment Variables Explanation

| Variable | Purpose |
|----------|---------|
| `DATABASE_URL` | PostgreSQL connection string |
| `DATABASE_USER` | Database username |
| `DATABASE_PASSWORD` | Database password |
| `PORT` | Port to run the application on (Railway provides this) |

## Troubleshooting

### Build Failures

- Check logs in Railway dashboard
- Ensure Java 17+ is being used
- Verify all dependencies are available in Maven Central

### Database Connection Issues

- Verify Neon DB credentials are correct
- Ensure SSL mode is enabled (`sslmode=require`)
- Check that Neon DB allows connections from Railway's IP range

### Application Won't Start

- Check the application logs in Railway
- Verify environment variables are set correctly
- Ensure port configuration is using `${PORT}`

### 502 Bad Gateway Error

- Wait a few minutes for the application to fully start
- Check the health endpoint: `https://your-project-name.railway.app/actuator/health`
- Review logs in Railway dashboard

## Database Schema Migration

The application uses Hibernate with `ddl-auto: update` mode, which:

- Automatically creates tables on first run
- Updates schema if entities change
- Does NOT drop existing data

The schema will be created automatically when the app starts.

## Monitoring

Railway provides built-in monitoring:

- **Logs**: Real-time application logs
- **Metrics**: CPU, Memory, Disk usage
- **Deployments**: Deployment history and status

## Rolling Back

To rollback to a previous deployment:

1. Go to Railway Dashboard
2. Select your service
3. Go to "Deployments" tab
4. Click the previous deployment version
5. Click "Redeploy"

## Custom Domain (Optional)

To add a custom domain:

1. Go to your Railway project settings
2. Select "Domain"
3. Add your custom domain
4. Configure DNS records with your domain provider

## Continuous Deployment

Railway automatically deploys when you push to the connected GitHub repository:

- Push to `main` branch → Automatic deployment
- Each commit creates a new build
- Previous deployments remain accessible

## Support and Resources

- **Railway Docs**: https://docs.railway.app
- **Spring Boot on Railway**: https://docs.railway.app/guides/frameworks/springboot
- **PostgreSQL on Railway**: https://docs.railway.app/guides/databases/postgresql

## Final Notes

- The application uses Spring Boot 3.2.0
- Java 17+ is required
- All data is persisted in Neon PostgreSQL
- SSL/TLS is automatically handled by Railway
- CORS is configured for all `/api/**` endpoints

---

**Happy Deploying! 🚀**

