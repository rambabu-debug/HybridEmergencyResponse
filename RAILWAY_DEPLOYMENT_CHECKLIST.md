# Railway Deployment Checklist ✓

Complete this checklist to successfully deploy your Hybrid Emergency Response System to Railway.

## Pre-Deployment (Local)

- [ ] **Java 17+ Installed**
  ```bash
  java -version
  ```
  Should show Java 17 or higher

- [ ] **Application Builds Successfully**
  ```bash
  gradlew clean build
  ```
  No errors, JAR file created

- [ ] **JAR File Exists**
  ```
  build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar
  ```

- [ ] **Git Repository Created**
  ```bash
  git init
  git remote -v
  ```

- [ ] **Code Committed to GitHub**
  ```bash
  git add .
  git commit -m "Ready for Railway deployment"
  git push
  ```

- [ ] **Procfile Exists** in project root
  ```
  Procfile (checked into Git)
  ```

- [ ] **application.yml Updated** with environment variables
  ```yaml
  DATABASE_URL: ${DATABASE_URL:...}
  DATABASE_USER: ${DATABASE_USER:...}
  DATABASE_PASSWORD: ${DATABASE_PASSWORD:...}
  PORT: ${PORT:9999}
  ```

- [ ] **Database Credentials Available**
  - [ ] Database host
  - [ ] Database name
  - [ ] Database user
  - [ ] Database password

## Railway Setup

- [ ] **Railway Account Created**
  - Go to https://railway.app
  - Sign up with GitHub

- [ ] **GitHub Connected to Railway**
  - Authorized GitHub access
  - Selected repository

- [ ] **New Project Created in Railway**
  - [ ] Clicked "New Project"
  - [ ] Selected "Deploy from GitHub repo"
  - [ ] Selected correct repository

- [ ] **Environment Variables Set in Railway**
  - [ ] `DATABASE_URL` = Full PostgreSQL connection string
  - [ ] `DATABASE_USER` = neondb_owner
  - [ ] `DATABASE_PASSWORD` = Your password
  
  Example:
  ```
  DATABASE_URL=postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require
  ```

## Deployment

- [ ] **Deployment Started**
  - Railway auto-detected Gradle project
  - Build started (watch the logs)

- [ ] **Build Completed Successfully**
  - Watch logs in Railway dashboard
  - No errors in build output

- [ ] **Application Started**
  - See "Your application is live" message
  - Railway provides public URL

- [ ] **Public URL Assigned**
  - Copy the URL: `https://hybrid-emergency-response-xxx.railway.app`
  - Bookmark it

## Post-Deployment Tests

- [ ] **Application Accessible**
  ```bash
  curl https://your-railway-url.railway.app
  ```
  Should return HTML (not 502 error)

- [ ] **Dashboard Loads**
  - Open URL in browser
  - See the dashboard

- [ ] **Database Connection Works**
  - Try registering a user
  - Check data appears in Neon console

- [ ] **Login Works**
  - Register new user
  - Login with credentials
  - Access main dashboard

- [ ] **SOS Alert Works**
  - Test SOS button
  - Verify data in database

- [ ] **API Endpoints Respond**
  ```bash
  curl https://your-railway-url.railway.app/api/health
  ```

- [ ] **Static Files Load**
  - CSS/JS loads correctly
  - No 404 errors in console

## Monitoring

- [ ] **Logs Accessible**
  - Go to Railway dashboard → Logs tab
  - See application output

- [ ] **Metrics Visible**
  - CPU usage
  - Memory usage
  - Network I/O

- [ ] **No Errors in Logs**
  - Check for SQL errors
  - Check for connection errors

## Optional: Production Hardening

- [ ] **Change Database Credentials**
  - Create new Neon user
  - Update Railway variables

- [ ] **Enable HTTPS** (Automatic on Railway)

- [ ] **Set Up Custom Domain**
  - Configure DNS
  - Add domain in Railway

- [ ] **Enable Backups**
  - Neon automated backups
  - Set retention period

- [ ] **Monitor with Alerts**
  - CPU threshold
  - Memory threshold
  - Deployment failures

## Troubleshooting

If deployment fails, check:

- [ ] **Build Logs**
  - Railway dashboard → Build logs
  - Look for compilation errors

- [ ] **Database Connection**
  - Test connection locally
  - Verify credentials
  - Check Neon DB is accessible

- [ ] **Environment Variables**
  - Verify all variables set
  - Check for typos
  - No extra spaces

- [ ] **Application Logs**
  - Real-time logs in Railway
  - Check for startup errors

- [ ] **GitHub Webhook**
  - Verify Railway has access to GitHub
  - Check webhook in GitHub settings

## Rollback Plan

If something goes wrong:

1. [ ] Go to Railway dashboard
2. [ ] Click on your service
3. [ ] Go to "Deployments" tab
4. [ ] Find previous working deployment
5. [ ] Click "Redeploy"
6. [ ] Wait for deployment to complete

## Final Sign-Off

- [ ] Application is live and accessible
- [ ] All features working correctly
- [ ] Database persisting data
- [ ] Users can register and login
- [ ] SOS alerts working
- [ ] Admin panel accessible
- [ ] No critical errors in logs

**Deployment Complete! 🎉**

---

## Quick Reference URLs

| Item | URL |
|------|-----|
| Railway Dashboard | https://railway.app |
| Your Application | https://hybrid-emergency-response-xxx.railway.app |
| Neon Console | https://console.neon.tech |
| GitHub Repository | https://github.com/YOUR_USERNAME/YOUR_REPO |

## Support Contacts

- Railway Support: https://discord.gg/railway
- Neon Support: https://neon.tech/docs
- Spring Boot Help: https://spring.io/projects/spring-boot

---

**Remember**: Always test in staging before production! 🚀

