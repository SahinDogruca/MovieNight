# Project Setup Instructions

To run this project, you'll need to create and configure a `.env` file with your database details.

## Create a `.env` File

Add the following content to your `.env` file:

```env
DB_URL=jdbc:postgresql://<your-database-url>/
DB_NAME=<your-database-name>
DB_USER=<your-database-username>
DB_PASSWORD=<your-database-password>
```

## Sample Admin and User Credentials

For testing or demonstration purposes, you can use the following credentials:

### Admin Account:
- **Username:** `admin`
- **Password:** `admin123`

### User Account:
- **Username:** `alparslan`
- **Password:** `alparslan123`


## Additional Notes

- Ensure your database server is running and accessible from your application environment.
- The project is configured to use **PostgreSQL** by default; if you’re using a different database, update the connection details in the `.env` file accordingly.



