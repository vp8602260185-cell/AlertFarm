# AlertKisan HTTPS Configuration Guide

This guide outlines the steps to configure and maintain SSL certificates using **Let's Encrypt** and **Certbot** for the AlertKisan application.

## Overview
* **SSL Provider:** Let's Encrypt (Free)
* **Validity:** 90 Days
* **Domain:** `alertkisan.centralindia.cloudapp.azure.com`

---

## Phase 1: Initial Bootstrap (HTTP Only)

Before generating certificates, Nginx must be running on Port 80 to allow Let's Encrypt to verify domain ownership.

1.  **Modify `nginx.conf`**: Open your Nginx configuration and ensure the `listen 443` and `ssl_certificate` lines are **commented out**. 
2.  **Start Nginx**: Bring up the Nginx service to handle the ACME challenge.
    ```bash
    docker-compose up -d --build nginx
    ```

---

## Phase 2: Generate SSL Certificate

Run this command on your Azure VM to initiate the "webroot" challenge. This will communicate with Let's Encrypt and save the certificate files to your mapped volume.

```bash
sudo docker-compose run --rm certbot certonly --webroot \
  --webroot-path=/var/www/certbot \
  --email vp8602260185@gmail.com \
  --agree-tos \
  --no-eff-email \
  -d alertkisan.centralindia.cloudapp.azure.com
```

---

## Phase 3: Enable HTTPS and Reload

Once the certificates are generated successfully, update your Nginx configuration to enable encryption.

1.  **Modify `nginx.conf`**: Uncomment the `listen 443 ssl` block and ensure the paths point to:
    * `/etc/letsencrypt/live/alertkisan.centralindia.cloudapp.azure.com/fullchain.pem`
    * `/etc/letsencrypt/live/alertkisan.centralindia.cloudapp.azure.com/privkey.pem`
2.  **Rebuild and Restart**: Apply the changes to the Nginx container.
    ```bash
    docker-compose up -d --build nginx
    ```

---

## Phase 4: Maintenance and Renewal

Let's Encrypt certificates expire every 90 days. You should run the renewal check periodically.

### Manual Renewal
To check for and perform a renewal manually:
```bash
docker-compose run --rm certbot renew --quiet
```

### Automated Renewal (Recommended)
Add a Cron job to the host VM to automate this check daily. If the certificate is not near expiration, Certbot will take no action.

**Crontab entry (`crontab -e`):**
```bash
0 0 * * * docker-compose -f /home/azureuser/AlertFarm/docker-compose.yml run --rm certbot renew --quiet && docker-compose -f /home/azureuser/AlertFarm/docker-compose.yml exec -T nginx-proxy nginx -s reload
```

---

### Troubleshooting
* **502 Bad Gateway:** Ensure Nginx is running and the `location /.well-known/acme-challenge/` block is correctly pointing to `/var/www/certbot`.
* **Permission Denied:** Ensure the local `./certbot/conf` and `./certbot/www` directories have the correct permissions for Docker to write to them.