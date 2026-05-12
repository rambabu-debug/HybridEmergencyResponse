# Railway Environment Variables Configuration

## For Development (Local Testing)

Create a `.env` file in the project root (NOT committed to Git):

```env
# Database Configuration
DATABASE_URL=jdbc:postgresql://ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require
DATABASE_USER=neondb_owner
DATABASE_PASSWORD=npg_5wRfogI9nGuM

# Application Port
PORT=8080
```

## For Railway Deployment

In Railway Dashboard → Your Project → Variables:

| Variable Name | Value | Purpose |
|---|---|---|
| `DATABASE_URL` | `postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require` | PostgreSQL connection string |
| `DATABASE_USER` | `neondb_owner` | Database username |
| `DATABASE_PASSWORD` | `npg_5wRfogI9nGuM` | Database password |

**Note**: Railway automatically provides the `PORT` variable, so you don't need to set it.

## Adding Variables in Railway Dashboard

### Method 1: Web Interface

1. Go to https://railway.app
2. Select your project
3. Click on the "Variables" tab
4. Add each variable by clicking "Add Variable"
5. Enter Key and Value
6. Click "Save"

### Method 2: Using Railway CLI

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login

# Navigate to project
railway link

# Set variables
railway variables set DATABASE_URL="postgresql://..."
railway variables set DATABASE_USER="neondb_owner"
railway variables set DATABASE_PASSWORD="npg_5wRfogI9nGuM"
```

## Important Security Notes

⚠️ **NEVER commit credentials to GitHub!**

1. Add `.env` to `.gitignore` (already done)
2. Use environment variables for all sensitive data
3. Change database password in production
4. Use Railway's secret management feature
5. Rotate credentials periodically

## For Production Deployment

### Step 1: Create New Neon Database (Optional but Recommended)

In Neon Console:
1. Create a new project
2. Create a new database
3. Get the connection string
4. Update Railway variables

### Step 2: Update Railway Variables

Replace with production credentials:

```
DATABASE_URL=postgresql://user:password@prod-host/prod_db?sslmode=require
DATABASE_USER=prod_user
DATABASE_PASSWORD=prod_password
```

### Step 3: Enable Database Backups

In Neon Console:
1. Go to Project Settings
2. Enable automated backups
3. Set backup retention period

## Monitoring Variables

After deployment, verify variables are set correctly:

```bash
# Using Railway CLI
railway variables list

# Or check in web dashboard
# Project → Variables tab
```

## Troubleshooting Variable Issues

### Variables not working?

1. **Restart the app** in Railway dashboard
2. **Verify spelling** - Variable names are case-sensitive
3. **Check values** - No extra spaces or quotes
4. **Wait for sync** - Changes may take a few seconds

### Connection string not working?

Verify format:
```
postgresql://user:password@host:5432/database?sslmode=require
```

Check that:
- ✓ Host is correct
- ✓ Port is 5432 (default)
- ✓ Database name is correct
- ✓ User and password have no special characters (or URL-encoded)
- ✓ SSL mode is enabled

## Application Configuration

The application reads from environment variables:

**application.yml**:
```yaml
spring:
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://...}
    username: ${DATABASE_USER:neondb_owner}
    password: ${DATABASE_PASSWORD:npg_5wRfogI9nGuM}

server:
  port: ${PORT:9999}
```

The `:` syntax provides fallback values if variables are not set.

## Further Reading

- [Railway Variables Documentation](https://docs.railway.app/deploy/variables)
- [Spring Boot Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Neon PostgreSQL Setup](https://neon.tech/docs)

